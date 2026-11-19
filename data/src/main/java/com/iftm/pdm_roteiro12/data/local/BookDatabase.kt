package com.iftm.pdm_roteiro12.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iftm.pdm_roteiro12.data.local.dao.BookDao
import com.iftm.pdm_roteiro12.data.local.entity.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
