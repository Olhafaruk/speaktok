package com.example.dialogtrainer.data.model

data class SceneLine(
    val id: Int,
    val order: Int,
    val role: Role,
    val textGerman: String,
    val textTranslation: String
)
