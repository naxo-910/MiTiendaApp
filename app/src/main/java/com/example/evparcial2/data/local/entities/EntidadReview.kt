package com.example.evparcial2.data.local.entities

data class EntidadReview(
    val id: Long = 0,
    val productoId: Long,
    val usuarioId: Long,
    val nombreUsuario: String,
    val calificacion: Int,
    val comentario: String,
    val fecha: Long = System.currentTimeMillis(),
    val aprobado: Boolean = true
)