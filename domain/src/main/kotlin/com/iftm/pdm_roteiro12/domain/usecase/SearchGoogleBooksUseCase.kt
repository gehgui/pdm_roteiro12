package com.iftm.pdm_roteiro12.domain.usecase

import com.iftm.pdm_roteiro12.domain.error.DomainException
import com.iftm.pdm_roteiro12.domain.model.GoogleBookSearchItem
import com.iftm.pdm_roteiro12.domain.repository.GoogleBooksRepository

/**
 * Consulta a Google Books API buscando livros pelo termo informado.
 */
class SearchGoogleBooksUseCase(private val repository: GoogleBooksRepository) {
    suspend operator fun invoke(query: String): Result<List<GoogleBookSearchItem>> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return Result.failure(DomainException.EmptySearchQuery())
        }
        return try {
            val items = repository.searchBooks(trimmed)
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(
                e as? DomainException ?: DomainException.NetworkException(
                    e.message ?: "Erro ao buscar livros na API do Google Books."
                )
            )
        }
    }
}
