package com.example.evparcial2.data.model

data class Review(
    val id: Long = 0,
    val productoId: Long,
    val usuarioId: Long,
    val nombreUsuario: String,
    val calificacion: Int, // 1-5 estrellas
    val comentario: String,
    val fecha: Long = System.currentTimeMillis(),
    val aprobado: Boolean = true // Para moderación
)

data class CalificacionResumen(
    val promedioCalificacion: Float,
    val totalReviews: Int,
    val distribucionEstrellas: Map<Int, Int> // Cuántas reviews por cada estrella
)