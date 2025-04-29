package com.example.khetguru.data.repository

import com.example.khetguru.data.api.CropApiService
import com.example.khetguru.data.remote.cropRecommendDto.CropRequest
import com.example.khetguru.domain.repository.CropRecommendationRepository
import javax.inject.Inject

class CropRecommendationRepositoryImpl @Inject constructor(
    private val api: CropApiService
) : CropRecommendationRepository {

    override suspend fun getCropRecommendation(
        N: Float,
        P: Float,
        K: Float,
        temperature: Float,
        humidity: Float,
        ph: Float,
        rainfall: Float
    ): String {
        val response = api.predictCrop(
            CropRequest(
                N = N,
                P = P,
                K = K,
                temperature = temperature,
                humidity = humidity,
                ph = ph,
                rainfall = rainfall
            )
        )
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.recommended_crop
        } else {
            throw Exception("API error")
        }
    }
}