package com.todokanai.filemanager.compose.presets.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageHolder(
    modifier: Modifier,
    isAsyncImage:Boolean,
    icon: ImageBitmap,
    data: Any?
){
    val context = LocalContext.current
    if(isAsyncImage) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data)
                .crossfade(true)
                .build(),
            null,
            modifier = modifier,
            placeholder = BitmapPainter(icon)
        )                                           // AsyncImage에 해당될 경우
    } else {
        Image(
            bitmap = icon,
            null,
            modifier = modifier
        )
    }
}