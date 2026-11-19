package com.iftm.pdm_roteiro12.domain

import com.iftm.pdm_roteiro12.domain.model.GoogleBookSearchItem
import com.iftm.pdm_roteiro12.domain.repository.GoogleBooksRepository

class FakeGoogleBooksRepository : GoogleBooksRepository {
    var searchResults: List<GoogleBookSearchItem> = listOf(
        GoogleBookSearchItem(
            id = "google123",
            title = "Clean Code",
            authors = listOf("Robert C. Martin"),
            description = "Even bad code can function...",
            thumbnailUrl = "http://example.com/cover.jpg",
            pageCount = 464,
            publishedDate = "2008",
        )
    )

    var shouldThrow = false

    override suspend fun searchBooks(query: String): List<GoogleBookSearchItem> {
        if (shouldThrow) throw RuntimeException("API error")
        return searchResults.filter { it.title.contains(query, ignoreCase = true) }
    }
}
