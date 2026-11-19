package com.iftm.pdm_roteiro12.presentation.books.trash

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iftm.pdm_roteiro12.domain.model.Book
import com.iftm.pdm_roteiro12.presentation.components.BookCoverImage
import com.iftm.pdm_roteiro12.presentation.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TrashViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var bookToPermanentlyDelete by remember { mutableStateOf<Book?>(null) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TrashEvent.Message -> snackbarHostState.showSnackbar(event.text)
            }
        }
    }

    if (bookToPermanentlyDelete != null) {
        AlertDialog(
            onDismissRequest = { bookToPermanentlyDelete = null },
            title = { Text("Excluir definitivamente?") },
            text = { Text("O livro \"${bookToPermanentlyDelete?.title}\" será apagado do banco de dados permanentemente e não poderá ser recuperado.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookToPermanentlyDelete?.let { viewModel.onPermanentDelete(it.id) }
                        bookToPermanentlyDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { bookToPermanentlyDelete = null }) {
                    Text("Cancelar")
                }
            },
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Lixeira") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.deletedBooks.isEmpty() -> {
                    EmptyState(
                        title = "A lixeira está vazia",
                        message = "Livros excluídos (soft delete) aparecerão aqui e poderão ser restaurados a qualquer momento.",
                        icon = Icons.Outlined.DeleteSweep,
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
                            items = uiState.deletedBooks,
                            key = { it.id },
                        ) { book ->
                            DeletedBookCard(
                                book = book,
                                onRestore = { viewModel.onRestore(book.id) },
                                onPermanentDelete = { bookToPermanentlyDelete = book },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeletedBookCard(
    book: Book,
    onRestore: () -> Unit,
    onPermanentDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BookCoverImage(
                    url = book.thumbnailUrl,
                    contentDescription = book.title,
                    width = 55.dp,
                    height = 80.dp,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (book.authors.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = book.authors,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Excluído logicamente (soft delete)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    onClick = onPermanentDelete,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp),
                    )
                    Text("Excluir")
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(onClick = onRestore) {
                    Icon(
                        imageVector = Icons.Filled.Restore,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp),
                    )
                    Text("Restaurar")
                }
            }
        }
    }
}
