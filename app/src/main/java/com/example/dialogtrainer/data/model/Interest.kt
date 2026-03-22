//data/model/Interest.kt
package com.example.dialogtrainer.data.model

enum class Interest(val title: String) {
    TRAVEL("Travel"),
    MUSIC("Music"),
    MOVIES("Movies"),
    BOOKS("Books"),
    FITNESS("Fitness"),
    COOKING("Cooking"),
    IT("IT"),
    LANGUAGES("Languages")
}

val allInterests = listOf(
    Interest.TRAVEL,
    Interest.MUSIC,
    Interest.MOVIES,
    Interest.BOOKS,
    Interest.FITNESS,
    Interest.COOKING,
    Interest.IT,
    Interest.LANGUAGES
)
