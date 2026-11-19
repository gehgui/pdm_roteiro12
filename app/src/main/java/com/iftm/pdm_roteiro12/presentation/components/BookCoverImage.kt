package com.iftm.pdm_roteiro12.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun BookCoverImage(
    url: String?,
    modifier: Modifier = Modifier,
    width: Dp = 70.dp,
    height: Dp = 100.dp,
    contentDescription: String? = null,
) {
    val cornerShape = RoundedCornerShape(8.dp)

    if (url.isNullOrBlank()) {
        PlaceholderCover(
            modifier = modifier
                .size(width, height)
                .clip(cornerShape),
        )
    } else {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier
                .size(width, height)
                .clip(cornerShape),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                    )
                }
            },
            error = {
                PlaceholderCover(modifier = Modifier.fillMaxSize())
            },
        )
    }
}

@Composable
private fun PlaceholderCover(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.surfaceVariant,
                ),
            ),
        ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.MenuBook,
            contentDescription = "Capa padrão",
            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
            modifier = Modifier.size(32.dp),
        )
    }
}
