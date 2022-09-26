package com.lqs.smb

import android.app.PendingIntent.getActivity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hierynomus.msdtyp.AccessMask
import com.hierynomus.mssmb2.SMB2CreateDisposition
import com.hierynomus.mssmb2.SMB2ShareAccess
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.SmbConfig
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


/**
 * @Description:     java类作用描述
 * @Author:         Linqs
 * @CreateDate:     2022/9/23 11:09
 * @UpdateUser:     更新者：
 * @UpdateDate:     2022/9/23 11:09
 * @UpdateRemark:   更新说明：
 */
class SMBViewModel : ViewModel() {
    var isInit = false
    fun init(context: Context) {
        if (!isInit) {
            isInit = true
            initShareImage()
            initPhoneImage(context)
        }
    }


    private lateinit var session: Session
    private lateinit var connection: Connection
    val client by lazy {
        val config = SmbConfig.builder()
            .withTimeout(
                120,
                TimeUnit.SECONDS
            ) // Timeout sets Read, Write, and Transact timeouts (default is 60 seconds)
            .withSoTimeout(
                180,
                TimeUnit.SECONDS
            ) // Socket Timeout (default is 0 seconds, blocks forever)
            .build();

        SMBClient(config)
    }

    val shareImages by lazy { MutableLiveData<MutableList<String>>() }
    fun initShareImage() {
        thread {
            try {
                val connection: Connection = client.connect("10.123.60.74")
                this@SMBViewModel.connection = connection
                // save the connection reference to be able to close it later manually
                val ac = AuthenticationContext("Share", "Share".toCharArray(), "")
                val session: Session = connection.authenticate(ac)
                this@SMBViewModel.session = session
                (this@SMBViewModel.session.connectShare("Share") as DiskShare).use { share ->
                    val list = share.list("").map { it.fileName }
                    shareImages.postValue(list.toMutableList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun uploadImage(name: String, path: String) {
        val image = File(path)
        if (!image.exists()) {
            return
        }
        thread {
            try {
                image.inputStream().use { input ->
                    (this@SMBViewModel.session.connectShare("Share") as DiskShare).use { share ->
                        share.openFile(
                            name,
                            setOf(AccessMask.GENERIC_ALL),
                            null,
                            SMB2ShareAccess.ALL,
                            SMB2CreateDisposition.FILE_CREATE,
                            null
                        ).outputStream.use { output ->
                            input.copyTo(output, DEFAULT_BUFFER_SIZE)
                        }
                    }
                }
                val list = shareImages.value?.toMutableList()
                list?.add(name)
                shareImages.postValue(list)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val phoneImages by lazy { MutableLiveData<MutableList<Pair<String, String>>>() }

    fun initPhoneImage(context: Context) {
        val mImageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projImage = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val mCursor: Cursor? = context.contentResolver.query(
            mImageUri,
            projImage,
            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
            arrayOf("image/jpeg", "image/png"),
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        )
        if (mCursor != null && mCursor.moveToFirst()) {
            val list = arrayListOf<Pair<String, String>>()
            fun readLine() {
                val dataIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val path = mCursor.getString(dataIndex)
                val nameIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val displayName = mCursor.getString(nameIndex)
                list.add(displayName to path)
                Log.e("phoneImage", "$displayName/ $path")
            }
            readLine()
            while (mCursor.moveToNext()) {
                readLine()
            }
            phoneImages.postValue(list)
        }
    }
}
