package com.iftm.pdm_roteiro12.data.di

import com.iftm.pdm_roteiro12.data.repository.BookRepositoryImpl
import com.iftm.pdm_roteiro12.data.repository.GoogleBooksRepositoryImpl
import com.iftm.pdm_roteiro12.domain.repository.BookRepository
import com.iftm.pdm_roteiro12.domain.repository.GoogleBooksRepository
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
