package com.iftm.livros.domain.usecase

import com.iftm.livros.domain.repository.BookRepository

/**
 * Remove fisicamente o livro do banco de dados (exclusão permanente a partir da lixeira).
 */
class PermanentDeleteBookUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: Long) = repository.deletePermanently(id)
}
