package com.tienda.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    sealed class CurrencyEvent {
        data class ConvertAmount(val amount: String, val fromCurrency: String, val toCurrency: String) : CurrencyEvent()
        data class UpdateAmount(val amount: String) : CurrencyEvent()
        data class UpdateFromCurrency(val currency: String) : CurrencyEvent()
        data class UpdateToCurrency(val currency: String) : CurrencyEvent()
        object SwapCurrencies : CurrencyEvent()
        object DismissError : CurrencyEvent()
    }

    data class CurrencyUiState(
        val amount: String = "100.00",
        val fromCurrency: String = "USD",
        val toCurrency: String = "ARS",
        val convertedAmount: String = "",
        val exchangeRate: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val supportedCurrencies: List<CurrencyInfo> = getSupportedCurrencies()
    )

    data class CurrencyInfo(
        val code: String,
        val name: String,
        val symbol: String
    )

    fun onEvent(event: CurrencyEvent) {
        when (event) {
            is CurrencyEvent.ConvertAmount -> {
                convertCurrency(event.amount, event.fromCurrency, event.toCurrency)
            }
            is CurrencyEvent.UpdateAmount -> {
                _uiState.value = _uiState.value.copy(amount = event.amount)
            }
            is CurrencyEvent.UpdateFromCurrency -> {
                _uiState.value = _uiState.value.copy(fromCurrency = event.currency)
            }
            is CurrencyEvent.UpdateToCurrency -> {
                _uiState.value = _uiState.value.copy(toCurrency = event.currency)
            }
            is CurrencyEvent.SwapCurrencies -> {
                val current = _uiState.value
                _uiState.value = current.copy(
                    fromCurrency = current.toCurrency,
                    toCurrency = current.fromCurrency
                )
            }
            is CurrencyEvent.DismissError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    private fun convertCurrency(amount: String, fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val amountValue = amount.toDoubleOrNull() ?: 0.0
                if (amountValue <= 0) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Por favor ingresa un monto válido"
                    )
                    return@launch
                }

                // Simular tasas de cambio (en una implementación real, esto vendría de la API)
                val rate = getExchangeRate(fromCurrency, toCurrency)
                val convertedValue = amountValue * rate
                val roundedResult = BigDecimal(convertedValue).setScale(2, RoundingMode.HALF_UP).toDouble()

                _uiState.value = _uiState.value.copy(
                    convertedAmount = String.format("%.2f", roundedResult),
                    exchangeRate = String.format("%.4f", rate),
                    isLoading = false
                )

                Log.d("CurrencyViewModel", "Conversión: $amount $fromCurrency = ${_uiState.value.convertedAmount} $toCurrency (Tasa: $rate)")

            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Error en conversión: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al convertir monedas: ${e.message}"
                )
            }
        }
    }

    // Simulación de tasas de cambio (en una implementación real, esto vendría de una API externa)
    private fun getExchangeRate(from: String, to: String): Double {
        if (from == to) return 1.0

        val rates = mapOf(
            "USD" to mapOf(
                "ARS" to 350.0,
                "EUR" to 0.85,
                "GBP" to 0.73,
                "JPY" to 110.0,
                "BRL" to 5.2,
                "CLP" to 800.0,
                "COP" to 4000.0,
                "PEN" to 3.7,
                "MXN" to 18.5
            ),
            "EUR" to mapOf(
                "USD" to 1.18,
                "ARS" to 412.0,
                "GBP" to 0.86,
                "JPY" to 129.0,
                "BRL" to 6.1
            ),
            "ARS" to mapOf(
                "USD" to 0.0029,
                "EUR" to 0.0024,
                "BRL" to 0.015,
                "CLP" to 2.3,
                "COP" to 11.4
            ),
            "BRL" to mapOf(
                "USD" to 0.19,
                "EUR" to 0.16,
                "ARS" to 67.0,
                "COP" to 770.0
            )
        )

        return rates[from]?.get(to) ?: 1.0
    }

    companion object {
        fun getSupportedCurrencies(): List<CurrencyInfo> = listOf(
            CurrencyInfo("USD", "Dólar Estadounidense", "$"),
            CurrencyInfo("EUR", "Euro", "€"),
            CurrencyInfo("GBP", "Libra Esterlina", "£"),
            CurrencyInfo("JPY", "Yen Japonés", "¥"),
            CurrencyInfo("ARS", "Peso Argentino", "$"),
            CurrencyInfo("BRL", "Real Brasileño", "R$"),
            CurrencyInfo("CLP", "Peso Chileno", "$"),
            CurrencyInfo("COP", "Peso Colombiano", "$"),
            CurrencyInfo("PEN", "Sol Peruano", "S/"),
            CurrencyInfo("MXN", "Peso Mexicano", "$")
        )
    }
}