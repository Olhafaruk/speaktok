//data/model/Country.kt
package com.example.dialogtrainer.data.model

enum class Country(
    val code: String,
    val title: String,
    val flag: String
) {
    UKRAINE("UA", "Ukraine", "🇺🇦"),
    GERMANY("DE", "Germany", "🇩🇪"),
    POLAND("PL", "Poland", "🇵🇱"),
    FRANCE("FR", "France", "🇫🇷"),
    ITALY("IT", "Italy", "🇮🇹"),
    SPAIN("ES", "Spain", "🇪🇸"),
    USA("US", "United States", "🇺🇸"),
    UK("GB", "United Kingdom", "🇬🇧");

    companion object {
        fun fromTitle(title: String): Country? =
            entries.firstOrNull { it.title == title }
    }
}


