package com.iftm.pdm_roteiro12.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GoogleBooksResponseDto(
    @SerializedName("totalItems") val totalItems: Int = 0,
    @SerializedName("items") val items: List<VolumeItemDto>? = null,
)

data class VolumeItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfoDto,
)

data class VolumeInfoDto(
    @SerializedName("title") val title: String? = null,
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("authors") val authors: List<String>? = null,
    @SerializedName("publisher") val publisher: String? = null,
    @SerializedName("publishedDate") val publishedDate: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("pageCount") val pageCount: Int? = null,
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("imageLinks") val imageLinks: ImageLinksDto? = null,
    @SerializedName("language") val language: String? = null,
)

data class ImageLinksDto(
    @SerializedName("smallThumbnail") val smallThumbnail: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
)
