package com.example.khetguru.domain.usecase

import com.example.khetguru.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentLocationWeatherDataUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double) = repository.getCurrentLocationWeatherData(
        latitude = latitude,
        longitude = longitude
    )
}