package com.iftm.livros.presentation.books.list

import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.model.ReadingStatus

data class BookListUiState(
    val books: List<Book> = emptyList(),
    val selectedStatus: ReadingStatus? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = true,
) {
    val displayedBooks: List<Book>
        get() = books.filter { book ->
            val matchesStatus = selectedStatus == null || book.readingStatus == selectedStatus
            val matchesQuery = searchQuery.isBlank() ||
                book.title.contains(searchQuery, ignoreCase = true) ||
                book.authors.contains(searchQuery, ignoreCase = true)
            matchesStatus && matchesQuery
        }
}
