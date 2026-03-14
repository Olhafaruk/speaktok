package com.example.dialogtrainer.data.model

data class Scene(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: Int,
    val lines: List<SceneLine>
)
