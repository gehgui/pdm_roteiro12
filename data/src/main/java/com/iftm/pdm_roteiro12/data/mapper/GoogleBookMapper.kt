package com.iftm.pdm_roteiro12.data.mapper

import com.iftm.pdm_roteiro12.data.remote.dto.OpenLibraryDocDto
import com.iftm.pdm_roteiro12.data.remote.dto.VolumeItemDto
import com.iftm.pdm_roteiro12.domain.model.GoogleBookSearchItem

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

fun OpenLibraryDocDto.toDomainSearchItem(): GoogleBookSearchItem {
    val coverUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" } ?: ""
    return GoogleBookSearchItem(
        id = key ?: "${title.orEmpty()}_${firstPublishYear ?: 0}",
        title = title ?: "Sem título",
        authors = authorName ?: emptyList(),
        description = firstSentence?.firstOrNull().orEmpty(),
        thumbnailUrl = coverUrl,
        pageCount = numberOfPagesMedian ?: 0,
        publishedDate = firstPublishYear?.toString().orEmpty(),
    )
}
