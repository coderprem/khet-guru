package com.example.khetguru.data.mapper

import com.example.khetguru.data.remote.marketPriceDto.MarketPriceDto
import com.example.khetguru.data.remote.marketPriceDto.Record

fun MarketPriceDto.toRecordsList(): List<Record> {
    return records.map { record ->
        Record(
            arrival_date = record.arrival_date,
            commodity = record.commodity,
            district = record.district,
            grade = record.grade,
            market = record.market,
            max_price = record.max_price,
            min_price = record.min_price,
            modal_price = record.modal_price,
            state = record.state,
            variety = record.variety
        )
    }
}
