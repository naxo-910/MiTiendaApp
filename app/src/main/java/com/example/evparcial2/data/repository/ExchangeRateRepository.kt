package com.example.evparcial2.data.repository

import com.example.evparcial2.data.api.ExchangeRateApiService
import com.example.evparcial2.data.model.ConversionResponse
import com.example.evparcial2.data.model.ExchangeRateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepository @Inject constructor(
    private val exchangeRateApiService: ExchangeRateApiService
) {
    
    private val apiKey = "YOUR_FIXER_API_KEY" // Reemplazar con clave real
    
    fun getLatestRates(base: String = "USD", symbols: String? = null): Flow<ApiResult<ExchangeRateResponse>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = exchangeRateApiService.getLatestRates(apiKey, base, symbols)
            if (response.isSuccessful) {
                val data = response.body()
                if (data?.success == true) {
                    emit(ApiResult.Success(data))
                } else {
                    emit(ApiResult.Error("Error en API de tipos de cambio"))
                }
            } else {
                emit(ApiResult.Error("Error al obtener tipos de cambio", response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Error desconocido"))
        }
    }
    
    fun convertCurrency(from: String, to: String, amount: Double): Flow<ApiResult<ConversionResponse>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = exchangeRateApiService.convertCurrency(apiKey, from, to, amount)
            if (response.isSuccessful) {
                val data = response.body()
                if (data?.success == true) {
                    emit(ApiResult.Success(data))
                } else {
                    emit(ApiResult.Error("Error en conversión de moneda"))
                }
            } else {
                emit(ApiResult.Error("Error al convertir moneda", response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Error desconocido"))
        }
    }
    
    // Función de utilidad para conversión USD -> CLP
    fun convertUsdToClp(amountUsd: Double): Flow<ApiResult<Double>> = flow {
        convertCurrency("USD", "CLP", amountUsd).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    emit(ApiResult.Success(result.data.result))
                }
                is ApiResult.Error -> emit(result)
                is ApiResult.Loading -> emit(result)
            }
        }
    }
}