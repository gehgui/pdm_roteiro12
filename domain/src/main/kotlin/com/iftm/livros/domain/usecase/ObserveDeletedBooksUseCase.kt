package com.iftm.livros.domain.usecase

import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class ObserveDeletedBooksUseCase(private val repository: BookRepository) {
    operator fun invoke(): Flow<List<Book>> = repository.observeDeleted()
}
