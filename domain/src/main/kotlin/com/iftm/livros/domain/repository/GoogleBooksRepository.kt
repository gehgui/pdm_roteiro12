package com.iftm.livros.domain.repository

import com.iftm.livros.domain.model.GoogleBookSearchItem

interface GoogleBooksRepository {
    suspend fun searchBooks(query: String): List<GoogleBookSearchItem>
}
