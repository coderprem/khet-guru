package com.example.khetguru.domain.usecase

import com.example.khetguru.domain.repository.DiseaseRepository
import java.io.File
import javax.inject.Inject

class DiseaseIdentificationUseCase @Inject constructor(
    private val repository: DiseaseRepository
) {
    suspend operator fun invoke(imageFile: File): Result<String> {
        return repository.identifyDisease(imageFile)
    }
}
