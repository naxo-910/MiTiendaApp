package com.tienda.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>
)

data class ConversionResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("query")
    val query: ConversionQuery,
    @SerializedName("info")
    val info: ConversionInfo,
    @SerializedName("historical")
    val historical: Boolean?,
    @SerializedName("date")
    val date: String,
    @SerializedName("result")
    val result: Double
)

data class ConversionQuery(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("amount")
    val amount: Double
)

data class ConversionInfo(
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("rate")
    val rate: Double
)