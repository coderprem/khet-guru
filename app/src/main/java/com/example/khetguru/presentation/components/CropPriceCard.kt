package com.example.khetguru.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.khetguru.data.remote.marketPriceDto.Record

@Composable
fun CropPriceCard(
    record: Record,
    index: Int,
    onSaveClick: (Record) -> Unit = {}
) {
    val backgroundColors = listOf(Color(0xFFFFCDD2), Color(0xFFC8E6C9), Color(0xFFBBDEFB))
    val cardColor = backgroundColors[index % backgroundColors.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                // Title: Commodity Name and Market
                Text(
                    text = "${record.commodity} - ${record.market}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // State and District
                Text(text = "State: ${record.state}, District: ${record.district}")

                Spacer(modifier = Modifier.height(4.dp))

                // Modal Price
                Text(
                    text = "Price: ₹${record.modal_price}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            // Save Icon in Top-Right Corner
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Save",
                tint = Color.Black.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onSaveClick(record) }
            )
        }
    }
}
