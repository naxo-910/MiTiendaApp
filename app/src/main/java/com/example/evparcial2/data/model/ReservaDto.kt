package com.example.evparcial2.data.model

import com.google.gson.annotations.SerializedName

data class ReservaDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("hostel_id")
    val hostelId: String,
    @SerializedName("check_in")
    val checkIn: String,
    @SerializedName("check_out")
    val checkOut: String,
    @SerializedName("guests")
    val huespedes: Int,
    @SerializedName("total_amount")
    val montoTotal: Double,
    @SerializedName("status")
    val estado: String,
    @SerializedName("created_at")
    val fechaCreacion: String
)

// Request para crear reserva
data class CreateReservaRequest(
    @SerializedName("hostel_id")
    val hostelId: String,
    @SerializedName("check_in")
    val checkIn: String,
    @SerializedName("check_out")
    val checkOut: String,
    @SerializedName("guests")
    val huespedes: Int,
    @SerializedName("total_amount")
    val montoTotal: Double
)

// Response para lista de reservas
data class ReservaListResponse(
    @SerializedName("reservations")
    val data: List<ReservaDto>,
    @SerializedName("total")
    val total: Int
)

// Response para reserva individual
data class ReservaResponse(
    @SerializedName("reservation")
    val data: ReservaDto,
    @SerializedName("message")
    val message: String
)