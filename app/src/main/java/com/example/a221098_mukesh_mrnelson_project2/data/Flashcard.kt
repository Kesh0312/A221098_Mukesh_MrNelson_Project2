package com.example.a221098_mukesh_mrnelson_project2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topic: String,
    val notes: String,
    val priority: Int
)