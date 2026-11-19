package com.iftm.pdm_roteiro12.presentation.books.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.domain.usecase.ObserveDeletedBooksUseCase
import com.iftm.pdm_roteiro12.domain.usecase.PermanentDeleteBookUseCase
import com.iftm.pdm_roteiro12.domain.usecase.RestoreBookUseCase
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

data class TrashUiState(
    val deletedBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface TrashEvent {
    data class Message(val text: String) : TrashEvent
}

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val observeDeletedBooks: ObserveDeletedBooksUseCase,
    private val restoreBook: RestoreBookUseCase,
    private val permanentDeleteBook: PermanentDeleteBookUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrashUiState())
    val uiState: StateFlow<TrashUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TrashEvent>()
    val events = _events.asSharedFlow()

    init {
        observeDeletedBooks()
            .onEach { list ->
                _uiState.update { it.copy(deletedBooks = list, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onRestore(id: Long) {
        viewModelScope.launch {
            restoreBook(id)
            _events.emit(TrashEvent.Message("Livro restaurado com sucesso!"))
        }
    }

    fun onPermanentDelete(id: Long) {
        viewModelScope.launch {
            permanentDeleteBook(id)
            _events.emit(TrashEvent.Message("Livro excluído definitivamente."))
        }
    }
}
