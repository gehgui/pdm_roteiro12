package com.iftm.pdm_roteiro12.data.di

import android.content.Context
import androidx.room.Room
import com.iftm.pdm_roteiro12.data.local.BookDatabase
import com.iftm.pdm_roteiro12.data.local.dao.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase =
        Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "livros.db",
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideBookDao(database: BookDatabase): BookDao = database.bookDao()
}
