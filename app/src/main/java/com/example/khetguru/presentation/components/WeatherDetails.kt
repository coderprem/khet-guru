package com.example.khetguru.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.khetguru.R
import com.example.khetguru.data.remote.weatherDto.Hour
import com.example.khetguru.data.remote.weatherDto.WeatherDto

@Composable
fun WeatherDetails(data: WeatherDto) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Weather Icon
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https:${data.current.condition.icon}")  // URL of the image
                    .build(),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
                    .padding(8.dp)
            )

            // Temperature & Location Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "${data.current.temp_c}℃",
                        style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Outlined.LocationOn, contentDescription = "Location", tint = Color(0xFF1565C0))
                        Text(
                            text = "${data.location.name}, ${data.location.country}",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF555555))
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(painter = painterResource(id= R.drawable.baseline_access_time_24), contentDescription = "Time", tint = Color(0xFF1565C0))
                        Text(
                            text = " ${data.location.localtime}",
                            style = TextStyle(fontSize = 16.sp, color = Color(0xFF777777))
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(painter = painterResource(id= R.drawable.baseline_cloud_24), contentDescription = "Weather", tint = Color(0xFF1565C0))
                        Text(
                            text = data.current.condition.text,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF222222))
                        )
                    }
                }
            }

            // Hourly Forecast Section
            Text(
                text = "Hourly Forecast",
                style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Color.White)
            )

            // Horizontal Scrollable Forecast using LazyRow
            LazyRow(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(data.forecast.forecastday.firstOrNull()?.hour.orEmpty()) { hour ->
                    HourlyForecastCard(hour)
                }
            }
        }
    }
}

@Composable
fun HourlyForecastCard(hour: Hour) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .width(140.dp)
            .height(230.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Time with Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(painter = painterResource(id=R.drawable.baseline_schedule_24), contentDescription = "Time", tint = Color(0xFF1565C0))
                Text(
                    text = hour.time.substring(11), // Extracting time only
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                )
            }

            // Weather Icon
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https:${hour.condition.icon}")
                    .build(),
                contentDescription = "Hourly Weather Icon",
                modifier = Modifier.size(70.dp)
            )

            // Temperature with Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(painter = painterResource(id=R.drawable.baseline_thermostat_24), contentDescription = "Temp", tint = Color(0xFF1565C0))
                Text(
                    text = "${hour.temp_c}℃",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                )
            }

            // Weather Condition
            Text(
                text = hour.condition.text,
                style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.DarkGray),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
