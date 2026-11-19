package com.iftm.pdm_roteiro12.presentation.books.form

import com.iftm.pdm_roteiro12.domain.model.GoogleBookSearchItem
import com.iftm.pdm_roteiro12.domain.model.ReadingStatus
import com.iftm.pdm_roteiro12.domain.validation.BookField

data class BookFormUiState(
    // Campos do livro
    val title: String = "",
    val authors: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val pageCount: Int = 0,
    val publishedDate: String = "",
    val readingStatus: ReadingStatus = ReadingStatus.INTERESSE,
    val userRating: Int = 0,
    val userNotes: String = "",
    val googleBookId: String = "",

    // Estados de carregamento e formulário
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errors: Map<BookField, String> = emptyMap(),

    // Busca na Google Books API
    val apiQuery: String = "",
    val isSearchingApi: Boolean = false,
    val apiResults: List<GoogleBookSearchItem> = emptyList(),
    val apiFeedbackMessage: String? = null,
    val isApiFeedbackError: Boolean = false,
    val isSearchDialogVisible: Boolean = false,
)
