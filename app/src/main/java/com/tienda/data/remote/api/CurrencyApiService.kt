package com.tienda.data.remote.api

import com.tienda.data.remote.dto.CurrencyResponse
import com.tienda.data.remote.dto.ConversionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApiService {
    
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") accessKey: String,
        @Query("base") base: String = "USD",
        @Query("symbols") symbols: String? = null
    ): CurrencyResponse
    
    @GET("convert")
    suspend fun convertCurrency(
        @Query("access_key") accessKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): ConversionResponse
    
    @GET("symbols")
    suspend fun getSupportedSymbols(
        @Query("access_key") accessKey: String
    ): CurrencyResponse
}