package com.example.khetguru.data.remote.weatherDto

data class WeatherDto(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)