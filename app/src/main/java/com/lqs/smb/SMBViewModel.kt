package com.lqs.smb

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hierynomus.msdtyp.AccessMask
import com.hierynomus.mssmb2.SMB2CreateDisposition
import com.hierynomus.mssmb2.SMB2ShareAccess
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.SmbConfig
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import kotlinx.coroutines.async
import java.util.*
import java.util.concurrent.TimeUnit
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

    fun init() {
        thread {
            try {
                val connection: Connection = client.connect("10.123.60.74")
                this@SMBViewModel.connection = connection
                // save the connection reference to be able to close it later manually
                val ac = AuthenticationContext("Share", "Share".toCharArray(), "")
                val session: Session = connection.authenticate(ac)
                this@SMBViewModel.session = session
                (session.connectShare("Share") as DiskShare).use { share ->
                    share.list("").forEach{
                    }
                    share.openFile("2.txt",setOf(AccessMask.GENERIC_ALL),null,SMB2ShareAccess.ALL,SMB2CreateDisposition.FILE_CREATE,null).use {
                        it.write("sdfdfdf".toByteArray(),0L)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun shareFileList() {
//        viewModelScope.async {
//            client.connect("10.123.60.74").use { connection ->
//                val ac = AuthenticationContext("Everyone", "".toCharArray(), "");
//                val session = connection.authenticate(ac);
//                (session.connectShare("Share") as DiskShare).use { share ->
//                    share.list("").map { it.fileName }.forEach {
//                        Log.e("list", it)
//                    }
//                }
//            }
//        }
    }
}