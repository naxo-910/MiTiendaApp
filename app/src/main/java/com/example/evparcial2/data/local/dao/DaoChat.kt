package com.example.evparcial2.data.local.dao

import com.example.evparcial2.data.local.entities.EntidadChat
import com.example.evparcial2.data.local.entities.EntidadMensaje
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DaoChat {
    private val chats = mutableListOf<EntidadChat>()
    private val mensajes = mutableListOf<EntidadMensaje>()

    // Chats de ejemplo
    init {
        chats.addAll(listOf(
            EntidadChat(
                id = 1,
                participantesIds = "1,2", // Admin y Cliente
                productoId = 1L,
                nombreProducto = "Casa moderna",
                ultimoMensaje = "Hola, me interesa la propiedad",
                fechaUltimoMensaje = System.currentTimeMillis() - 3600000 // Hace 1 hora
            )
        ))
        
        mensajes.addAll(listOf(
            EntidadMensaje(
                id = 1,
                chatId = 1,
                emisorId = 2L,
                emisorNombre = "Cliente",
                contenido = "Hola, me interesa la propiedad",
                fecha = System.currentTimeMillis() - 3600000
            ),
            EntidadMensaje(
                id = 2,
                chatId = 1,
                emisorId = 1L,
                emisorNombre = "Admin",
                contenido = "¡Hola! Claro, estaré encantado de ayudarte. ¿Tienes alguna pregunta específica?",
                fecha = System.currentTimeMillis() - 3300000
            )
        ))
    }

    // CRUD para Chats
    suspend fun insertarChat(chat: EntidadChat): Long {
        val nuevoId = (chats.maxByOrNull { it.id }?.id ?: 0) + 1
        chats.add(chat.copy(id = nuevoId))
        return nuevoId
    }

    fun obtenerChatsPorUsuario(usuarioId: Long): Flow<List<EntidadChat>> {
        val chatsUsuario = chats.filter { chat ->
            chat.participantesIds.split(",").map { it.toLong() }.contains(usuarioId)
        }
        return flowOf(chatsUsuario.sortedByDescending { it.fechaUltimoMensaje })
    }

    suspend fun obtenerChatPorProductoYUsuarios(productoId: Long, usuario1Id: Long, usuario2Id: Long): EntidadChat? {
        return chats.find { chat ->
            chat.productoId == productoId && 
            chat.participantesIds.split(",").map { it.toLong() }.toSet() == setOf(usuario1Id, usuario2Id)
        }
    }

    suspend fun actualizarUltimoMensaje(chatId: Long, mensaje: String, fecha: Long) {
        val index = chats.indexOfFirst { it.id == chatId }
        if (index != -1) {
            chats[index] = chats[index].copy(
                ultimoMensaje = mensaje,
                fechaUltimoMensaje = fecha
            )
        }
    }

    // CRUD para Mensajes
    suspend fun insertarMensaje(mensaje: EntidadMensaje): Long {
        val nuevoId = (mensajes.maxByOrNull { it.id }?.id ?: 0) + 1
        mensajes.add(mensaje.copy(id = nuevoId))
        
        // Actualizar último mensaje del chat
        actualizarUltimoMensaje(mensaje.chatId, mensaje.contenido, mensaje.fecha)
        
        return nuevoId
    }

    fun obtenerMensajesPorChat(chatId: Long): Flow<List<EntidadMensaje>> {
        val mensajesChat = mensajes.filter { it.chatId == chatId }
        return flowOf(mensajesChat.sortedBy { it.fecha })
    }

    suspend fun marcarComoLeido(mensajeId: Long) {
        val index = mensajes.indexOfFirst { it.id == mensajeId }
        if (index != -1) {
            mensajes[index] = mensajes[index].copy(leido = true)
        }
    }

    suspend fun marcarTodosChatComoLeido(chatId: Long, usuarioId: Long) {
        mensajes.forEachIndexed { index, mensaje ->
            if (mensaje.chatId == chatId && mensaje.emisorId != usuarioId && !mensaje.leido) {
                mensajes[index] = mensaje.copy(leido = true)
            }
        }
    }
}