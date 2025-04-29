package com.example.khetguru.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khetguru.domain.usecase.GetCropRecommendationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropRecommendationViewModel @Inject constructor(
    private val getCropRecommendationUseCase: GetCropRecommendationUseCase
) : ViewModel() {
    private val _crop = MutableStateFlow<String>("")
    val crop = _crop

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getCropRecommendation(
        N: Float,
        P: Float,
        K: Float,
        temperature: Float,
        humidity: Float,
        ph: Float,
        rainfall: Float
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val recommendation = getCropRecommendationUseCase(N, P, K, temperature, humidity, ph, rainfall)
            _crop.value = recommendation
            _isLoading.value = false
        }
    }
}