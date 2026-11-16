package com.iftm.livros.presentation.books.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iftm.livros.domain.model.GoogleBookSearchItem
import com.iftm.livros.domain.model.ReadingStatus
import com.iftm.livros.domain.validation.BookField
import com.iftm.livros.presentation.components.BookCoverImage
import com.iftm.livros.presentation.components.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormScreen(
    snackbarHostState: SnackbarHostState,
    isEditing: Boolean,
    onSaved: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookFormViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BookFormEvent.Saved -> {
                    snackbarHostState.showSnackbar(event.message)
                    onSaved()
                }
                is BookFormEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    // Diálogo com os resultados da busca no Google Books
    if (uiState.isSearchDialogVisible) {
        GoogleBooksResultsDialog(
            results = uiState.apiResults,
            onSelect = viewModel::onSelectApiBook,
            onDismiss = viewModel::closeSearchDialog,
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(if (isEditing) "Editar Livro" else "Novo Livro") },
            navigationIcon = {
                if (isEditing) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Seção de busca na API do Google Books
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Buscar dados no Google Books API",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedTextField(
                            value = uiState.apiQuery,
                            onValueChange = viewModel::onApiQueryChange,
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Nome do livro ou autor...") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                focusManager.clearFocus()
                                viewModel.searchGoogleBooks()
                            }),
                        )

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.searchGoogleBooks()
                            },
                            enabled = !uiState.isSearchingApi && uiState.apiQuery.isNotBlank(),
                        ) {
                            if (uiState.isSearchingApi) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            } else {
                                Icon(Icons.Filled.Search, contentDescription = "Buscar")
                            }
                        }
                    }

                    if (uiState.apiFeedbackMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = uiState.apiFeedbackMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }

            // Preview da capa do livro
            if (uiState.thumbnailUrl.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    BookCoverImage(
                        url = uiState.thumbnailUrl,
                        contentDescription = "Capa do livro",
                        width = 110.dp,
                        height = 160.dp,
                    )
                }
            }

            // Campo: Título
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Título do Livro *") },
                isError = BookField.TITLE in uiState.errors,
                supportingText = uiState.errors[BookField.TITLE]?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Campo: Autores
            OutlinedTextField(
                value = uiState.authors,
                onValueChange = viewModel::onAuthorsChange,
                label = { Text("Autor(es) *") },
                isError = BookField.AUTHORS in uiState.errors,
                supportingText = uiState.errors[BookField.AUTHORS]?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Seleção de Status de Leitura (Lendo, Lido, Interesse)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Status de Leitura:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ReadingStatus.entries.forEach { status ->
                        FilterChip(
                            selected = uiState.readingStatus == status,
                            onClick = { viewModel.onReadingStatusChange(status) },
                            label = { Text(status.displayName) },
                        )
                    }
                }
            }

            // Avaliação por estrelas
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Sua Avaliação (0 a 5 estrelas):",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(6.dp))
                RatingBar(
                    rating = uiState.userRating,
                    starSize = 32.dp,
                    onRatingChanged = viewModel::onRatingChange,
                )
            }

            // Páginas e Ano de Publicação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = if (uiState.pageCount > 0) uiState.pageCount.toString() else "",
                    onValueChange = viewModel::onPageCountChange,
                    label = { Text("Páginas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )

                OutlinedTextField(
                    value = uiState.publishedDate,
                    onValueChange = viewModel::onPublishedDateChange,
                    label = { Text("Publicação") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
            }

            // URL da imagem de capa (editável ou preenchida pela API)
            OutlinedTextField(
                value = uiState.thumbnailUrl,
                onValueChange = viewModel::onThumbnailUrlChange,
                label = { Text("URL da Capa") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Anotações pessoais
            OutlinedTextField(
                value = uiState.userNotes,
                onValueChange = viewModel::onNotesChange,
                label = { Text("Anotações Pessoais / Resenha") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
            )

            // Descrição / Sinopse
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Sinopse / Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botão Salvar
            Button(
                onClick = viewModel::onSave,
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(
                        text = if (isEditing) "Salvar Alterações" else "Cadastrar Livro",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun GoogleBooksResultsDialog(
    results: List<GoogleBookSearchItem>,
    onSelect: (GoogleBookSearchItem) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecione um Livro do Google Books") },
        text = {
            if (results.isEmpty()) {
                Text("Nenhum resultado encontrado. Tente pesquisar com outro nome.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(results, key = { it.id }) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(item) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            BookCoverImage(
                                url = item.thumbnailUrl,
                                contentDescription = item.title,
                                width = 50.dp,
                                height = 75.dp,
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = item.authorsFormatted,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                if (item.publishedDate.isNotBlank()) {
                                    Text(
                                        text = item.publishedDate,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline,
                                    )
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        },
    )
}
