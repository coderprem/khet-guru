package com.example.khetguru.domain.repository

import java.io.File

interface DiseaseRepository {
    suspend fun identifyDisease(imageFile: File): Result<String>
    suspend fun formatApiResponse(response: String): String
}