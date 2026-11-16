package com.iftm.livros.domain.usecase

import com.iftm.livros.domain.error.DomainException
import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.repository.BookRepository

class GetBookByIdUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: Long): Book =
        repository.findById(id) ?: throw DomainException.BookNotFound(id)
}
