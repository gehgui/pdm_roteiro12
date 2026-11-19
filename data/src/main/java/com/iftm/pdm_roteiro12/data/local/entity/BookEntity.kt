package com.iftm.pdm_roteiro12.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "google_book_id")
    val googleBookId: String = "",

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "authors")
    val authors: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String = "",

    @ColumnInfo(name = "page_count")
    val pageCount: Int = 0,

    @ColumnInfo(name = "published_date")
    val publishedDate: String = "",

    @ColumnInfo(name = "reading_status")
    val readingStatus: String,

    @ColumnInfo(name = "user_rating")
    val userRating: Int = 0,

    @ColumnInfo(name = "user_notes")
    val userNotes: String = "",

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long? = null,
)
