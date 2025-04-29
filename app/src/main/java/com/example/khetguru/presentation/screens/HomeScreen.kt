package com.example.khetguru.presentation.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khetguru.presentation.components.DiseaseIdentificationCard
import com.example.khetguru.presentation.components.ExpertAdvisoryCard
import com.example.khetguru.presentation.components.GovtSchemesCard
import com.example.khetguru.presentation.components.HomePageCard
import com.example.khetguru.presentation.components.MarketPriceCard
import com.example.khetguru.presentation.components.SoilAnalysisCard
import com.example.khetguru.presentation.components.WeatherCard
import com.example.khetguru.presentation.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel,
    navigateToWeather: () -> Unit,
    navigateToSoilHealth: () -> Unit,
    navigateToMarketPrices: () -> Unit,
    navigateToDiseaseIdentification: () -> Unit,
    navigateToExpertAdvisory: () -> Unit,
    navigateToGovtSchemes: () -> Unit
) {
    val currentLocation by weatherViewModel.currentLocation.collectAsState()
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
    // Check if location is enabled
    LaunchedEffect(Unit) {
        weatherViewModel.checkLocationEnabled()
    }

    LaunchedEffect(Unit) {
        weatherViewModel.checkLocationEnabled()
        if (!weatherViewModel.checkLocationPermission()) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83D\uDE9CKhetGuru",
            fontSize = 45.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF388E3C), // Deep Green
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // First Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomePageCard(
                cardColor = Color(0xFFBBDEFB),
                text = "Weather",
                emoji = "\uD83C\uDF24️",
                onClick = navigateToWeather
            )
            HomePageCard(
                cardColor = Color(0xFFDECFCB),
                text = "Soil Health",
                emoji = "\uD83C\uDF31",
                onClick = navigateToSoilHealth
            )
        }

        // Second Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomePageCard(
                cardColor = Color(0xFFC8E6C9),
                text = "Disease Identification",
                emoji = "\uD83E\uDDD1\u200D\uD83C\uDF3E",
                onClick = navigateToDiseaseIdentification
            )
            HomePageCard(
                cardColor = Color(0xFFFDDEE1),
                text = "Market Price",
                emoji = "\uD83D\uDCB0",
                onClick = navigateToMarketPrices
            )
        }

        // Third Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomePageCard(
                cardColor = Color(0xFFA6CDD2),
                text = "Expert Advisory",
                emoji = "\uD83E\uDDD1\u200D\uD83C\uDFEB",
                onClick = navigateToExpertAdvisory
            )
            HomePageCard(
                cardColor = Color(0xFFC7CBCB),
                text = "Govt Schemes",
                emoji = "\uD83C\uDFDB\uFE0F",
                onClick = navigateToGovtSchemes
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        DiseaseIdentificationCard {
            navigateToDiseaseIdentification()
        }

        WeatherCard {
            navigateToWeather()
        }

        SoilAnalysisCard {
            navigateToSoilHealth()
        }

        MarketPriceCard {
            navigateToMarketPrices()
        }

        ExpertAdvisoryCard {
            navigateToExpertAdvisory()
        }

        GovtSchemesCard {
            navigateToGovtSchemes()
        }
    }
}

