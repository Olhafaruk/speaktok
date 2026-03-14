//IconUtils.kt
package com.example.dialogtrainer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.dialogtrainer.data.model.Role

fun iconForRole(role: Role): ImageVector {
    return when (role) {
        Role.WAITER -> Icons.Filled.Restaurant
        Role.CUSTOMER -> Icons.Filled.Person
        Role.DOCTOR -> Icons.Filled.MedicalServices
        Role.FRIEND -> Icons.Filled.Face
        Role.NARRATOR -> Icons.Filled.RecordVoiceOver
    }
}
