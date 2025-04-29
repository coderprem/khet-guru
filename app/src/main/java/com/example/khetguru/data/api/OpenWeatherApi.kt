package com.example.khetguru.data.api

import com.example.khetguru.data.remote.currentLocationDto.CurrentLocationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentLocationWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<CurrentLocationDto>
}