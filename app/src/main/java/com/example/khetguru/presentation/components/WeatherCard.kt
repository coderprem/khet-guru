package com.example.khetguru.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WeatherCard(
    navigateToWeather: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(200.dp)
            .clickable { navigateToWeather() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC)) // Light blue background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                // Weather Title
                Text(
                    text = "Check Weather",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF01579B) // Dark Blue Text
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Current weather text
                Text(
                    text = "Partly Cloudy, 25°C",
                    fontSize = 14.sp,
                    color = Color(0xFF555555) // Grey color for subtext
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Navigation Button
                Button(
                    onClick = navigateToWeather,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text(text = "View Details", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Icon representing weather
            Text(
                text = "⛅🌡️", // Sun and thermometer emojis
                fontSize = 50.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
