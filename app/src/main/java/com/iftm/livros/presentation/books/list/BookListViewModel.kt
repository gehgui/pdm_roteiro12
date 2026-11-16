package com.iftm.livros.presentation.books.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftm.livros.domain.model.ReadingStatus
import com.iftm.livros.domain.usecase.DeleteBookUseCase
import com.iftm.livros.domain.usecase.ObserveBooksUseCase
import com.iftm.livros.domain.usecase.RestoreBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BookListEvent {
    data class BookDeleted(val id: Long, val title: String) : BookListEvent
    data class Message(val text: String) : BookListEvent
}

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val observeBooks: ObserveBooksUseCase,
    private val deleteBook: DeleteBookUseCase,
    private val restoreBook: RestoreBookUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BookListEvent>()
    val events = _events.asSharedFlow()

    init {
        observeBooks()
            .onEach { list ->
                _uiState.update { it.copy(books = list, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onStatusFilterSelected(status: ReadingStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onDeleteBook(id: Long) {
        val book = _uiState.value.books.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            deleteBook(id)
            _events.emit(BookListEvent.BookDeleted(id, book.title))
        }
    }

    fun onUndoDelete(id: Long) {
        viewModelScope.launch {
            restoreBook(id)
            _events.emit(BookListEvent.Message("Livro restaurado com sucesso!"))
        }
    }
}
