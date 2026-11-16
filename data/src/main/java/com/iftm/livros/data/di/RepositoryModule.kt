package com.iftm.livros.data.di

import com.iftm.livros.data.repository.BookRepositoryImpl
import com.iftm.livros.data.repository.GoogleBooksRepositoryImpl
import com.iftm.livros.domain.repository.BookRepository
import com.iftm.livros.domain.repository.GoogleBooksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

    @Binds
    @Singleton
    abstract fun bindGoogleBooksRepository(impl: GoogleBooksRepositoryImpl): GoogleBooksRepository
}
