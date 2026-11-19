package com.iftm.pdm_roteiro12.data.di

import com.iftm.pdm_roteiro12.data.remote.GoogleBooksApiService
import com.iftm.pdm_roteiro12.data.remote.OpenLibraryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(GoogleBooksApiService.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideGoogleBooksApiService(retrofit: Retrofit): GoogleBooksApiService =
        retrofit.create(GoogleBooksApiService::class.java)

    @Provides
    @Singleton
    fun provideOpenLibraryApiService(client: OkHttpClient): OpenLibraryApiService =
        Retrofit.Builder()
            .baseUrl(OpenLibraryApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenLibraryApiService::class.java)
}
