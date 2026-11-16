package com.iftm.livros.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iftm.livros.domain.model.ReadingStatus
import com.iftm.livros.ui.theme.StatusInteresseBg
import com.iftm.livros.ui.theme.StatusInteresseText
import com.iftm.livros.ui.theme.StatusLendoBg
import com.iftm.livros.ui.theme.StatusLendoText
import com.iftm.livros.ui.theme.StatusLidoBg
import com.iftm.livros.ui.theme.StatusLidoText

@Composable
fun ReadingStatusBadge(
    status: ReadingStatus,
    modifier: Modifier = Modifier,
) {
    val (bgColor, textColor) = when (status) {
        ReadingStatus.LENDO -> StatusLendoBg to StatusLendoText
        ReadingStatus.LIDO -> StatusLidoBg to StatusLidoText
        ReadingStatus.INTERESSE -> StatusInteresseBg to StatusInteresseText
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = status.displayName,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
