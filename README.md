# Meus Livros - Catálogo & Biblioteca Pessoal

Aplicativo Android desenvolvido como solução do **Roteiro 12 de Programação para Dispositivos Móveis (PDM)**.

O app permite cadastrar, acompanhar e gerenciar livros, consultando dados automaticamente na **Google Books API** e classificando-os por status de leitura (**Lendo**, **Lido** ou **Interesse**), com suporte completo a **edição**, **exclusão lógica (soft delete)** e **restauração via lixeira**.

---

## 🎯 Atendimento aos Requisitos

| Requisito | Como foi atendido |
| :--- | :--- |
| **Isolamento de Domínio (Regras de negócio)** | Módulo `:domain` em **Kotlin puro**, sem nenhuma dependência de Android, Room ou Retrofit. Contém entidades (`Book`, `ReadingStatus`), repositórios abstratos, validadores e casos de uso. |
| **Uso do Hilt para Injeção de Dependências** | Hilt configurado em `:app` e `:data` (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `NetworkModule`, `DatabaseModule`, `RepositoryModule`, `UseCaseModule`). |
| **Uso do Retrofit e OkHttp** | Cliente HTTP configurado com `OkHttpClient` (com logging interceptor de requisições) e `Retrofit` com `GsonConverterFactory` consumindo a Google Books API. |
| **Mínimo de uma requisição a uma API** | Integração com a **Google Books API** (`https://www.googleapis.com/books/v1/volumes?q={termo}`) para busca de títulos, autores, capas e sinopses. |
| **Arquitetura Limpa (Clean Architecture)** | Separação estrita em três módulos: `:domain` (regras e entidades), `:data` (Room, Retrofit, mappers e repositórios concretos) e `:app` (UI Jetpack Compose, ViewModels, navegação). |
| **Cadastro com requisição a uma API** | Na tela de formulário ("Novo Livro"), o usuário pode pesquisar diretamente na Google Books API e selecionar uma obra; os campos de título, autor(es), sinopse, capa e páginas são preenchidos automaticamente. |
| **Navegação entre páginas** | Navegação por rotas tipadas com **Navigation Compose** e barra de navegação inferior (`Biblioteca`, `Novo Livro` e `Lixeira`). |
| **Exclusão lógica (Soft Delete) e Edição** | Livros podem ser editados a qualquer momento. A exclusão marca `isDeleted = true` (mantendo o registro no banco), remove-o da lista ativa e o envia para a **Lixeira**, permitindo restaurá-lo ou excluí-lo permanentemente. |

---

## 🏛️ Arquitetura do Projeto

O projeto é modularizado de acordo com os princípios da Arquitetura Limpa:

```text
pdm_roteiro12/
├── app/                  # Módulo de Apresentação (UI Android / Jetpack Compose)
│   └── src/main/java/com/iftm/livros/
│       ├── presentation/
│       │   ├── books/
│       │   │   ├── list/     # Lista de livros, filtros por status, busca local
│       │   │   ├── form/     # Cadastro e edição com integração à Google Books API
│       │   │   └── trash/    # Lixeira com restauração e exclusão permanente
│       │   ├── components/   # BookCard, BookCoverImage, RatingBar, Badges, etc.
│       │   └── navigation/   # Rotas tipadas e NavHost com barra inferior
│       ├── di/               # UseCaseModule provendo casos de uso para ViewModels
│       ├── ui/theme/         # Design System com Material 3
│       ├── BookApplication.kt
│       └── MainActivity.kt
│
├── domain/               # Módulo de Domínio (Kotlin Puro - Regras de Negócio)
│   └── src/main/kotlin/com/iftm/livros/domain/
│       ├── model/            # Book, ReadingStatus, GoogleBookSearchItem
│       ├── repository/       # Interfaces BookRepository e GoogleBooksRepository
│       ├── usecase/          # ObserveBooks, SaveBook, DeleteBook, RestoreBook, etc.
│       ├── validation/       # BookValidator e regras de validação
│       └── error/            # DomainException (BookNotFound, ValidationException, etc.)
│
└── data/                 # Módulo de Dados (Acesso a dados e Integrações)
    └── src/main/java/com/iftm/livros/data/
        ├── remote/           # GoogleBooksApiService (Retrofit/OkHttp) e DTOs
        ├── local/            # Room Database, BookDao, BookEntity
        ├── mapper/           # Mappers entre DTOs, Entidades e Domínio
        ├── repository/       # Implementação concreta dos repositórios
        └── di/               # NetworkModule, DatabaseModule, RepositoryModule
```

---

## 🚀 Funcionalidades

1. **Minha Biblioteca**:
   - Visualização dos livros cadastrados com capas, títulos, autores e status.
   - Filtros por abas: `Todos`, `Lendo`, `Lido` e `Interesse`.
   - Busca instantânea por título ou autor.
   - Ação rápida de exclusão (soft delete) com Snackbar de confirmação e botão **Desfazer**.

2. **Cadastro & Edição de Livros**:
   - Campo integrado para consultar a **Google Books API**.
   - Diálogo com pré-visualização das obras encontradas no Google Books.
   - Auto-preenchimento dos dados do livro selecionado (Título, Autor, Capa, Sinopse, Páginas, Ano).
   - Definição do status de leitura: `Lendo`, `Lido` ou `Interesse`.
   - Avaliação por estrelas (0 a 5).
   - Campo para anotações e resenhas pessoais.

3. **Lixeira (Soft Delete)**:
   - Exibe todos os livros que foram excluídos logicamente.
   - Permite **Restaurar** o livro de volta para a biblioteca.
   - Permite **Excluir Definitivamente** o livro do banco de dados Room.

---

## 🛠️ Tecnologias Utilizadas

- **Kotlin** & **Coroutines** & **Flow**
- **Jetpack Compose** (Material 3)
- **Jetpack Navigation Compose** (Rotas Type-Safe com Kotlinx Serialization)
- **Room Database** (Persistência local com SQLite e Soft Delete)
- **Retrofit 2** & **OkHttp 3** (Consumo de API REST e Logging Interceptor)
- **Google Books API** (Endpoint `/volumes`)
- **Dagger Hilt** (Injeção de Dependências)
- **Coil** (Carregamento assíncrono de imagens de capa)
- **JUnit 4** & **Kotlinx Coroutines Test** (Testes unitários de domínio)

---

## 🧪 Testes Unitários

O módulo `:domain` possui cobertura completa de testes unitários testando:
- Validação e normalização de livros salvos.
- Rejeição de livros com dados inválidos.
- Funcionamento do soft delete e restauração.
- Exclusão definitiva de livros.
- Filtragem por status de leitura.
- Validação da consulta à API do Google Books.

Para executar os testes:
```bash
./gradlew :domain:test
```
