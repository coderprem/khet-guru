package com.example.khetguru.domain.repository

interface CropRecommendationRepository {
    suspend fun getCropRecommendation(
        N: Float,
        P: Float,
        K: Float,
        temperature: Float,
        humidity: Float,
        ph: Float,
        rainfall: Float
    ): String
}