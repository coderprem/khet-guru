package com.example.khetguru.data.api

import com.example.khetguru.domain.utils.DISEASE_API_KEY
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DiseaseApiService {
    @POST("api/v1/identification?details=common_names,type,taxonomy,eppo_code,eppo_regulation_status,gbif_id,image,images,wiki_url,wiki_description,treatment,description,symptoms,severity,spreading&language=en\n")
    @Headers("Content-Type: application/json", "Api-Key: $DISEASE_API_KEY")
    suspend fun identifyDisease(@Body request: RequestBody): Response<ResponseBody>
}
