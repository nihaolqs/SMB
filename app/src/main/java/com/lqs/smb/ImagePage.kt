package com.lqs.smb

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

/**
 * @Description:     java类作用描述
 * @Author:         Linqs
 * @CreateDate:     2022/9/26 15:15
 * @UpdateUser:     更新者：
 * @UpdateDate:     2022/9/26 15:15
 * @UpdateRemark:   更新说明：
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UploadImagePage() {
    val viewModel: SMBViewModel = viewModel()
    val shareImages by viewModel.shareImages.observeAsState()
    val phoneImages by viewModel.phoneImages.observeAsState(emptyList())
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(180.dp),
    ) {
        items(phoneImages) { (name, path) ->
            ImageCard(
                image = path,
                modifier = Modifier.size(180.dp),
                shareImages?.contains(name) == true
            ) {
                viewModel.uploadImage(name, path)

            }
        }
    }
}

@Composable
fun ImageCard(image: String, modifier: Modifier, isUpload: Boolean, onClick: () -> Unit = {}) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        AsyncImage(
            model = File(image),
            contentDescription = null,
            modifier = Modifier.padding(3.dp).fillMaxSize().background(Color.Black),
            contentScale = ContentScale.Crop,
        )

        if (!isUpload) {
            AsyncImage(
                model = R.drawable.send,
                contentDescription = null,
                modifier = Modifier.size(50.dp).clickable {
                    onClick.invoke()
                })
        }
    }
}