package com.example.khetguru.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khetguru.domain.model.Scheme
import com.example.khetguru.presentation.components.SchemeCard

@Composable
fun GovtSchemesScreen(modifier: Modifier = Modifier) {
    val schemes = listOf(
        Scheme(
            "PM-Kisan Samman Nidhi",
            "Financial assistance to farmers with direct income support of ₹6,000 per year.",
            "https://pmkisan.gov.in/"
        ),
        Scheme(
            "Soil Health Card Scheme",
            "Provides farmers with soil health reports to improve crop productivity.",
            "https://soilhealth.dac.gov.in/"
        ),
        Scheme(
            "Pradhan Mantri Fasal Bima Yojana",
            "Insurance coverage for crop failures due to natural calamities.",
            "https://pmfby.gov.in/"
        ),
        Scheme(
            "National Agriculture Market (e-NAM)",
            "Online trading platform for better price discovery and transparency.",
            "https://www.enam.gov.in/"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9)) // Light green background
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Government Schemes",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(schemes) { scheme ->
                SchemeCard(scheme)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}