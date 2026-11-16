package com.iftm.livros.domain

import com.iftm.livros.domain.error.DomainException
import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.model.ReadingStatus
import com.iftm.livros.domain.usecase.DeleteBookUseCase
import com.iftm.livros.domain.usecase.GetBookByIdUseCase
import com.iftm.livros.domain.usecase.ObserveBooksUseCase
import com.iftm.livros.domain.usecase.ObserveDeletedBooksUseCase
import com.iftm.livros.domain.usecase.PermanentDeleteBookUseCase
import com.iftm.livros.domain.usecase.RestoreBookUseCase
import com.iftm.livros.domain.usecase.SaveBookUseCase
import com.iftm.livros.domain.usecase.SearchGoogleBooksUseCase
import com.iftm.livros.domain.validation.BookField
import com.iftm.livros.domain.validation.BookValidator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BookUseCasesTest {

    private val bookRepository = FakeBookRepository()
    private val googleBooksRepository = FakeGoogleBooksRepository()
    private val validator = BookValidator()

    private val saveBook = SaveBookUseCase(bookRepository, validator)
    private val deleteBook = DeleteBookUseCase(bookRepository)
    private val restoreBook = RestoreBookUseCase(bookRepository)
    private val permanentDeleteBook = PermanentDeleteBookUseCase(bookRepository)
    private val observeBooks = ObserveBooksUseCase(bookRepository)
    private val observeDeletedBooks = ObserveDeletedBooksUseCase(bookRepository)
    private val getBookById = GetBookByIdUseCase(bookRepository)
    private val searchGoogleBooks = SearchGoogleBooksUseCase(googleBooksRepository)

    private val sampleBook = Book(
        title = "  O Senhor dos Anéis  ",
        authors = " J. R. R. Tolkien ",
        description = "Uma grande fantasia épica",
        thumbnailUrl = "http://books.google.com/cover.jpg",
        pageCount = 1200,
        publishedDate = "1954",
        readingStatus = ReadingStatus.LENDO,
        userRating = 5,
        userNotes = "Excelente leitura!",
    )

    @Test
    fun `salva livro valido com sucesso e normaliza campos`() = runTest {
        val result = saveBook(sampleBook)

        assertTrue(result is SaveBookUseCase.Result.Success)
        val bookId = (result as SaveBookUseCase.Result.Success).bookId
        val saved = getBookById(bookId)

        assertEquals("O Senhor dos Anéis", saved.title)
        assertEquals("J. R. R. Tolkien", saved.authors)
        assertEquals("https://books.google.com/cover.jpg", saved.thumbnailUrl)
        assertEquals(ReadingStatus.LENDO, saved.readingStatus)
        assertEquals(5, saved.userRating)
    }

    @Test
    fun `rejeita livro sem titulo ou sem autores`() = runTest {
        val invalidResult = saveBook(sampleBook.copy(title = "   ", authors = ""))

        assertTrue(invalidResult is SaveBookUseCase.Result.Invalid)
        val errors = (invalidResult as SaveBookUseCase.Result.Invalid).errors
        assertTrue(BookField.TITLE in errors)
        assertTrue(BookField.AUTHORS in errors)
        assertTrue(bookRepository.books.value.isEmpty())
    }

    @Test
    fun `soft delete move livro para a lixeira sem apaga-lo do banco`() = runTest {
        val id = (saveBook(sampleBook) as SaveBookUseCase.Result.Success).bookId

        // Livro está ativo
        assertEquals(1, observeBooks().first().size)
        assertEquals(0, observeDeletedBooks().first().size)

        // Executa soft delete
        deleteBook(id)

        // Agora não está mais na lista de ativos, mas está na lixeira
        assertTrue(observeBooks().first().isEmpty())
        val deleted = observeDeletedBooks().first()
        assertEquals(1, deleted.size)
        assertEquals("O Senhor dos Anéis", deleted.first().title)
        assertNotNull(deleted.first().deletedAt)

        // Banco de dados ainda mantém o registro
        assertEquals(1, bookRepository.books.value.size)

        // Restaura o livro
        restoreBook(id)
        assertEquals(1, observeBooks().first().size)
        assertTrue(observeDeletedBooks().first().isEmpty())
    }

    @Test
    fun `exclusao permanente remove o livro definitivamente`() = runTest {
        val id = (saveBook(sampleBook) as SaveBookUseCase.Result.Success).bookId
        deleteBook(id)

        permanentDeleteBook(id)

        assertTrue(observeBooks().first().isEmpty())
        assertTrue(observeDeletedBooks().first().isEmpty())
        assertTrue(bookRepository.books.value.isEmpty())
    }

    @Test
    fun `filtrar livros por status de leitura`() = runTest {
        saveBook(sampleBook.copy(title = "Livro 1", readingStatus = ReadingStatus.LENDO))
        saveBook(sampleBook.copy(title = "Livro 2", readingStatus = ReadingStatus.LIDO))
        saveBook(sampleBook.copy(title = "Livro 3", readingStatus = ReadingStatus.INTERESSE))

        val lendo = observeBooks(ReadingStatus.LENDO).first()
        val lido = observeBooks(ReadingStatus.LIDO).first()
        val interesse = observeBooks(ReadingStatus.INTERESSE).first()

        assertEquals(1, lendo.size)
        assertEquals("Livro 1", lendo.first().title)
        assertEquals(1, lido.size)
        assertEquals("Livro 2", lido.first().title)
        assertEquals(1, interesse.size)
        assertEquals("Livro 3", interesse.first().title)
    }

    @Test
    fun `busca de livros na API rejeita termo em branco`() = runTest {
        val result = searchGoogleBooks("   ")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is DomainException.EmptySearchQuery)
    }

    @Test
    fun `busca de livros na API retorna resultados correspondentes`() = runTest {
        val result = searchGoogleBooks("Clean Code")

        assertTrue(result.isSuccess)
        val books = result.getOrNull()!!
        assertEquals(1, books.size)
        assertEquals("Clean Code", books.first().title)
        assertEquals("Robert C. Martin", books.first().authorsFormatted)
    }
}
