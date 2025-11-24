package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.local.entities.EntidadChat
import com.example.evparcial2.data.local.entities.EntidadMensaje
import com.example.evparcial2.data.local.repository.RepoChat
import com.example.evparcial2.data.model.TipoMensaje
import com.example.evparcial2.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatListUiState(
    val chats: List<EntidadChat> = emptyList(),
    val isLoading: Boolean = false
)

data class ChatUiState(
    val mensajes: List<EntidadMensaje> = emptyList(),
    val nuevoMensaje: String = "",
    val isLoading: Boolean = false,
    val chatId: Long? = null
)

sealed class ChatEvent {
    data class CargarChats(val usuarioId: Long) : ChatEvent()
    data class IniciarChat(val productoId: Long, val nombreProducto: String, val usuario: Usuario, val otroUsuarioId: Long) : ChatEvent()
    data class CargarMensajes(val chatId: Long) : ChatEvent()
    data class CambiarMensaje(val mensaje: String) : ChatEvent()
    data class EnviarMensaje(val usuario: Usuario) : ChatEvent()
    data class MarcarComoLeido(val chatId: Long, val usuarioId: Long) : ChatEvent()
}

class ViewModelChat : ViewModel() {
    
    private val repoChat = RepoChat()
    
    private val _chatListUiState = MutableStateFlow(ChatListUiState())
    val chatListUiState: StateFlow<ChatListUiState> = _chatListUiState.asStateFlow()
    
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()
    
    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.CargarChats -> {
                cargarChats(event.usuarioId)
            }
            is ChatEvent.IniciarChat -> {
                iniciarChat(event.productoId, event.nombreProducto, event.usuario, event.otroUsuarioId)
            }
            is ChatEvent.CargarMensajes -> {
                cargarMensajes(event.chatId)
            }
            is ChatEvent.CambiarMensaje -> {
                _chatUiState.value = _chatUiState.value.copy(nuevoMensaje = event.mensaje)
            }
            is ChatEvent.EnviarMensaje -> {
                enviarMensaje(event.usuario)
            }
            is ChatEvent.MarcarComoLeido -> {
                marcarComoLeido(event.chatId, event.usuarioId)
            }
        }
    }
    
    private fun cargarChats(usuarioId: Long) {
        viewModelScope.launch {
            _chatListUiState.value = _chatListUiState.value.copy(isLoading = true)
            
            repoChat.obtenerChatsPorUsuario(usuarioId).collect { chats ->
                _chatListUiState.value = _chatListUiState.value.copy(
                    chats = chats,
                    isLoading = false
                )
            }
        }
    }
    
    private fun iniciarChat(productoId: Long, nombreProducto: String, usuario: Usuario, otroUsuarioId: Long) {
        viewModelScope.launch {
            val chatId = repoChat.crearOObtenerChat(productoId, nombreProducto, usuario.id, otroUsuarioId)
            _chatUiState.value = _chatUiState.value.copy(chatId = chatId)
            cargarMensajes(chatId)
        }
    }
    
    private fun cargarMensajes(chatId: Long) {
        viewModelScope.launch {
            _chatUiState.value = _chatUiState.value.copy(isLoading = true, chatId = chatId)
            
            repoChat.obtenerMensajesPorChat(chatId).collect { mensajes ->
                _chatUiState.value = _chatUiState.value.copy(
                    mensajes = mensajes,
                    isLoading = false
                )
            }
        }
    }
    
    private fun enviarMensaje(usuario: Usuario) {
        val estado = _chatUiState.value
        val chatId = estado.chatId ?: return
        val mensaje = estado.nuevoMensaje.trim()
        
        if (mensaje.isBlank()) return
        
        viewModelScope.launch {
            repoChat.enviarMensaje(
                chatId = chatId,
                emisorId = usuario.id,
                emisorNombre = usuario.nombre,
                contenido = mensaje,
                tipo = TipoMensaje.TEXTO
            )
            
            // Limpiar campo de mensaje
            _chatUiState.value = _chatUiState.value.copy(nuevoMensaje = "")
        }
    }
    
    private fun marcarComoLeido(chatId: Long, usuarioId: Long) {
        viewModelScope.launch {
            repoChat.marcarChatComoLeido(chatId, usuarioId)
        }
    }
}