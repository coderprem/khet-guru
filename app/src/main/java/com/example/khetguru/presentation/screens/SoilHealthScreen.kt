package com.example.khetguru.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khetguru.domain.utils.toSafeFloat
import com.example.khetguru.presentation.viewmodel.CropRecommendationViewModel

@Composable
fun SoilHealthScreen(
    modifier: Modifier = Modifier,
    cropRecommendationViewModel: CropRecommendationViewModel
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var nitrogen by remember { mutableStateOf("") }
    var phosphorus by remember { mutableStateOf("") }
    var potassium by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var humidity by remember { mutableStateOf("") }
    var ph by remember { mutableStateOf("") }
    var rainfall by remember { mutableStateOf("") }
    val isLoading by cropRecommendationViewModel.isLoading.collectAsState()

    val crop by cropRecommendationViewModel.crop.collectAsState()

    // Create FocusRequesters for each text field
    val focusRequesters = List(7) { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC8E6C9))
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83C\uDF31Soil Health Analysis\uD83D\uDC1E",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Nitrogen field
            OutlinedTextField(
                value = nitrogen,
                onValueChange = { nitrogen = it },
                label = { Text("N (Nitrogen)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequesters[0]),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[1].requestFocus() }
                )
            )

            // Phosphorus field
            OutlinedTextField(
                value = phosphorus,
                onValueChange = { phosphorus = it },
                label = { Text("P (Phosphorus)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequesters[1]),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[2].requestFocus() }
                )
            )

            // Potassium field
            OutlinedTextField(
                value = potassium,
                onValueChange = { potassium = it },
                label = { Text("K (Potassium)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequesters[2]),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[3].requestFocus() }
                )
            )

            // Temperature field
            OutlinedTextField(
                value = temperature,
                onValueChange = { temperature = it },
                label = { Text("Temperature (°C)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequesters[3]),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[4].requestFocus() }
                )
            )

            // Humidity field
            OutlinedTextField(
                value = humidity,
                onValueChange = { humidity = it },
                label = { Text("Humidity (%)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequesters[4]),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[5].requestFocus() }
                )
            )

            // pH field
            OutlinedTextField(
                value = ph,
                onValueChange = { ph = it },
                label = { Text("pH Level") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequesters[5]),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[6].requestFocus() }
                )
            )

            // Rainfall field (last field - uses Done action)
            OutlinedTextField(
                value = rainfall,
                onValueChange = { rainfall = it },
                label = { Text("Rainfall (mm)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequesters[6]),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val n = nitrogen.toSafeFloat()
                    val p = phosphorus.toSafeFloat()
                    val k = potassium.toSafeFloat()
                    val t = temperature.toSafeFloat()
                    val h = humidity.toSafeFloat()
                    val phValue = ph.toSafeFloat()
                    val r = rainfall.toSafeFloat()

                    if (listOf(n, p, k, t, h, phValue, r).all { it != null }) {
                        cropRecommendationViewModel.getCropRecommendation(
                            n!!, p!!, k!!, t!!, h!!, phValue!!, r!!
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF236027)),
                modifier = Modifier
                    .shadow(10.dp, shape = RoundedCornerShape(20.dp))
                    .height(56.dp)
                    .width(220.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "\uD83D\uDD0D Analyze Soil",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = Color(0xFF1B5E20),
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(48.dp)
                    )
                }

                crop.isNotBlank() -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = crop,
                            modifier = Modifier.padding(20.dp),
                            color = Color(0xFF1B5E20),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                else -> {
                    Spacer(modifier = Modifier.height(0.dp)) // 👈 just a dummy Composable for empty state
                }
            }
        }
    }
}