package com.iftm.livros.data.mapper

import com.iftm.livros.data.remote.dto.VolumeItemDto
import com.iftm.livros.domain.model.GoogleBookSearchItem

fun VolumeItemDto.toDomainSearchItem(): GoogleBookSearchItem {
    val info = volumeInfo
    val thumb = info.imageLinks?.thumbnail ?: info.imageLinks?.smallThumbnail ?: ""
    val secureThumb = thumb.replace("http://", "https://")
    return GoogleBookSearchItem(
        id = id,
        title = info.title ?: "Sem título",
        authors = info.authors ?: emptyList(),
        description = info.description ?: "",
        thumbnailUrl = secureThumb,
        pageCount = info.pageCount ?: 0,
        publishedDate = info.publishedDate ?: "",
    )
}
