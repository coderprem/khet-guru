package com.example.khetguru.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khetguru.data.api.NetworkResponse
import com.example.khetguru.data.remote.currentLocationDto.CurrentLocationDto
import com.example.khetguru.data.remote.weatherDto.WeatherDto
import com.example.khetguru.domain.usecase.GetWeatherDataUseCase
import com.example.khetguru.data.service.LocationService
import com.example.khetguru.domain.usecase.GetCurrentLocationWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.android.gms.maps.model.LatLng

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationService: LocationService,
    @ApplicationContext private val context: Context,
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getCurrentLocationWeatherDataUseCase: GetCurrentLocationWeatherDataUseCase
) : ViewModel() {

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLocationEnabled = MutableStateFlow(false)
    val isLocationEnabled: StateFlow<Boolean> = _isLocationEnabled

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val _cityWeatherResult = MutableStateFlow<NetworkResponse<WeatherDto>>(NetworkResponse.Loading)
    val cityWeatherResult: StateFlow<NetworkResponse<WeatherDto>> = _cityWeatherResult

    private val _currentLocationWeatherResult = MutableStateFlow<NetworkResponse<CurrentLocationDto>>(NetworkResponse.Loading)
    val currentLocationWeatherResult: StateFlow<NetworkResponse<CurrentLocationDto>> = _currentLocationWeatherResult

    init {
        getCurrentLocationWeather()
        checkLocationPermission()
        Log.d("WeatherViewModel", "Location permission granted: ${checkLocationPermission()}")
    }

    fun updateErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun clearCityWeatherResult() {
        _cityWeatherResult.value = NetworkResponse.Empty
    }


    fun checkLocationEnabled() {
        _isLocationEnabled.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun getCurrentLocationWeather() {
        if (!checkLocationPermission()) {
            _errorMessage.value = "Location permissions not granted"
            return
        }
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation().await()
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    Log.d("WeatherViewModel", "Current location: ${latLng.latitude}, ${latLng.longitude}")
                    _currentLocation.value = latLng

                    getCurrentLocationData(latLng)
                } else {
                    _errorMessage.value = "Failed to fetch location"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to fetch location: ${e.message}"
            }
        }
    }

    fun getCurrentLocationData(latLng: LatLng) {
        _currentLocationWeatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Fetching weather data for location: ${latLng.latitude}, ${latLng.longitude}")
                val result = getCurrentLocationWeatherDataUseCase(latLng.latitude, latLng.longitude)
                println("✅ Weather API Success for location: ${latLng.latitude}, ${latLng.longitude}")  // Debug
                _currentLocationWeatherResult.value = NetworkResponse.Success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                _currentLocationWeatherResult.value = NetworkResponse.Error("Failed to load weather data: ${e.message}")
                Log.d("WeatherViewModel", "Error fetching weather data: ${e.message}")
            }
        }
    }

    fun getLocationData(city: String) {
        _cityWeatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val result = getWeatherDataUseCase(city)
                _cityWeatherResult.value = NetworkResponse.Success(result)
            } catch (e: Exception) {
                _cityWeatherResult.value = NetworkResponse.Error("Failed to Load Data")
            }
        }
    }
}