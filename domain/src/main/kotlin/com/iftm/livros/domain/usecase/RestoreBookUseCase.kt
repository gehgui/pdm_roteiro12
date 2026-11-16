package com.iftm.livros.domain.usecase

import com.iftm.livros.domain.repository.BookRepository

/**
 * Restaura um livro que foi excluído logicamente (soft delete),
 * retornando-o para a lista principal ativa.
 */
class RestoreBookUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: Long) = repository.restore(id)
}
