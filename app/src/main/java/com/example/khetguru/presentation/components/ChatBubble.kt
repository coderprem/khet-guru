package com.example.khetguru.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    val bubbleColor = if (isUser) {
        Color(0xFFD1E7DD)
    } else Color(0xFFFFF3CD)
    val alignment = if (isUser) Alignment.End else Alignment.Start

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = message,
            color = Color.Black,
            modifier = Modifier
                .background(bubbleColor, shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
        )
    }
}