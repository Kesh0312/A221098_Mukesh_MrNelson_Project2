package com.example.a221098_mukesh_mrnelson_project2.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Data Model matching the API response structure based on coordinates
data class TechTermResponse(
    val setup: String,      // Serves as our "Topic"
    val punchline: String   // Serves as our "Notes"
) {
    val topic: String get() = setup
    val notes: String get() = punchline
    val priority: Int get() = 3
}

interface TechTermApiService {
    // Passes hardware GPS parameters directly to the web API endpoint
    @GET("jokes/programming/random")
    suspend fun getRandomTermByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): List<TechTermResponse>

    companion object {
        fun create(): TechTermApiService {
            return Retrofit.Builder()
                .baseUrl("https://official-joke-api.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TechTermApiService::class.java)
        }
    }
}