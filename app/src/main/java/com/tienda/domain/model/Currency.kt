package com.tienda.domain.model

data class Currency(
    val code: String,
    val name: String
)

data class CurrencyConversion(
    val amount: Double,
    val fromCurrency: String,
    val toCurrency: String,
    val convertedAmount: Double,
    val exchangeRate: Double,
    val timestamp: Long
)