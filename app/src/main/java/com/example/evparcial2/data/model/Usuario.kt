package com.example.evparcial2.data.model

data class Usuario(
    val id: Long, // <-- ¡¡AÑADIDO!!
    val nombre: String,
    val email: String,
    val rol: String,
    val pass: String
)