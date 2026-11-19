package com.iftm.pdm_roteiro12.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 20.dp,
    starColor: Color = Color(0xFFFFB300),
    onRatingChanged: ((Int) -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarOutline
            val clickableModifier = if (onRatingChanged != null) {
                Modifier.clickable {
                    // Clicar na mesma estrela zera a nota; caso contrário define a nota
                    onRatingChanged(if (rating == i) 0 else i)
                }
            } else {
                Modifier
            }

            Icon(
                imageVector = icon,
                contentDescription = "$i estrelas",
                tint = if (isSelected) starColor else Color.Gray.copy(alpha = 0.5f),
                modifier = clickableModifier.size(starSize),
            )
        }
    }
}
