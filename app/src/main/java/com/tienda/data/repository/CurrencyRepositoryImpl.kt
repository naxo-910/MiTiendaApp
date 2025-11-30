package com.tienda.data.repository

import com.tienda.data.remote.api.CurrencyApiService
import com.tienda.data.remote.dto.ConversionResponse
import com.tienda.data.remote.dto.CurrencyResponse
import com.tienda.domain.model.Currency
import com.tienda.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApiService: CurrencyApiService
) : CurrencyRepository {
    
    companion object {
        private const val API_KEY = "YOUR_API_KEY_HERE" // En un proyecto real, esto estaría en BuildConfig o archivo seguro
    }
    
    override suspend fun getSupportedCurrencies(): Result<List<Currency>> {
        return withContext(Dispatchers.IO) {
            try {
                // Por ahora retornamos las monedas más comunes
                // En un proyecto real, esto vendría del endpoint de symbols
                val currencies = listOf(
                    Currency("USD", "United States Dollar"),
                    Currency("EUR", "Euro"),
                    Currency("GBP", "British Pound Sterling"),
                    Currency("JPY", "Japanese Yen"),
                    Currency("AUD", "Australian Dollar"),
                    Currency("CAD", "Canadian Dollar"),
                    Currency("CHF", "Swiss Franc"),
                    Currency("CNY", "Chinese Yuan"),
                    Currency("SEK", "Swedish Krona"),
                    Currency("NZD", "New Zealand Dollar"),
                    Currency("MXN", "Mexican Peso"),
                    Currency("SGD", "Singapore Dollar"),
                    Currency("HKD", "Hong Kong Dollar"),
                    Currency("NOK", "Norwegian Krone"),
                    Currency("BRL", "Brazilian Real"),
                    Currency("INR", "Indian Rupee"),
                    Currency("KRW", "South Korean Won"),
                    Currency("TRY", "Turkish Lira"),
                    Currency("RUB", "Russian Ruble"),
                    Currency("ZAR", "South African Rand")
                )
                
                Result.success(currencies)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun getLatestRates(base: String): Result<CurrencyResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = currencyApiService.getLatestRates(API_KEY, base)
                if (response.success) {
                    Result.success(response)
                } else {
                    Result.failure(Exception("API call failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun convertCurrency(from: String, to: String, amount: Double): Result<ConversionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Simulamos una conversión exitosa para demo
                // En un proyecto real, esto haría la llamada real a la API
                simulateConversion(from, to, amount)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    private fun simulateConversion(from: String, to: String, amount: Double): Result<ConversionResponse> {
        // Tasas simuladas para demo
        val exchangeRates = mapOf(
            "USD-EUR" to 0.85,
            "EUR-USD" to 1.18,
            "USD-GBP" to 0.73,
            "GBP-USD" to 1.37,
            "USD-JPY" to 110.0,
            "JPY-USD" to 0.009,
            "EUR-GBP" to 0.86,
            "GBP-EUR" to 1.16,
            "USD-MXN" to 20.5,
            "MXN-USD" to 0.049,
            "EUR-MXN" to 24.1,
            "MXN-EUR" to 0.041
        )
        
        val rateKey = "$from-$to"
        val rate = exchangeRates[rateKey] ?: 1.0
        val convertedAmount = amount * rate
        
        val response = ConversionResponse(
            success = true,
            query = com.tienda.data.remote.dto.ConversionQuery(
                from = from,
                to = to,
                amount = amount
            ),
            info = com.tienda.data.remote.dto.ConversionInfo(
                timestamp = System.currentTimeMillis() / 1000,
                rate = rate
            ),
            historical = false,
            date = "2024-01-01",
            result = convertedAmount
        )
        
        return Result.success(response)
    }
}