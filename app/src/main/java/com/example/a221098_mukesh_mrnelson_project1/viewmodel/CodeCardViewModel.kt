package com.example.a221098_mukesh_mrnelson_project1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.a221098_mukesh_mrnelson_project1.data.Flashcard
import java.util.UUID

class CodeCardViewModel : ViewModel() {
    var inputTopic by mutableStateOf("")
    var inputNotes by mutableStateOf("")
    var inputPriority by mutableIntStateOf(1)

    // --- SEARCH LOGIC ---
    var searchText by mutableStateOf("")

    val flashcardList = mutableStateListOf<Flashcard>()

    // This list automatically updates whenever the user types in the search bar
    val filteredList: List<Flashcard>
        get() = if (searchText.isEmpty()) {
            flashcardList
        } else {
            flashcardList.filter { it.topic.contains(searchText, ignoreCase = true) }
        }

    fun addCardToList() {
        if (inputTopic.isNotBlank()) {
            flashcardList.add(
                Flashcard(
                    id = UUID.randomUUID().toString(),
                    topic = inputTopic,
                    notes = inputNotes,
                    priority = inputPriority
                )
            )
            inputTopic = ""
            inputNotes = ""
            inputPriority = 1
        }
    }

    fun getStudyTimeEstimate(): Int {
        return flashcardList.sumOf { it.priority }
    }
}