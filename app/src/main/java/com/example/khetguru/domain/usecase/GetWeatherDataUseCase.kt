package com.example.khetguru.domain.usecase

import com.example.khetguru.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String) = repository.getWeatherData(city)
}