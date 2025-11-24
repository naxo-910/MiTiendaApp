package com.example.evparcial2.data.model

sealed class EstadoUI<T> {
    object Cargando : EstadoUI<Nothing>()
    data class Exito<T>(val datos: T) : EstadoUI<T>()
    data class Error(val mensaje: String) : EstadoUI<Nothing>()
}