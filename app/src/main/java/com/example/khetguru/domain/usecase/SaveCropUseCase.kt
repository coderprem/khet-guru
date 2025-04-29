package com.example.khetguru.domain.usecase

import com.example.khetguru.data.remote.marketPriceDto.Record
import com.example.khetguru.domain.repository.CropRepository
import javax.inject.Inject

class SaveCropUseCase @Inject constructor(
    private val cropRepository: CropRepository
) {
    suspend fun execute(userId: String, crop: Record): Boolean {
        return if (!cropRepository.isCropExist(userId, crop)) {
            cropRepository.saveCrop(userId, crop)
        } else {
            false
        }
    }
}

