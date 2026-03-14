package com.example.dialogtrainer.data.repository

import com.example.dialogtrainer.data.model.Role
import com.example.dialogtrainer.data.model.Scene
import com.example.dialogtrainer.data.model.SceneLine

class SceneRepository {

    fun getScenes(): List<Scene> {
        return listOf(
            Scene(
                id = 1,
                title = "At the restaurant",
                description = "A simple conversation between a customer and a waiter.",
                difficulty = 1,
                lines = listOf(
                    SceneLine(
                        id = 1,
                        order = 1,
                        role = Role.WAITER,
                        textGerman = "Guten Tag! Möchten Sie etwas bestellen?",
                        textTranslation = "Good afternoon! Would you like to order something?"
                    ),
                    SceneLine(
                        id = 2,
                        order = 2,
                        role = Role.CUSTOMER,
                        textGerman = "Ja, ich hätte gern eine Suppe.",
                        textTranslation = "Yes, I would like a soup."
                    )
                )
            )
        )
    }
}
