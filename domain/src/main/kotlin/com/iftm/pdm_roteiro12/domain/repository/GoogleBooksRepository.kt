package com.iftm.pdm_roteiro12.domain.repository

import com.iftm.pdm_roteiro12.domain.model.GoogleBookSearchItem

interface GoogleBooksRepository {
    suspend fun searchBooks(query: String): List<GoogleBookSearchItem>
}
