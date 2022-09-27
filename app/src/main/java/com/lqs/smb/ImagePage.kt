package com.lqs.smb

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.io.File

/**
 * @Description:     java类作用描述
 * @Author:         Linqs
 * @CreateDate:     2022/9/26 15:15
 * @UpdateUser:     更新者：
 * @UpdateDate:     2022/9/26 15:15
 * @UpdateRemark:   更新说明：
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun UploadImagePage() {
    val viewModel: SMBViewModel = viewModel()
    val shareImages by viewModel.shareImages.observeAsState()
    val phoneImages by viewModel.phoneImages.observeAsState(emptyList())
    var bigPicture by remember { mutableStateOf("" to "") }
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(180.dp),
    ) {
        items(phoneImages) { pair ->
            ImageCard(
                image = pair.second,
                modifier = Modifier.size(180.dp),
                isUpload = shareImages?.contains(pair.first) == true,
                onClick = { viewModel.uploadImage(pair.first, pair.second) },
                onShowBigPicture = { bigPicture = pair })
        }
    }
    if (bigPicture.first.isNotEmpty()) {

        val pagerState = rememberPagerState(phoneImages.indexOf(bigPicture))
        HorizontalPager(
            count = phoneImages.size,
            state = pagerState,
            itemSpacing = 15.dp,
            modifier = Modifier.fillMaxSize().background(
                Color.Black
            )
        ) { page ->
            val pair = phoneImages[page]
            Column(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = File(pair.second),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black)
                        .clickable {
                            bigPicture = "" to ""
                        }
                )
                if (shareImages?.contains(pair.first) != true) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                            .background(MaterialTheme.colors.primary).clickable {
                                viewModel.uploadImage(pair.first, pair.second)
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text("上传")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCard(
    image: String,
    modifier: Modifier,
    isUpload: Boolean,
    onClick: () -> Unit = {},
    onShowBigPicture: () -> Unit = {}
) {
    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier) {
        AsyncImage(
            model = File(image),
            contentDescription = null,
            modifier = Modifier.padding(3.dp).fillMaxSize().background(Color.Black).clickable {
                onShowBigPicture.invoke()
            },
            contentScale = ContentScale.Crop,
        )
        if (!isUpload) {
            Box(
                modifier = Modifier.fillMaxWidth().height(40.dp).padding(horizontal = 3.dp)
                    .background(MaterialTheme.colors.primary).clickable {
                        onClick.invoke()
                    }, contentAlignment = Alignment.Center
            ) {
                Text("上传")
            }
        }
    }
}