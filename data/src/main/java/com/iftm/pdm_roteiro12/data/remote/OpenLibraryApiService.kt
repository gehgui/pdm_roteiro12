package com.iftm.pdm_roteiro12.data.remote

import com.iftm.pdm_roteiro12.data.remote.dto.OpenLibraryResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OpenLibraryApiService {

    @Headers("User-Agent: pdm_roteiro12/1.0 (Android)")
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,number_of_pages_median,cover_i,first_sentence",
    ): OpenLibraryResponseDto

    companion object {
        const val BASE_URL = "https://openlibrary.org/"
    }
}
