package com.example.khetguru.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khetguru.domain.usecase.DiseaseIdentificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class DiseaseViewModel @Inject constructor(
    private val useCase: DiseaseIdentificationUseCase
) : ViewModel() {

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return inputStream?.let {
            val file = File(context.cacheDir, "uploaded_image.jpg")
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()
            file
        }
    }

    fun analyzeImage(file: File) {
        viewModelScope.launch {
            Log.d("DiseaseViewModel", "Analyzing image: ${file.name}")
            _result.value = "🔍 Analyzing..."
            val response = useCase(file)
            _result.value = response.getOrElse { "❌ ${it.message}" }
        }
    }
}
