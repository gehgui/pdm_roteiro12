package com.iftm.pdm_roteiro12.domain.model

/**
 * Representa um livro encontrado na API do Google Books durante a busca.
 */
data class GoogleBookSearchItem(
    val id: String,
    val title: String,
    val authors: List<String> = emptyList(),
    val description: String = "",
    val thumbnailUrl: String = "",
    val pageCount: Int = 0,
    val publishedDate: String = "",
) {
    val authorsFormatted: String get() = if (authors.isEmpty()) "Autor desconhecido" else authors.joinToString(", ")
}
