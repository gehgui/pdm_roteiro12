package com.iftm.pdm_roteiro12.presentation.components

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
import com.iftm.pdm_roteiro12.domain.model.ReadingStatus
import com.iftm.pdm_roteiro12.ui.theme.StatusInteresseBg
import com.iftm.pdm_roteiro12.ui.theme.StatusInteresseText
import com.iftm.pdm_roteiro12.ui.theme.StatusLendoBg
import com.iftm.pdm_roteiro12.ui.theme.StatusLendoText
import com.iftm.pdm_roteiro12.ui.theme.StatusLidoBg
import com.iftm.pdm_roteiro12.ui.theme.StatusLidoText

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
