package com.tienda.domain.repository

import com.tienda.data.remote.dto.ConversionResponse
import com.tienda.data.remote.dto.CurrencyResponse
import com.tienda.domain.model.Currency

interface CurrencyRepository {
    suspend fun getSupportedCurrencies(): Result<List<Currency>>
    suspend fun getLatestRates(base: String): Result<CurrencyResponse>
    suspend fun convertCurrency(from: String, to: String, amount: Double): Result<ConversionResponse>
}