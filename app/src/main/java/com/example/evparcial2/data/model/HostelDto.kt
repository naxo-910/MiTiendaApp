package com.example.evparcial2.data.model

import com.google.gson.annotations.SerializedName

data class HostelDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val nombre: String,
    @SerializedName("description")
    val descripcion: String,
    @SerializedName("price")
    val precio: Double,
    @SerializedName("available")
    val disponible: Boolean,
    @SerializedName("room_type")
    val tipoHabitacion: String,
    @SerializedName("city")
    val ciudad: String,
    @SerializedName("country")
    val pais: String,
    @SerializedName("image_url")
    val imagenUrl: String?,
    @SerializedName("rating")
    val rating: Float = 4.5f,
    @SerializedName("reviews_count")
    val totalReviews: Int = 0
)

// Response wrapper para listas
data class HostelListResponse(
    @SerializedName("hostels")
    val data: List<HostelDto>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int
)

// Response wrapper para hostel individual
data class HostelResponse(
    @SerializedName("hostel")
    val data: HostelDto,
    @SerializedName("message")
    val message: String
)