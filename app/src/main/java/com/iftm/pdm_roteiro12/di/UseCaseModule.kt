package com.iftm.pdm_roteiro12.di

import com.iftm.pdm_roteiro12.domain.repository.BookRepository
import com.iftm.pdm_roteiro12.domain.repository.GoogleBooksRepository
import com.iftm.pdm_roteiro12.domain.usecase.DeleteBookUseCase
import com.iftm.pdm_roteiro12.domain.usecase.GetBookByIdUseCase
import com.iftm.pdm_roteiro12.domain.usecase.ObserveBooksUseCase
import com.iftm.pdm_roteiro12.domain.usecase.ObserveDeletedBooksUseCase
import com.iftm.pdm_roteiro12.domain.usecase.PermanentDeleteBookUseCase
import com.iftm.pdm_roteiro12.domain.usecase.RestoreBookUseCase
import com.iftm.pdm_roteiro12.domain.usecase.SaveBookUseCase
import com.iftm.pdm_roteiro12.domain.usecase.SearchGoogleBooksUseCase
import com.iftm.pdm_roteiro12.domain.validation.BookValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Os casos de uso são classes puras de domínio Kotlin;
 * este módulo os provê para os ViewModels do Hilt.
 */
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideBookValidator(): BookValidator = BookValidator()

    @Provides
    fun provideObserveBooksUseCase(repo: BookRepository): ObserveBooksUseCase =
        ObserveBooksUseCase(repo)

    @Provides
    fun provideObserveDeletedBooksUseCase(repo: BookRepository): ObserveDeletedBooksUseCase =
        ObserveDeletedBooksUseCase(repo)

    @Provides
    fun provideGetBookByIdUseCase(repo: BookRepository): GetBookByIdUseCase =
        GetBookByIdUseCase(repo)

    @Provides
    fun provideSaveBookUseCase(
        repo: BookRepository,
        validator: BookValidator,
    ): SaveBookUseCase = SaveBookUseCase(repo, validator)

    @Provides
    fun provideDeleteBookUseCase(repo: BookRepository): DeleteBookUseCase =
        DeleteBookUseCase(repo)

    @Provides
    fun provideRestoreBookUseCase(repo: BookRepository): RestoreBookUseCase =
        RestoreBookUseCase(repo)

    @Provides
    fun providePermanentDeleteBookUseCase(repo: BookRepository): PermanentDeleteBookUseCase =
        PermanentDeleteBookUseCase(repo)

    @Provides
    fun provideSearchGoogleBooksUseCase(repo: GoogleBooksRepository): SearchGoogleBooksUseCase =
        SearchGoogleBooksUseCase(repo)
}
