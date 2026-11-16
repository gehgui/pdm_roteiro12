package com.iftm.livros.presentation.books.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.model.GoogleBookSearchItem
import com.iftm.livros.domain.model.ReadingStatus
import com.iftm.livros.domain.usecase.GetBookByIdUseCase
import com.iftm.livros.domain.usecase.SaveBookUseCase
import com.iftm.livros.domain.usecase.SearchGoogleBooksUseCase
import com.iftm.livros.domain.validation.BookField
import com.iftm.livros.presentation.navigation.BookFormRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BookFormEvent {
    data class Saved(val message: String) : BookFormEvent
    data class Error(val message: String) : BookFormEvent
}

@HiltViewModel
class BookFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookById: GetBookByIdUseCase,
    private val saveBook: SaveBookUseCase,
    private val searchGoogleBooks: SearchGoogleBooksUseCase,
) : ViewModel() {

    val bookId: Long = savedStateHandle.toRoute<BookFormRoute>().bookId

    private val _uiState = MutableStateFlow(BookFormUiState())
    val uiState: StateFlow<BookFormUiState> = _uiState.asStateFlow()

    private val _events = Channel<BookFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        if (bookId != Book.NEW_BOOK_ID) {
            loadBook()
        }
    }

    private fun loadBook() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching { getBookById(bookId) }
                .onSuccess { book ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = book.title,
                            authors = book.authors,
                            description = book.description,
                            thumbnailUrl = book.thumbnailUrl,
                            pageCount = book.pageCount,
                            publishedDate = book.publishedDate,
                            readingStatus = book.readingStatus,
                            userRating = book.userRating,
                            userNotes = book.userNotes,
                            googleBookId = book.googleBookId,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(BookFormEvent.Error(e.message ?: "Erro ao carregar livro."))
                }
        }
    }

    fun onTitleChange(value: String) = editField(BookField.TITLE) { copy(title = value) }
    fun onAuthorsChange(value: String) = editField(BookField.AUTHORS) { copy(authors = value) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }
    fun onThumbnailUrlChange(value: String) = _uiState.update { it.copy(thumbnailUrl = value) }
    fun onPageCountChange(value: String) {
        val pages = value.filter(Char::isDigit).toIntOrNull() ?: 0
        _uiState.update { it.copy(pageCount = pages) }
    }
    fun onPublishedDateChange(value: String) = _uiState.update { it.copy(publishedDate = value) }
    fun onReadingStatusChange(status: ReadingStatus) = _uiState.update { it.copy(readingStatus = status) }
    fun onRatingChange(rating: Int) = _uiState.update { it.copy(userRating = rating) }
    fun onNotesChange(notes: String) = _uiState.update { it.copy(userNotes = notes) }

    fun onApiQueryChange(query: String) = _uiState.update { it.copy(apiQuery = query) }

    fun openSearchDialog() = _uiState.update { it.copy(isSearchDialogVisible = true) }
    fun closeSearchDialog() = _uiState.update { it.copy(isSearchDialogVisible = false) }

    fun searchGoogleBooks() {
        val query = _uiState.value.apiQuery
        if (query.isBlank()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(isSearchingApi = true, apiFeedbackMessage = null) }
            searchGoogleBooks(query)
                .onSuccess { results ->
                    _uiState.update {
                        it.copy(
                            isSearchingApi = false,
                            apiResults = results,
                            apiFeedbackMessage = if (results.isEmpty()) "Nenhum livro encontrado para \"$query\"." else null,
                            isSearchDialogVisible = true,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSearchingApi = false,
                            apiFeedbackMessage = e.message ?: "Erro na consulta à API.",
                        )
                    }
                }
        }
    }

    fun onSelectApiBook(item: GoogleBookSearchItem) {
        _uiState.update {
            it.copy(
                title = item.title,
                authors = item.authorsFormatted,
                description = item.description,
                thumbnailUrl = item.thumbnailUrl,
                pageCount = item.pageCount,
                publishedDate = item.publishedDate,
                googleBookId = item.id,
                apiFeedbackMessage = "Dados preenchidos via Google Books API!",
                isSearchDialogVisible = false,
                errors = it.errors - setOf(BookField.TITLE, BookField.AUTHORS),
            )
        }
    }

    fun onSave() {
        val state = _uiState.value
        if (state.isSaving) return

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val book = Book(
                id = bookId,
                googleBookId = state.googleBookId,
                title = state.title,
                authors = state.authors,
                description = state.description,
                thumbnailUrl = state.thumbnailUrl,
                pageCount = state.pageCount,
                publishedDate = state.publishedDate,
                readingStatus = state.readingStatus,
                userRating = state.userRating,
                userNotes = state.userNotes,
            )

            runCatching { saveBook(book) }
                .onSuccess { result ->
                    when (result) {
                        is SaveBookUseCase.Result.Success -> {
                            _uiState.update { it.copy(isSaving = false) }
                            val msg = if (book.isNew) "Livro cadastrado com sucesso!" else "Livro atualizado com sucesso!"
                            _events.send(BookFormEvent.Saved(msg))
                        }
                        is SaveBookUseCase.Result.Invalid -> {
                            _uiState.update { it.copy(isSaving = false, errors = result.errors) }
                        }
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false) }
                    _events.send(BookFormEvent.Error(e.message ?: "Erro ao salvar livro."))
                }
        }
    }

    private inline fun editField(field: BookField?, crossinline block: BookFormUiState.() -> BookFormUiState) {
        _uiState.update { current ->
            val updated = current.block()
            if (field != null && field in updated.errors) updated.copy(errors = updated.errors - field) else updated
        }
    }
}
