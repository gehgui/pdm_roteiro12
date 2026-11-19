package com.iftm.pdm_roteiro12.domain.usecase

import com.iftm.pdm_roteiro12.domain.error.DomainException
import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.domain.repository.BookRepository

class GetBookByIdUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: Long): Book =
        repository.findById(id) ?: throw DomainException.BookNotFound(id)
}
