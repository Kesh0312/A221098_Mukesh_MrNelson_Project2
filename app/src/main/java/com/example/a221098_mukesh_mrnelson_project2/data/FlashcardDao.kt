package com.example.a221098_mukesh_mrnelson_project2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Flashcard)

    @Update
    suspend fun updateCard(card: Flashcard)

    @Delete
    suspend fun deleteCard(card: Flashcard)

    @Query("SELECT * FROM flashcards ORDER BY priority DESC")
    fun getAllCardsStream(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE id = :id")
    fun getCardStream(id: Int): Flow<Flashcard?>
}