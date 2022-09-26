package com.lqs.smb

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

/**
 * @Description:     java类作用描述
 * @Author:         Linqs
 * @CreateDate:     2022/9/26 18:22
 * @UpdateUser:     更新者：
 * @UpdateDate:     2022/9/26 18:22
 * @UpdateRemark:   更新说明：
 */
@Composable
fun ConnectionPage() {
    var ip by remember { mutableStateOf("") }
    var shareName by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = ip,
            onValueChange = { ip = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("电脑ip") })
        OutlinedTextField(
            value = shareName,
            onValueChange = { shareName = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("电脑共享名") })
        OutlinedTextField(
            value = account,
            onValueChange = { account = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("账户") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("密码") })
    }
}