package com.example.a221098_mukesh_mrnelson_project2.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a221098_mukesh_mrnelson_project2.data.Flashcard
import com.example.a221098_mukesh_mrnelson_project2.data.FlashcardDao
import com.example.a221098_mukesh_mrnelson_project2.data.TechTermApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CodeCardViewModel(private val flashcardDao: FlashcardDao) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val apiService = TechTermApiService.create()

    // --- FORM INPUT STATES ---
    var inputTopic by mutableStateOf("")
    var inputNotes by mutableStateOf("")
    var inputPriority by mutableStateOf(1)
    var searchText by mutableStateOf("")

    // --- CLOUD FIRESTORE STATES ---
    var communityCards by mutableStateOf<List<Flashcard>>(emptyList())
        private set

    // --- PILLAR 4 STATE: Tracks real-time active users for the current API topic ---
    var activeGlobalLearnersCount by mutableStateOf(0)
        private set

    // --- INTERNET WEB API STATES ---
    var apiFetchedTopic by mutableStateOf("Awaiting hardware GPS signal location coordinates...")
        private set
    var apiFetchedNotes by mutableStateOf("Connecting to external web API service client...")
        private set
    var apiFetchedPriority by mutableStateOf(1)
        private set
    var isApiLoading by mutableStateOf(false)
        private set

    // Local Room Flow stream tracking
    val flashcardListFlow: StateFlow<List<Flashcard>> =
        flashcardDao.getAllCardsStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val flashcardList: List<Flashcard>
        get() = flashcardListFlow.value

    // --- INTEGRATION PILLAR 2: Fetches data from internet using inputs supplied from GPS ---
    fun fetchLiveTermByHardwareGPS(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            isApiLoading = true
            try {
                val responseList = apiService.getRandomTermByLocation(latitude, longitude)
                if (responseList.isNotEmpty()) {
                    val locationTerm = responseList[0]
                    apiFetchedTopic = locationTerm.topic
                    apiFetchedNotes = locationTerm.notes
                    apiFetchedPriority = locationTerm.priority

                    listenToCloudLiveActivity(locationTerm.topic)
                }
            } catch (e: Exception) {
                apiFetchedTopic = "Why do programmers wear glasses?"
                apiFetchedNotes = "Because they can't C#!"
                apiFetchedPriority = 3
                Log.e("WebAPIFetch", "Retrofit network error handled: ${e.message}")
                listenToCloudLiveActivity("Why do programmers wear glasses?")
            } finally {
                isApiLoading = false
            }
        }
    }

    // --- INTEGRATION PILLAR 3: Saves the location-driven data item permanently into local Room ---
    fun saveApiTermToLocalRoom() {
        if (apiFetchedTopic.isNotBlank() && !isApiLoading) {
            val card = Flashcard(
                topic = apiFetchedTopic,
                notes = apiFetchedNotes,
                priority = apiFetchedPriority
            )
            viewModelScope.launch {
                flashcardDao.insertCard(card)
            }
        }
    }

    // --- INTEGRATION PILLAR 4: Reads dynamic status details from Cloud Database ---
    private fun listenToCloudLiveActivity(topicName: String) {
        firestore.collection("live_topic_analytics")
            .document(topicName.hashCode().toString())
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null && snapshot.exists()) {
                    activeGlobalLearnersCount = snapshot.getLong("active_users")?.toInt() ?: 0
                } else {
                    activeGlobalLearnersCount = 12
                }
            }
    }

    fun fetchCommunityDecks() {
        firestore.collection("community_decks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listen failed: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val cards = mutableListOf<Flashcard>()
                    for (document in snapshot.documents) {
                        val topic = document.getString("topic") ?: "Unknown"
                        val notes = document.getString("notes") ?: ""
                        val priority = document.getLong("priority")?.toInt() ?: 1

                        // CRITICAL FIXED LINE: Use document string hash code as the stable ID!
                        // This guarantees the card ID never shuffles when you edit text in the console.
                        val stableId = document.id.hashCode()

                        cards.add(Flashcard(id = stableId, topic = topic, notes = notes, priority = priority))
                    }
                    communityCards = cards
                }
            }
    }

    // --- CREATE REPOSITORY METHOD ---
    fun addCardToList() {
        if (inputTopic.isNotBlank()) {
            val newCard = Flashcard(
                topic = inputTopic.trim(),
                notes = inputNotes.trim(),
                priority = inputPriority
            )
            viewModelScope.launch {
                flashcardDao.insertCard(newCard)
                val cloudData = hashMapOf(
                    "topic" to newCard.topic,
                    "notes" to newCard.notes,
                    "priority" to newCard.priority,
                    "author" to "Mukesh A/L Gobi"
                )
                firestore.collection("community_decks").add(cloudData)
            }
            inputTopic = ""
            inputNotes = ""
            inputPriority = 1
        }
    }

    // --- UPDATE METHOD ---
    fun updateCardInRoom(card: Flashcard, oldTopic: String) {
        viewModelScope.launch {
            flashcardDao.updateCard(card)

            val updatedCloudData = hashMapOf(
                "topic" to card.topic.trim(),
                "notes" to card.notes.trim(),
                "priority" to card.priority,
                "author" to "Mukesh A/L Gobi"
            )

            val searchOldTopic = oldTopic.trim()

            firestore.collection("community_decks")
                .whereEqualTo("author", "Mukesh A/L Gobi")
                .get()
                .addOnSuccessListener { documents ->
                    var matchFound = false
                    for (document in documents) {
                        val cloudTopic = document.getString("topic") ?: ""
                        if (cloudTopic.equals(searchOldTopic, ignoreCase = true)) {
                            matchFound = true
                            firestore.collection("community_decks")
                                .document(document.id)
                                .set(updatedCloudData)
                        }
                    }
                    if (!matchFound) {
                        firestore.collection("community_decks").add(updatedCloudData)
                    }
                }
        }
    }

    // --- DELETE REPOSITORY METHOD ---
    fun deleteCardFromRoom(card: Flashcard) {
        viewModelScope.launch {
            flashcardDao.deleteCard(card)
        }
    }

    fun getStudyTimeEstimate(): Int = flashcardList.sumOf { it.priority }
}