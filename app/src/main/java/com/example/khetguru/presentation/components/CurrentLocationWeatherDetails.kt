package com.example.khetguru.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.khetguru.data.remote.currentLocationDto.CurrentLocationDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentLocationWeatherDetails(data: CurrentLocationDto) {
    val weather = data.weather.first()
    val main = data.main
    val wind = data.wind
    val sys = data.sys

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Add scroll here if needed
    ) {
        // Location and Weather Title
        Text(
            text = "${data.name}, ${data.coord.lat}, ${data.coord.lon}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = weather.main,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Temperature
        Text(
            text = "${String.format("%.1f", main.temp - 273.15)}°C", // Round to 1 decimal place
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = weather.description,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wind
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Air, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Wind Speed: ${wind.speed} m/s",
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Humidity and Pressure
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Humidity: ${main.humidity}%",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Pressure: ${main.pressure} hPa",
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sunrise and Sunset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sunrise: ${formatTime(sys.sunrise)}",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Sunset: ${formatTime(sys.sunset)}",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

// Helper function to format the time (sunrise and sunset)
fun formatTime(timestamp: Int): String {
    val date = Date(timestamp * 1000L)
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(date)
}
