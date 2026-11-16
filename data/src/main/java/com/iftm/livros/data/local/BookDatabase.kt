package com.iftm.livros.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iftm.livros.data.local.dao.BookDao
import com.iftm.livros.data.local.entity.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
