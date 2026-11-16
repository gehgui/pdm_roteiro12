package com.iftm.livros.data.remote

import com.iftm.livros.data.remote.dto.GoogleBooksResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApiService {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("printType") printType: String = "books",
    ): GoogleBooksResponseDto

    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }
}
