package com.example.khetguru.data.api

import com.example.khetguru.data.remote.cropRecommendDto.CropRequest
import com.example.khetguru.data.remote.cropRecommendDto.CropResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CropApiService {
    @POST("/predict")
    suspend fun predictCrop(@Body request: CropRequest): Response<CropResponse>
}
