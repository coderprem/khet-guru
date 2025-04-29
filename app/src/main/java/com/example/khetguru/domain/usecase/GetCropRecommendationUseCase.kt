package com.example.khetguru.domain.usecase

import com.example.khetguru.domain.repository.CropRecommendationRepository
import javax.inject.Inject

class GetCropRecommendationUseCase @Inject constructor(
    private val cropRecommendationRepository: CropRecommendationRepository
) {
    suspend operator fun invoke(
        N: Float,
        P: Float,
        K: Float,
        temperature: Float,
        humidity: Float,
        ph: Float,
        rainfall: Float
    ): String {
        return cropRecommendationRepository.getCropRecommendation(
            N,
            P,
            K,
            temperature,
            humidity,
            ph,
            rainfall
        )
    }
}