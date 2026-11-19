package com.iftm.pdm_roteiro12.domain.usecase

import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.domain.repository.BookRepository
import com.iftm.pdm_roteiro12.domain.validation.BookField
import com.iftm.pdm_roteiro12.domain.validation.BookValidator

/**
 * Normaliza, valida e persiste (insere ou atualiza) um livro.
 */
class SaveBookUseCase(
    private val repository: BookRepository,
    private val validator: BookValidator,
) {
    sealed interface Result {
        data class Success(val bookId: Long) : Result
        data class Invalid(val errors: Map<BookField, String>) : Result
    }

    suspend operator fun invoke(book: Book): Result {
        val normalized = book.normalized()
        val errors = validator.validate(normalized.title, normalized.authors)
        if (errors.isNotEmpty()) return Result.Invalid(errors)

        return if (normalized.isNew) {
            val id = repository.insert(normalized)
            Result.Success(id)
        } else {
            repository.update(normalized)
            Result.Success(normalized.id)
        }
    }

    private fun Book.normalized(): Book = copy(
        title = title.trim().replace(Regex("\\s+"), " "),
        authors = authors.trim().replace(Regex("\\s+"), " "),
        description = description.trim(),
        publishedDate = publishedDate.trim(),
        userNotes = userNotes.trim(),
        thumbnailUrl = thumbnailUrl.trim().replace("http://", "https://"),
        userRating = userRating.coerceIn(0, 5),
        pageCount = pageCount.coerceAtLeast(0),
        updatedAt = System.currentTimeMillis(),
    )
}
