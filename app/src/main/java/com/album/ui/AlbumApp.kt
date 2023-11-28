package com.album.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import com.example.album.R


@Composable
fun MainScreen(viewModel: AlbumViewModel = viewModel(),
               modifier: Modifier = Modifier
) {
    val images by viewModel.images.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isInitialComposition by rememberSaveable { mutableStateOf(true) }

    val state = rememberLazyStaggeredGridState()
    val isEndReached = !state.canScrollForward
    LaunchedEffect(isEndReached) {
        if (isEndReached) viewModel.update()
    }

    if (isInitialComposition) {
        viewModel.update()
        isInitialComposition = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    }
    else if (errorMessage.isNotEmpty()) {
        ErrorScreen(msg = errorMessage) {viewModel.update()}
    }
    else {
        LazyVerticalStaggeredGrid(
            state = state,
            columns = StaggeredGridCells.Adaptive(180.dp),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier,
        ) {
            items(images) { image ->
                AlbumImageComponent(
                    imageUrl = image.url,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun ErrorScreen(msg: String, onRetry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = msg)
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top=20.dp)
        ) {
            Text(text = "Retry")
        }
    }
}


@Composable
fun AlbumImageComponent(imageUrl: String, modifier: Modifier = Modifier){
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = null) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is AsyncImagePainter.State.Error -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(stringResource(id = R.string.image_loading_error))
                }
            }
            else -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = modifier
                )
            }
        }
    }
}
