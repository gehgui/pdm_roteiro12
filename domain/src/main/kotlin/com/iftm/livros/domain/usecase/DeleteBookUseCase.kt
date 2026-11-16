package com.iftm.livros.domain.usecase

import com.iftm.livros.domain.repository.BookRepository

/**
 * Exclusão lógica (soft delete): o livro não é removido fisicamente do banco de dados,
 * apenas tem seu status alterado para isDeleted = true e é movido para a lixeira.
 */
class DeleteBookUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: Long) = repository.softDelete(id)
}
