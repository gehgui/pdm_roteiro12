package com.iftm.livros.data.repository

import com.iftm.livros.data.mapper.toDomainSearchItem
import com.iftm.livros.data.remote.GoogleBooksApiService
import com.iftm.livros.domain.error.DomainException
import com.iftm.livros.domain.model.GoogleBookSearchItem
import com.iftm.livros.domain.repository.GoogleBooksRepository
import java.io.IOException
import javax.inject.Inject

class GoogleBooksRepositoryImpl @Inject constructor(
    private val apiService: GoogleBooksApiService,
) : GoogleBooksRepository {

    override suspend fun searchBooks(query: String): List<GoogleBookSearchItem> {
        return try {
            val response = apiService.searchBooks(query)
            response.items?.map { it.toDomainSearchItem() } ?: emptyList()
        } catch (e: IOException) {
            throw DomainException.NetworkException("Não foi possível conectar ao Google Books. Verifique sua conexão com a internet.")
        } catch (e: Exception) {
            throw DomainException.NetworkException(e.message ?: "Erro inesperado ao consultar a API do Google Books.")
        }
    }
}
