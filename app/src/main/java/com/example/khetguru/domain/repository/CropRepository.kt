package com.example.khetguru.domain.repository

import com.example.khetguru.data.remote.marketPriceDto.Record

interface CropRepository {
    suspend fun saveCrop(userId: String, crop: Record): Boolean
    suspend fun isCropExist(userId: String, crop: Record): Boolean
    suspend fun getSavedCrops(userId: String): List<Record>
    suspend fun deleteCrop(userId: String, crop: Record): Boolean
}
