package com.example.khetguru.data.repository

import com.example.khetguru.data.api.MarketPriceApi
import com.example.khetguru.data.remote.marketPriceDto.MarketPriceDto
import com.example.khetguru.domain.repository.MarketPriceRepository
import javax.inject.Inject

class MarketPriceRepositoryImpl @Inject constructor(
    private val api: MarketPriceApi
) : MarketPriceRepository {
    override suspend fun getMarketPrices(
        apiKey: String,
        format: String,
        limit: Int,
        offset: Int
    ): MarketPriceDto {
        val response = api.getMarketPrices(apiKey, format, limit, offset)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("API error")
        }
    }
}