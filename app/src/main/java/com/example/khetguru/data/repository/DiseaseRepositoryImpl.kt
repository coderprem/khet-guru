package com.example.khetguru.data.repository

import android.util.Base64
import com.example.khetguru.data.api.DiseaseApiService
import com.example.khetguru.data.remote.cropDiseaseDto.DiseaseRequest
import com.example.khetguru.domain.repository.DiseaseRepository
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

class DiseaseRepositoryImpl @Inject constructor(
    private val apiService: DiseaseApiService
) : DiseaseRepository {

    override suspend fun identifyDisease(imageFile: File): Result<String> {
        return try {
            val base64Image = Base64.encodeToString(imageFile.readBytes(), Base64.NO_WRAP)
            val requestBody = DiseaseRequest.create(base64Image)
            val response = apiService.identifyDisease(requestBody)

            if (response.isSuccessful) {
                Result.success(formatApiResponse(response.body()?.string().orEmpty()))
            } else {
                Result.failure(Exception("Server Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun formatApiResponse(response: String): String {
        return try {
            val jsonObject = JSONObject(response)
            val resultObject = jsonObject.optJSONObject("result")

            // Extract plant details
            val cropSuggestion = resultObject?.optJSONObject("crop")?.optJSONArray("suggestions")
            val plantName =
                cropSuggestion?.optJSONObject(0)?.optString("name", "Unknown Plant") ?: "Unknown Plant"
            val scientificName =
                cropSuggestion?.optJSONObject(0)?.optString("scientific_name", "N/A") ?: "N/A"

            // Extract disease details
            val diseaseArray = resultObject?.optJSONObject("disease")?.optJSONArray("suggestions")
            val diseaseInfoList = mutableListOf<String>()

            if (diseaseArray != null && diseaseArray.length() > 0) {
                for (i in 0 until diseaseArray.length()) {
                    val disease = diseaseArray.optJSONObject(i)
                    val diseaseName = disease?.optString("name", "Unknown Disease") ?: "Unknown Disease"
                    val probability = disease?.optDouble("probability", 0.0)?.times(100)?.toInt() ?: 0
                    val scientificDiseaseName = disease?.optString("scientific_name", "N/A") ?: "N/A"

                    val preventionList = disease?.optJSONObject("details")?.optJSONObject("treatment")
                        ?.optJSONArray("prevention")
                    val prevention = preventionList?.let {
                        if (it.length() > 0) "\n\uD83D\uDD34 \uD835\uDDE3\uD835\uDDE5\uD835\uDDD8\uD835\uDDE9\uD835\uDDD8\uD835\uDDE1\uD835\uDDE7\uD835\uDDDC\uD835\uDDE2\uD835\uDDE1: \n\uD83D\uDD39 " + it.optString(
                            0
                        ) else ""
                    } ?: ""
                    val chemicalTreatmentList =
                        disease?.optJSONObject("details")?.optJSONObject("treatment")
                            ?.optJSONArray("chemical treatment")
                    val chemicalTreatment = chemicalTreatmentList?.let {
                        if (it.length() > 0) "\n\uD83E\uDDEA \uD835\uDDD6\uD835\uDDDB\uD835\uDDD8\uD835\uDDE0\uD835\uDDDC\uD835\uDDD6\uD835\uDDD4\uD835\uDDDF \uD835\uDDE7\uD835\uDDE5\uD835\uDDD8\uD835\uDDD4\uD835\uDDE7\uD835\uDDE0\uD835\uDDD8\uD835\uDDE1\uD835\uDDE7: \n\uD83D\uDD39 " + it.optString(
                            0
                        ) else ""
                    } ?: ""

                    val biologicalTreatmentList =
                        disease?.optJSONObject("details")?.optJSONObject("treatment")
                            ?.optJSONArray("biological treatment")
                    val biologicalTreatment = biologicalTreatmentList?.let {
                        if (it.length() > 0) "\n\uD83C\uDF31 \uD835\uDDD5\uD835\uDDDC\uD835\uDDE2\uD835\uDDDF\uD835\uDDE2\uD835\uDDDA\uD835\uDDDC\uD835\uDDD6\uD835\uDDD4\uD835\uDDDF \uD835\uDDE7\uD835\uDDE5\uD835\uDDD8\uD835\uDDD4\uD835\uDDE7\uD835\uDDE0\uD835\uDDD8\uD835\uDDE1\uD835\uDDE7: \n\uD83D\uDD39 " + it.optString(
                            0
                        ) else ""
                    } ?: ""

                    diseaseInfoList.add(
                        "\n" +
                                "\uD83E\uDDA0 \uD835\uDDD7\uD835\uDDDC\uD835\uDDE6\uD835\uDDD8\uD835\uDDD4\uD835\uDDE6\uD835\uDDD8: $diseaseName ($scientificDiseaseName) \n\uD83C\uDFAF \uD835\uDDD6\uD835\uDDE2\uD835\uDDE1\uD835\uDDD9\uD835\uDDDC\uD835\uDDD7\uD835\uDDD8\uD835\uDDE1\uD835\uDDD6\uD835\uDDD8: $probability% \n $prevention$chemicalTreatment$biologicalTreatment"
                    )
                }
            } else {
                diseaseInfoList.add("🦠 🅽🅾 🅳🅸🆂🅴🅰🆂🅴 🅳🅴🆃🅴🅲🆃🅴🅳 - Confidence: *0%*")
            }

            """
        🌱 𝗣𝗟𝗔𝗡𝗧 𝗡𝗔𝗠𝗘: $plantName ($scientificName)
        ${diseaseInfoList.joinToString("\n")}
        """.trimIndent()
        } catch (e: Exception) {
            "❌ 🅴🆁🆁🅾🆁: ${e.localizedMessage}"
        }

    }
}
