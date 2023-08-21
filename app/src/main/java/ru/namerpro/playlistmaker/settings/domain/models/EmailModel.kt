package ru.namerpro.playlistmaker.settings.domain.models

data class EmailModel(
    val contactEmail: String,
    val extraSubject: String,
    val extraText: String
)
