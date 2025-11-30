package com.example.evparcial2.data.api

import com.example.evparcial2.data.model.ConversionResponse
import com.example.evparcial2.data.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ExchangeRateApiService {
    
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") apiKey: String,
        @Query("base") base: String = "USD",
        @Query("symbols") symbols: String? = null
    ): Response<ExchangeRateResponse>
    
    @GET("convert")
    suspend fun convertCurrency(
        @Query("access_key") apiKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): Response<ConversionResponse>
}