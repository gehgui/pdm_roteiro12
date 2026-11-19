package com.iftm.pdm_roteiro12.domain.validation

enum class BookField {
    TITLE,
    AUTHORS,
    STATUS,
}

class BookValidator {
    fun validate(title: String, authors: String): Map<BookField, String> {
        val errors = mutableMapOf<BookField, String>()

        if (title.isBlank()) {
            errors[BookField.TITLE] = "O título do livro é obrigatório."
        }

        if (authors.isBlank()) {
            errors[BookField.AUTHORS] = "Informe ao menos um autor."
        }

        return errors
    }
}
