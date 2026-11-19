package com.iftm.pdm_roteiro12.domain.model

enum class ReadingStatus(val displayName: String) {
    LENDO("Lendo"),
    LIDO("Lido"),
    INTERESSE("Interesse");

    companion object {
        fun fromString(value: String): ReadingStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: INTERESSE
    }
}
