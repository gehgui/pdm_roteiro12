package com.iftm.pdm_roteiro12.presentation.books.list

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iftm.pdm_roteiro12.domain.model.ReadingStatus
import com.iftm.pdm_roteiro12.presentation.components.BookCard
import com.iftm.pdm_roteiro12.presentation.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    snackbarHostState: SnackbarHostState,
    onEditBook: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BookListEvent.BookDeleted -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "\"${event.title}\" movido para a lixeira.",
                        actionLabel = "Desfazer",
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onUndoDelete(event.id)
                    }
                }
                is BookListEvent.Message -> {
                    snackbarHostState.showSnackbar(event.text)
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Minha Biblioteca") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

        // Barra de pesquisa local
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            placeholder = { Text("Filtrar por título ou autor...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (uiState.searchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Limpar busca")
                    }
                }
            },
            singleLine = true,
        )

        // Filtros por status de leitura
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = uiState.selectedStatus == null,
                onClick = { viewModel.onStatusFilterSelected(null) },
                label = { Text("Todos") },
            )
            ReadingStatus.entries.forEach { status ->
                FilterChip(
                    selected = uiState.selectedStatus == status,
                    onClick = {
                        viewModel.onStatusFilterSelected(
                            if (uiState.selectedStatus == status) null else status,
                        )
                    },
                    label = { Text(status.displayName) },
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Conteúdo da lista
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.displayedBooks.isEmpty() -> {
                    EmptyState(
                        title = if (uiState.books.isEmpty()) "Sua biblioteca está vazia" else "Nenhum livro encontrado",
                        message = if (uiState.books.isEmpty()) {
                            "Toque na aba 'Novo Livro' para pesquisar no Google Books e adicionar à sua estante!"
                        } else {
                            "Tente buscar com outros termos ou altere o filtro de status."
                        },
                        icon = Icons.Outlined.LibraryBooks,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            items = uiState.displayedBooks,
                            key = { it.id },
                        ) { book ->
                            BookCard(
                                book = book,
                                onEdit = onEditBook,
                                onDelete = viewModel::onDeleteBook,
                            )
                        }
                    }
                }
            }
        }
    }
}
