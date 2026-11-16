package com.iftm.livros.domain.error

sealed class DomainException(message: String) : Exception(message) {
    data class BookNotFound(val id: Long) : DomainException("Livro com ID $id não encontrado.")
    data class ValidationException(val errors: Map<String, String>) : DomainException("Dados do livro inválidos.")
    data class EmptySearchQuery(override val message: String = "Digite um termo para pesquisar.") : DomainException(message)
    data class NetworkException(override val message: String = "Falha ao se comunicar com a API do Google Books.") : DomainException(message)
}
