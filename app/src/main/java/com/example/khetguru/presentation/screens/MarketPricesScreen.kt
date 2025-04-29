package com.example.khetguru.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.khetguru.data.api.NetworkResponse
import com.example.khetguru.presentation.components.CropPriceCard
import com.example.khetguru.presentation.viewmodel.CropViewModel
import com.example.khetguru.presentation.viewmodel.MarketPriceViewModel

@Composable
fun MarketPricesScreen(
    modifier: Modifier = Modifier,
    marketPriceViewModel: MarketPriceViewModel,
    cropViewModel: CropViewModel,
    goToSavedPriceScreen: () -> Unit,
) {
    val marketPricesState = marketPriceViewModel.marketPriceResult.collectAsState()
    val filteredPrices = marketPriceViewModel.filteredPrices.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Update the search query in the ViewModel when the user types
    LaunchedEffect(searchQuery) {
        marketPriceViewModel.updateSearchQuery(searchQuery)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Search Crop or Market", fontWeight = FontWeight.Bold) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(16.dp)
            )
            FloatingActionButton(
                onClick = {
                    goToSavedPriceScreen.invoke()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Toggle Bookmark Filter"
                )
            }

        }

        when (val result = marketPricesState.value) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 30.dp)
                ) {
                    itemsIndexed(filteredPrices.value) { index, item ->
                        CropPriceCard(
                            record = item,
                            index = index,
                            onSaveClick = {
                                cropViewModel.saveCrop(item)
                            }
                        )
                    }
                }
            }

            is NetworkResponse.Error -> {
                Text(
                    text = result.message,
                    color = Color.Red
                )
            }

            else -> {
                Text("No data available.")
            }
        }
    }
}
