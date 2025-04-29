package com.example.khetguru.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.khetguru.presentation.viewmodel.DiseaseViewModel

@Composable
fun DiseaseIdentificationScreen(modifier: Modifier = Modifier, diseaseViewModel: DiseaseViewModel) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val result = diseaseViewModel.result.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Color(0xFFC8E6C9)
            ),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌾Crop Disease Identification🌾",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B5E20),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0x66000000),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                )
            )


            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .shadow(10.dp, shape = CircleShape)
                    .border(2.dp, Color(0xFF2E7D32), shape = CircleShape) // Green border
                    .background(Color.White, shape = CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            )
            {
                if (imageUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Upload Image",
                            tint = Color.Gray,
                            modifier = Modifier.size(70.dp)
                        )
                        Text(
                            text = "Tap to Upload",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Uploaded Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    imageUri?.let { uri ->
                        val file = diseaseViewModel.getFileFromUri(context, uri)
                        file?.let { diseaseViewModel.analyzeImage(it) }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                modifier = Modifier
                    .shadow(12.dp, shape = RoundedCornerShape(16.dp))
                    .height(56.dp)
                    .width(250.dp)
            ) {
                Text(
                    text = "🌾 Identify Disease",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            result.value?.let { predictionResult ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊 Diagnosis Result",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = predictionResult,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }

}