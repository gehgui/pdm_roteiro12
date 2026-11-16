package com.iftm.livros.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object BookListRoute

/**
 * [bookId] igual a 0L indica cadastro de um novo livro.
 * Caso contrário, indica edição do livro existente.
 */
@Serializable
data class BookFormRoute(val bookId: Long = 0L)

@Serializable
data object TrashRoute
