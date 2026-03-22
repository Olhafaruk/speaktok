//data/model/AppLanguage.kt
package com.example.dialogtrainer.data.model

enum class AppLanguage(
    val code: String,
    val flag: String
) {
    EN("en", "🇬🇧"),
    DE("de", "🇩🇪"),
    FR("fr", "🇫🇷"),
    ES("es", "🇪🇸"),
    IT("it", "🇮🇹"),
    PL("pl", "🇵🇱"),
    RU("ru", "🇷🇺"),
    UK("uk", "🇺🇦");

    companion object {
        fun fromCode(code: String): AppLanguage? =
            entries.firstOrNull { it.code == code }
    }
}
