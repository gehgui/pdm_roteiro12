package com.iftm.livros.data.repository

import com.iftm.livros.data.local.dao.BookDao
import com.iftm.livros.data.mapper.toDomain
import com.iftm.livros.data.mapper.toEntity
import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.model.ReadingStatus
import com.iftm.livros.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
) : BookRepository {

    override fun observeActive(): Flow<List<Book>> =
        bookDao.observeActive().map { list -> list.map { it.toDomain() } }

    override fun observeActiveByStatus(status: ReadingStatus): Flow<List<Book>> =
        bookDao.observeActiveByStatus(status.name).map { list -> list.map { it.toDomain() } }

    override fun observeDeleted(): Flow<List<Book>> =
        bookDao.observeDeleted().map { list -> list.map { it.toDomain() } }

    override suspend fun findById(id: Long): Book? =
        bookDao.findById(id)?.toDomain()

    override suspend fun insert(book: Book): Long =
        bookDao.insert(book.toEntity())

    override suspend fun update(book: Book) =
        bookDao.update(book.toEntity())

    override suspend fun softDelete(id: Long) =
        bookDao.softDelete(id, System.currentTimeMillis())

    override suspend fun restore(id: Long) =
        bookDao.restore(id, System.currentTimeMillis())

    override suspend fun deletePermanently(id: Long) =
        bookDao.deletePermanently(id)
}
