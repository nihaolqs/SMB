package com.lqs.smb

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.lqs.smb.ui.theme.SMBTheme

class MainActivity : ComponentActivity() {

    val viewModel: SMBViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(this, "Android VERSION  R OR ABOVE，HAVE MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Android VERSION  R OR ABOVE，NO MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + this.packageName)
                startActivityForResult(intent, 0)
            }
        }
        viewModel.init(this)
        setContent {
            SMBTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UploadImagePage()
                }
            }
        }
    }
}