package com.example.khetguru.domain.usecase

import com.example.khetguru.domain.repository.MarketPriceRepository
import javax.inject.Inject

class GetMarketPriceUseCase @Inject constructor(
    private val repository: MarketPriceRepository
) {
    suspend operator fun invoke(apiKey: String, format: String, limit: Int, offset: Int) =
        repository.getMarketPrices(apiKey, format, limit, offset)
}