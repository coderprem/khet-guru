package com.example.khetguru.data.repository

import com.example.khetguru.data.api.OpenWeatherApi
import com.example.khetguru.data.api.WeatherApi
import com.example.khetguru.data.remote.currentLocationDto.CurrentLocationDto
import com.example.khetguru.data.remote.weatherDto.WeatherDto
import com.example.khetguru.domain.repository.WeatherRepository
import com.example.khetguru.domain.utils.WEATHER_API_KEY
import com.example.khetguru.domain.utils.CURRENT_WEATHER_API_KEY
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val openWeatherApi: OpenWeatherApi
): WeatherRepository {
    override suspend fun getWeatherData(city: String): WeatherDto {
        val response = weatherApi.getWeatherData(
            apiKey = WEATHER_API_KEY,
            city = city
        )
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("API error")
        }
    }

    override suspend fun getCurrentLocationWeatherData(
        latitude: Double,
        longitude: Double
    ): CurrentLocationDto {
        val response = openWeatherApi.getCurrentLocationWeatherData(
            latitude = latitude,
            longitude = longitude,
            apiKey = CURRENT_WEATHER_API_KEY
        )
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("API error")
        }
    }
}