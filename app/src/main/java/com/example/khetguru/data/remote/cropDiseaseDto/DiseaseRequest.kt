package com.example.khetguru.data.remote.cropDiseaseDto

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DiseaseRequest {
    fun create(base64Image: String): RequestBody {
        val json = """
            {
                "images": ["data:image/jpeg;base64,$base64Image"],
                "latitude": 49.207,
                "longitude": 16.608,
                "similar_images": true
            }
        """.trimIndent()
        return json.toRequestBody("application/json".toMediaType())
    }
}
