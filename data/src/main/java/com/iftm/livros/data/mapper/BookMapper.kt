package com.iftm.livros.data.mapper

import com.iftm.livros.data.local.entity.BookEntity
import com.iftm.livros.domain.model.Book
import com.iftm.livros.domain.model.ReadingStatus

fun BookEntity.toDomain(): Book = Book(
    id = id,
    googleBookId = googleBookId,
    title = title,
    authors = authors,
    description = description,
    thumbnailUrl = thumbnailUrl,
    pageCount = pageCount,
    publishedDate = publishedDate,
    readingStatus = ReadingStatus.fromString(readingStatus),
    userRating = userRating,
    userNotes = userNotes,
    isDeleted = isDeleted,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt,
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    googleBookId = googleBookId,
    title = title,
    authors = authors,
    description = description,
    thumbnailUrl = thumbnailUrl,
    pageCount = pageCount,
    publishedDate = publishedDate,
    readingStatus = readingStatus.name,
    userRating = userRating,
    userNotes = userNotes,
    isDeleted = isDeleted,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt,
)
