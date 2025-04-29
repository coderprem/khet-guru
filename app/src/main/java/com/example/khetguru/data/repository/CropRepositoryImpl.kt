package com.example.khetguru.data.repository

import com.example.khetguru.data.remote.marketPriceDto.Record
import com.example.khetguru.domain.repository.CropRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CropRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CropRepository {

    private fun generateCropId(crop: Record): String {
        return "${crop.commodity}_${crop.market}_${crop.state}_${crop.district}".replace(" ", "_")
    }

    override suspend fun saveCrop(userId: String, crop: Record): Boolean {
        return try {
            // save to firebase
            val cropId = generateCropId(crop)
            val cropRef = firestore.collection("users")
                .document(userId)
                .collection("crops")
                .document(cropId)

            cropRef.set(crop).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isCropExist(userId: String, crop: Record): Boolean {
        return try {
            val cropId = generateCropId(crop)
            val cropRef = firestore.collection("users")
                .document(userId)
                .collection("crops")
                .document(cropId)

            val documentSnapshot = cropRef.get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getSavedCrops(userId: String): List<Record> {
        return try {
            val cropsRef = firestore.collection("users")
                .document(userId)
                .collection("crops")

            val querySnapshot = cropsRef.get().await()
            querySnapshot.toObjects(Record::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun deleteCrop(userId: String, crop: Record): Boolean {
        return try {
            val cropId = generateCropId(crop)
            val cropRef = firestore.collection("users")
                .document(userId)
                .collection("crops")
                .document(cropId)

            cropRef.delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
