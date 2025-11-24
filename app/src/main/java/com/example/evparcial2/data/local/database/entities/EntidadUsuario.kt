package com.example.evparcial2.data.local.entities

data class EntidadUsuario(
    val id: Long = 0,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val rol: String,
    val telefono: String? = null,
    val direccion: String? = null,
    val fechaRegistro: Long = System.currentTimeMillis(),
    val activo: Boolean = true
)