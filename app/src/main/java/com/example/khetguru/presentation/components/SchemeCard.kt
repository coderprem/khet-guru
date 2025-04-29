package com.example.khetguru.presentation.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khetguru.domain.model.Scheme
import androidx.core.net.toUri

@Composable
fun SchemeCard(scheme: Scheme) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = scheme.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = scheme.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            // "Learn More" Button with Null Checks and URL Validation
            Button(
                onClick = {
                    if (context != null && scheme.link.startsWith("http")) {
                        val intent = Intent(Intent.ACTION_VIEW, scheme.link.toUri())
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Invalid URL or Context", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier
                    .align(Alignment.End)
                    .height(40.dp)
            ) {
                Text(text = "Learn More", color = Color.White)
            }
        }
    }
}
