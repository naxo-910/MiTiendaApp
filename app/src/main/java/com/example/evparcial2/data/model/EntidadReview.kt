package com.example.evparcial2.data.model

data class EntidadReview(
    val id: Long,
    val calificacion: Float,
    val comentario: String,
    val fecha: String,
    val nombreUsuario: String,
    val productoId: Long
)

data class CalificacionResumen(
    val promedioCalificacion: Float,
    val totalReviews: Int
)