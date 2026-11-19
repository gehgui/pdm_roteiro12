package com.iftm.pdm_roteiro12.domain.usecase

import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.domain.model.ReadingStatus
import com.iftm.pdm_roteiro12.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class ObserveBooksUseCase(private val repository: BookRepository) {
    operator fun invoke(status: ReadingStatus? = null): Flow<List<Book>> =
        if (status != null) {
            repository.observeActiveByStatus(status)
        } else {
            repository.observeActive()
        }
}
