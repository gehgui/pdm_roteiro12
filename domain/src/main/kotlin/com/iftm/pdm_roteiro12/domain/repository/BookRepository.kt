package com.iftm.pdm_roteiro12.domain.repository

import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.domain.model.ReadingStatus
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun observeActive(): Flow<List<Book>>
    fun observeActiveByStatus(status: ReadingStatus): Flow<List<Book>>
    fun observeDeleted(): Flow<List<Book>>
    suspend fun findById(id: Long): Book?
    suspend fun insert(book: Book): Long
    suspend fun update(book: Book)
    suspend fun softDelete(id: Long)
    suspend fun restore(id: Long)
    suspend fun deletePermanently(id: Long)
}
