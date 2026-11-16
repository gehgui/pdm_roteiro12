package com.iftm.livros.domain.usecase

import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.model.ReadingStatus
import com.iftm.livros.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class ObserveBooksUseCase(private val repository: BookRepository) {
    operator fun invoke(status: ReadingStatus? = null): Flow<List<Book>> =
        if (status != null) {
            repository.observeActiveByStatus(status)
        } else {
            repository.observeActive()
        }
}
