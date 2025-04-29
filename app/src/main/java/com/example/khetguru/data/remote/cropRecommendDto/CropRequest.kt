package com.example.khetguru.data.remote.cropRecommendDto

data class CropRequest(
    val N: Float,
    val P: Float,
    val K: Float,
    val temperature: Float,
    val humidity: Float,
    val ph: Float,
    val rainfall: Float
)
