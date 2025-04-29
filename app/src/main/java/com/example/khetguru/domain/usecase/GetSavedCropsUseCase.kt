package com.example.khetguru.domain.usecase

import com.example.khetguru.data.remote.marketPriceDto.Record
import com.example.khetguru.domain.repository.CropRepository
import javax.inject.Inject

class GetSavedCropsUseCase @Inject constructor(
    private val cropRepository: CropRepository
) {
    suspend operator fun invoke(userId: String): List<Record> {
        return cropRepository.getSavedCrops(userId)
    }
}