package com.example.evparcial2.data.local.repository

import com.example.evparcial2.data.local.dao.DaoChat
import com.example.evparcial2.data.local.entities.EntidadChat
import com.example.evparcial2.data.local.entities.EntidadMensaje
import com.example.evparcial2.data.model.TipoMensaje
import kotlinx.coroutines.flow.Flow

class RepoChat(private val daoChat: DaoChat = DaoChat()) {

    suspend fun crearOObtenerChat(productoId: Long, nombreProducto: String, usuario1Id: Long, usuario2Id: Long): Long {
        // Intentar obtener chat existente
        val chatExistente = daoChat.obtenerChatPorProductoYUsuarios(productoId, usuario1Id, usuario2Id)
        
        return if (chatExistente != null) {
            chatExistente.id
        } else {
            // Crear nuevo chat
            val nuevoChat = EntidadChat(
                participantesIds = "$usuario1Id,$usuario2Id",
                productoId = productoId,
                nombreProducto = nombreProducto,
                ultimoMensaje = "Chat iniciado",
                fechaUltimoMensaje = System.currentTimeMillis()
            )
            daoChat.insertarChat(nuevoChat)
        }
    }

    fun obtenerChatsPorUsuario(usuarioId: Long): Flow<List<EntidadChat>> {
        return daoChat.obtenerChatsPorUsuario(usuarioId)
    }

    suspend fun enviarMensaje(
        chatId: Long,
        emisorId: Long,
        emisorNombre: String,
        contenido: String,
        tipo: TipoMensaje = TipoMensaje.TEXTO
    ): Long {
        val mensaje = EntidadMensaje(
            chatId = chatId,
            emisorId = emisorId,
            emisorNombre = emisorNombre,
            contenido = contenido,
            tipo = tipo.name
        )
        return daoChat.insertarMensaje(mensaje)
    }

    fun obtenerMensajesPorChat(chatId: Long): Flow<List<EntidadMensaje>> {
        return daoChat.obtenerMensajesPorChat(chatId)
    }

    suspend fun marcarComoLeido(mensajeId: Long) {
        daoChat.marcarComoLeido(mensajeId)
    }

    suspend fun marcarChatComoLeido(chatId: Long, usuarioId: Long) {
        daoChat.marcarTodosChatComoLeido(chatId, usuarioId)
    }
}