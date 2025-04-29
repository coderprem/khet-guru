package com.example.khetguru.domain.repository

import com.example.khetguru.data.remote.currentLocationDto.CurrentLocationDto
import com.example.khetguru.data.remote.weatherDto.WeatherDto

interface WeatherRepository {
    suspend fun getWeatherData(city: String): WeatherDto
    suspend fun getCurrentLocationWeatherData(latitude: Double, longitude: Double): CurrentLocationDto
}