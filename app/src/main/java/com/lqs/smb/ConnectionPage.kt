package com.lqs.smb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

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
    val viewModel: SMBViewModel = viewModel()
    var hostName by remember { mutableStateOf(viewModel.hostName) }
    var shareName by remember { mutableStateOf(viewModel.shareName) }
    var account by remember { mutableStateOf(viewModel.account) }
    var password by remember { mutableStateOf(viewModel.password) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier.padding(15.dp).width(300.dp),
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = hostName,
                onValueChange = { hostName = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("hostName") })
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = shareName,
                onValueChange = { shareName = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("共享名") })
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = account,
                onValueChange = { account = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("账户") })
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("密码") })
            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                viewModel.hostName = hostName
                viewModel.shareName = shareName
                viewModel.account = account
                viewModel.password = password
                viewModel.initShareImage()
            }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("连接共享")
            }
        }
    }
}