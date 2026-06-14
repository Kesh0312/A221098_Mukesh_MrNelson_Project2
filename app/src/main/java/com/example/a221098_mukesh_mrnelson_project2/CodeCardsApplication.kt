package com.example.a221098_mukesh_mrnelson_project2

import android.app.Application
import com.example.a221098_mukesh_mrnelson_project2.data.FlashcardDatabase
import com.google.firebase.FirebaseApp

class CodeCardsApplication : Application() {

    // Initialize your Room Database lazily
    val database: FlashcardDatabase by lazy { FlashcardDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        // CRITICAL RUNTIME FIX: Force manual context initialization for Firebase
        FirebaseApp.initializeApp(this)
    }
}