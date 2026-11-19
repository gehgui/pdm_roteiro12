package com.iftm.pdm_roteiro12.data.repository

import com.iftm.pdm_roteiro12.data.mapper.toDomainSearchItem
import com.iftm.pdm_roteiro12.data.remote.GoogleBooksApiService
import com.iftm.pdm_roteiro12.data.remote.OpenLibraryApiService
import com.iftm.pdm_roteiro12.domain.error.DomainException
import com.iftm.pdm_roteiro12.domain.model.GoogleBookSearchItem
import com.iftm.pdm_roteiro12.domain.repository.GoogleBooksRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GoogleBooksRepositoryImpl @Inject constructor(
    private val apiService: GoogleBooksApiService,
    private val openLibraryApiService: OpenLibraryApiService,
) : GoogleBooksRepository {

    override suspend fun searchBooks(query: String): List<GoogleBookSearchItem> {
        return try {
            val response = apiService.searchBooks(query)
            response.items?.map { it.toDomainSearchItem() } ?: emptyList()
        } catch (e: HttpException) {
            // Google Books frequentemente retorna HTTP 429 (limite de requisições sem API Key).
            // Nesse caso, fazemos fallback automático para a Open Library API (gratuita e sem chave).
            if (e.code() == 429 || e.code() == 403) {
                searchWithOpenLibraryFallback(query)
            } else {
                throw DomainException.NetworkException("Erro ao consultar livros (HTTP ${e.code()}). Tente novamente.")
            }
        } catch (e: IOException) {
            throw DomainException.NetworkException("Não foi possível conectar à internet. Verifique sua conexão.")
        } catch (e: Exception) {
            throw DomainException.NetworkException(e.message ?: "Erro inesperado ao consultar a API de livros.")
        }
    }

    private suspend fun searchWithOpenLibraryFallback(query: String): List<GoogleBookSearchItem> {
        return try {
            val response = openLibraryApiService.searchBooks(query)
            response.docs?.map { it.toDomainSearchItem() } ?: emptyList()
        } catch (e: IOException) {
            throw DomainException.NetworkException("Não foi possível conectar à internet. Verifique sua conexão.")
        } catch (e: Exception) {
            throw DomainException.NetworkException("Limite de requisições do Google Books excedido (HTTP 429). Tente novamente em instantes.")
        }
    }
}
