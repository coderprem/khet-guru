package com.example.khetguru.presentation.screens

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.khetguru.R
import androidx.core.net.toUri

@Composable
fun ExpertAdvisoryScreen(modifier: Modifier) {
    val chatbotUrl =
        "https://cdn.botpress.cloud/webchat/v2.4/shareable.html?configUrl=https://files.bpcontent.cloud/2025/04/29/13/20250429131648-4PSLIOLZ.json"
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE8F5E9), Color(0xFFD0F0C0), Color(0xFFF9FFF9))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Top image section
            Image(
                painter = painterResource(id = R.drawable.farm1),
                contentDescription = "Farming Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(4.dp, RoundedCornerShape(24.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your Farming Companion",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Access real-time expert guidance for healthier crops and higher yields. Our intelligent chatbot helps you with:",
                fontSize = 16.sp,
                color = Color(0xFF4E342E),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Features List
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                listOf(
                    "Crop Disease Diagnosis 🌾",
                    "Pest Management Solutions 🐛",
                    "Soil Health Tips 🌱",
                    "Weather Updates ☀️🌧️",
                    "Organic & Chemical Treatments 🧪"
                ).forEach {
                    Text(
                        text = "• $it",
                        fontSize = 15.sp,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Get personalized advice tailored to your farm's needs — anytime, anywhere!",
                fontSize = 15.sp,
                color = Color(0xFF4E342E),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CTA Button
            Button(
                onClick = {
                    val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(Color(0xFF2E7D32).hashCode())
                        .build()
                    customTabsIntent.launchUrl(context, chatbotUrl.toUri())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.buttonElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Start Chatting Now",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Invisible WebView for preloading if needed
        AnimatedVisibility(visible = false) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(chatbotUrl)
                    }
                },
                modifier = Modifier.size(0.dp)
            )
        }
    }
}