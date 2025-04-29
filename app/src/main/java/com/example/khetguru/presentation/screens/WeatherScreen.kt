package com.example.khetguru.presentation.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.khetguru.data.api.NetworkResponse
import com.example.khetguru.presentation.components.CurrentLocationWeatherDetails
import com.example.khetguru.presentation.components.WeatherDetails
import com.example.khetguru.presentation.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    // Collecting state for weather and location data
    val cityWeatherResult by weatherViewModel.cityWeatherResult.collectAsState()
    val currentLocationWeatherResult by weatherViewModel.currentLocationWeatherResult.collectAsState()
    val errorMessage by weatherViewModel.errorMessage.collectAsState()
    val isLocationEnabled by weatherViewModel.isLocationEnabled.collectAsState()

    // Permission launcher for location access
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (locationGranted) {
            weatherViewModel.getCurrentLocationWeather()
        } else {
            weatherViewModel.updateErrorMessage("Location permissions not granted")
        }
    }

    // Launch the permissions check when the screen is first loaded
    LaunchedEffect(Unit) {
        weatherViewModel.checkLocationEnabled()
        if (!weatherViewModel.checkLocationPermission()) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            weatherViewModel.getCurrentLocationWeather()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF9FD2FC), Color(0xFF5EAFEF)) // Soft blue gradient sky
                )
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- Search Field ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isEmpty()) {
                        weatherViewModel.clearCityWeatherResult()
                    }
                },
                placeholder = { Text("Enter city name") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp), // Rounded border
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search // Set IME action as Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotEmpty()) {
                            weatherViewModel.getLocationData(searchQuery) // Trigger search action
                        }
                    }
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchQuery.isNotEmpty()) {
                                weatherViewModel.getLocationData(searchQuery) // Trigger search action
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Errors and Location Warnings ---
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!isLocationEnabled) {
                Text(
                    text = "Location services are turned off!",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Display Weather Based on searchQuery ---
            when {
                searchQuery.isNotEmpty() -> {
                    when (cityWeatherResult) {
                        is NetworkResponse.Loading -> LoadingIndicator()
                        is NetworkResponse.Success -> WeatherDetails((cityWeatherResult as NetworkResponse.Success).data)
                        is NetworkResponse.Error -> ErrorText((cityWeatherResult as NetworkResponse.Error).message)
                        NetworkResponse.Empty -> {} // Don't show anything
                    }
                }

                else -> {
                    when (currentLocationWeatherResult) {
                        is NetworkResponse.Loading -> LoadingIndicator()
                        is NetworkResponse.Success -> CurrentLocationWeatherDetails((currentLocationWeatherResult as NetworkResponse.Success).data)
                        is NetworkResponse.Error -> ErrorText((currentLocationWeatherResult as NetworkResponse.Error).message)
                        NetworkResponse.Empty -> {} // Don't show anything
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(modifier = Modifier.padding(30.dp))
}

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = Color.Red,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}
