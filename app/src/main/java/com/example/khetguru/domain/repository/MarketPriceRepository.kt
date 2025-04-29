package com.example.khetguru.domain.repository

import com.example.khetguru.data.remote.marketPriceDto.MarketPriceDto

interface MarketPriceRepository {
    suspend fun getMarketPrices(apiKey: String, format: String, limit: Int, offset: Int): MarketPriceDto
}