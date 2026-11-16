package com.iftm.livros.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.iftm.livros.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books WHERE is_deleted = 0 ORDER BY updated_at DESC")
    fun observeActive(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE is_deleted = 0 AND reading_status = :status ORDER BY updated_at DESC")
    fun observeActiveByStatus(status: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE is_deleted = 1 ORDER BY deleted_at DESC")
    fun observeDeleted(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun findById(id: Long): BookEntity?

    @Insert
    suspend fun insert(entity: BookEntity): Long

    @Update
    suspend fun update(entity: BookEntity)

    @Query("UPDATE books SET is_deleted = 1, deleted_at = :timestamp, updated_at = :timestamp WHERE id = :id")
    suspend fun softDelete(id: Long, timestamp: Long)

    @Query("UPDATE books SET is_deleted = 0, deleted_at = NULL, updated_at = :timestamp WHERE id = :id")
    suspend fun restore(id: Long, timestamp: Long)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deletePermanently(id: Long)
}
