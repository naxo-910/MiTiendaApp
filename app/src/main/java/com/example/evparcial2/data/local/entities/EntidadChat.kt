package com.example.evparcial2.data.local.entities

import com.example.evparcial2.data.model.TipoMensaje

data class EntidadChat(
    val id: Long = 0,
    val participantesIds: String, // IDs separados por comas
    val productoId: Long,
    val nombreProducto: String,
    val ultimoMensaje: String,
    val fechaUltimoMensaje: Long,
    val fechaCreacion: Long = System.currentTimeMillis()
)

data class EntidadMensaje(
    val id: Long = 0,
    val chatId: Long,
    val emisorId: Long,
    val emisorNombre: String,
    val contenido: String,
    val tipo: String = TipoMensaje.TEXTO.name,
    val fecha: Long = System.currentTimeMillis(),
    val leido: Boolean = false
)