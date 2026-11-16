package com.iftm.livros.domain.model

/**
 * Entidade central do domínio para um Livro.
 * Totalmente isolada de detalhes de persistência ou interface.
 *
 * @property id identificador local no banco de dados; 0L indica livro ainda não salvo.
 * @property googleBookId id retornado pela Google Books API (quando obtido via busca).
 * @property title título do livro.
 * @property authors autores do livro formatados como string (ex: "J.K. Rowling, John Tiffany").
 * @property description descrição / sinopse do livro.
 * @property thumbnailUrl URL da imagem de capa (Google Books).
 * @property pageCount quantidade de páginas do livro.
 * @property publishedDate data ou ano de publicação.
 * @property readingStatus status de leitura ("Lendo", "Lido", "Interesse").
 * @property userRating avaliação do usuário (0 a 5 estrelas).
 * @property userNotes anotações ou resenha pessoal do usuário.
 * @property isDeleted flag que indica exclusão lógica (soft delete).
 * @property createdAt timestamp de criação.
 * @property updatedAt timestamp de última atualização.
 * @property deletedAt timestamp de quando foi movido para a lixeira (se excluído).
 */
data class Book(
    val id: Long = NEW_BOOK_ID,
    val googleBookId: String = "",
    val title: String,
    val authors: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val pageCount: Int = 0,
    val publishedDate: String = "",
    val readingStatus: ReadingStatus = ReadingStatus.INTERESSE,
    val userRating: Int = 0,
    val userNotes: String = "",
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null,
) {
    val isNew: Boolean get() = id == NEW_BOOK_ID

    companion object {
        const val NEW_BOOK_ID = 0L
    }
}
