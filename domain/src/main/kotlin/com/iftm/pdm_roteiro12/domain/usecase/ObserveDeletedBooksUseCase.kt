package com.iftm.pdm_roteiro12.domain.usecase

import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class ObserveDeletedBooksUseCase(private val repository: BookRepository) {
    operator fun invoke(): Flow<List<Book>> = repository.observeDeleted()
}
