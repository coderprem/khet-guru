package com.example.khetguru.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khetguru.data.remote.marketPriceDto.Record
import com.example.khetguru.domain.usecase.DeleteCropUseCase
import com.example.khetguru.domain.usecase.GetSavedCropsUseCase
import com.example.khetguru.domain.usecase.SaveCropUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropViewModel @Inject constructor(
    private val saveCropUseCase: SaveCropUseCase,
    private val getSavedCropsUseCase: GetSavedCropsUseCase,
    private val deleteCropUseCase: DeleteCropUseCase,
    firebaseAuth: FirebaseAuth
) : ViewModel() {
    val userId = firebaseAuth.currentUser?.uid ?: "anonymous_user"

    private val _state = MutableStateFlow<Boolean?>(null)
    val state: StateFlow<Boolean?> = _state

    private val _savedCrops = MutableStateFlow<List<Record>>(emptyList())
    val savedCrops: StateFlow<List<Record>> = _savedCrops

    fun saveCrop(crop: Record) {
        viewModelScope.launch {
            val isSaved = saveCropUseCase.execute(userId, crop)
            _state.value = isSaved
        }
    }

    fun getSavedCrops() {
        viewModelScope.launch {
            val cropsList = getSavedCropsUseCase(userId)
            _savedCrops.value = cropsList
        }
    }

    fun deleteCrop(crop: Record) {
        viewModelScope.launch {
            val isDeleted = deleteCropUseCase.execute(userId, crop)
            _state.value = isDeleted
            if (isDeleted) {
                getSavedCrops() // refresh after deletion
            }
        }
    }
}
