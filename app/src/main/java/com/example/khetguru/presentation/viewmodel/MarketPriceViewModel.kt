package com.example.khetguru.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khetguru.data.api.NetworkResponse
import com.example.khetguru.data.remote.marketPriceDto.Record
import com.example.khetguru.data.mapper.toRecordsList
import com.example.khetguru.domain.usecase.GetMarketPriceUseCase
import com.example.khetguru.domain.utils.MARKET_API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketPriceViewModel @Inject constructor(
    private val getMarketPriceUseCase: GetMarketPriceUseCase
) : ViewModel() {
    private val _marketPriceResult = MutableStateFlow<NetworkResponse<List<Record>>>(NetworkResponse.Loading)
    val marketPriceResult: StateFlow<NetworkResponse<List<Record>>> = _marketPriceResult

    private val _filteredPrices = MutableStateFlow<List<Record>>(emptyList())
    val filteredPrices: StateFlow<List<Record>> = _filteredPrices

    private val _searchQuery = MutableStateFlow("")

    init {
        fetchAllMarketPrices()
        observeSearchQuery()
    }

    private fun fetchAllMarketPrices() {
        viewModelScope.launch {
            val allRecords = mutableListOf<Record>()
            var offset = 0
            val limit = 1000
            var hasMoreData = true

            try {
                while (hasMoreData) {
                    val response = getMarketPriceUseCase(MARKET_API_KEY, "json", limit, offset)
                    val mappedRecords = response.toRecordsList()

                    if (mappedRecords.isNotEmpty()) {
                        allRecords.addAll(mappedRecords)
                        offset += limit
                    } else {
                        hasMoreData = false
                    }
                }

                _marketPriceResult.value = NetworkResponse.Success(allRecords)
                _filteredPrices.value = allRecords // Set the initial filtered list
            } catch (e: Exception) {
                _marketPriceResult.value = NetworkResponse.Error(e.message ?: "Unknown error")
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.debounce(300) // Debounce to avoid constant filtering
                .collect { query ->
                    filterRecords(query)
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun filterRecords(query: String) {
        val allRecords = (marketPriceResult.value as? NetworkResponse.Success)?.data ?: return
        _filteredPrices.value = allRecords.filter { record ->
            record.commodity.contains(query, ignoreCase = true) ||
                    record.market.contains(query, ignoreCase = true) ||
                    record.state.contains(query, ignoreCase = true) ||
                    record.district.contains(query, ignoreCase = true)
        }
    }
}
