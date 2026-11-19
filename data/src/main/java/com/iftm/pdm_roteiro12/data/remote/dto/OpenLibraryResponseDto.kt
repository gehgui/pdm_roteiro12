package com.iftm.pdm_roteiro12.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpenLibraryResponseDto(
    @SerializedName("docs") val docs: List<OpenLibraryDocDto>? = null,
)

data class OpenLibraryDocDto(
    @SerializedName("key") val key: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("author_name") val authorName: List<String>? = null,
    @SerializedName("first_publish_year") val firstPublishYear: Int? = null,
    @SerializedName("number_of_pages_median") val numberOfPagesMedian: Int? = null,
    @SerializedName("cover_i") val coverId: Long? = null,
    @SerializedName("first_sentence") val firstSentence: List<String>? = null,
)
