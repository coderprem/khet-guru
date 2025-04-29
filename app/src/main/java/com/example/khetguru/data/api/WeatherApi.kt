package com.example.khetguru.data.api

import com.example.khetguru.data.remote.currentLocationDto.CurrentLocationDto
import com.example.khetguru.data.remote.weatherDto.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/forecast.json")
    suspend fun getWeatherData(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): Response<WeatherDto>
}