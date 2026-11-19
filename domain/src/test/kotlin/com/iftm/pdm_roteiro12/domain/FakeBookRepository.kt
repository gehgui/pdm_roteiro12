package com.iftm.pdm_roteiro12.domain

import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.domain.model.ReadingStatus
import com.iftm.pdm_roteiro12.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeBookRepository : BookRepository {

    private var nextId = 1L
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books = _books.asStateFlow()

    override fun observeActive(): Flow<List<Book>> =
        _books.map { list -> list.filterNot { it.isDeleted } }

    override fun observeActiveByStatus(status: ReadingStatus): Flow<List<Book>> =
        _books.map { list -> list.filter { !it.isDeleted && it.readingStatus == status } }

    override fun observeDeleted(): Flow<List<Book>> =
        _books.map { list -> list.filter { it.isDeleted } }

    override suspend fun findById(id: Long): Book? =
        _books.value.firstOrNull { it.id == id }

    override suspend fun insert(book: Book): Long {
        val id = nextId++
        val newBook = book.copy(id = id)
        _books.update { it + newBook }
        return id
    }

    override suspend fun update(book: Book) {
        _books.update { list -> list.map { if (it.id == book.id) book else it } }
    }

    override suspend fun softDelete(id: Long) {
        _books.update { list ->
            list.map {
                if (it.id == id) it.copy(isDeleted = true, deletedAt = System.currentTimeMillis()) else it
            }
        }
    }

    override suspend fun restore(id: Long) {
        _books.update { list ->
            list.map {
                if (it.id == id) it.copy(isDeleted = false, deletedAt = null) else it
            }
        }
    }

    override suspend fun deletePermanently(id: Long) {
        _books.update { list -> list.filterNot { it.id == id } }
    }
}
