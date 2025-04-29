package com.example.khetguru.data.api

import com.example.khetguru.data.remote.marketPriceDto.MarketPriceDto
import com.example.khetguru.domain.utils.MARKET_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketPriceApi {
    @GET("9ef84268-d588-465a-a308-a864a43d0070")
    suspend fun getMarketPrices(
        @Query("api-key") apiKey: String = MARKET_API_KEY,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 100000,
        @Query("offset") offset: Int = 0
    ): Response<MarketPriceDto>
}