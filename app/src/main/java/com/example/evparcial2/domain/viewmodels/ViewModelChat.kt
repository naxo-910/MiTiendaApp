package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.model.Chat
import com.example.evparcial2.data.model.Mensaje
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.data.repository.BasicRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- EVENTOS DE CHAT ---
sealed class ChatEvent {
    data class LoadChats(val usuarioId: Long) : ChatEvent()
    data class LoadChat(val chatId: Long) : ChatEvent()
    data class SendMessage(val chatId: Long, val contenido: String, val remitente: Usuario) : ChatEvent()
    data class CreateChat(val usuario1: Usuario, val usuario2: Usuario, val productoId: Long?) : ChatEvent()
    data class OnMessageTextChange(val texto: String) : ChatEvent()
}

// --- UI STATE PARA LISTA DE CHATS ---
data class ChatsListUiState(
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// --- UI STATE PARA CHAT INDIVIDUAL ---
data class ChatUiState(
    val chat: Chat? = null,
    val mensajes: List<Mensaje> = emptyList(),
    val nuevoMensajeTexto: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ViewModelChat @Inject constructor(
    private val repository: BasicRepositories
) : ViewModel() {
    
    private val _chatsListUiState = MutableStateFlow(ChatsListUiState())
    val chatsListUiState: StateFlow<ChatsListUiState> = _chatsListUiState.asStateFlow()
    
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()
    
    fun onEvent(evento: ChatEvent) {
        when (evento) {
            is ChatEvent.LoadChats -> {
                loadChats(evento.usuarioId)
            }
            is ChatEvent.LoadChat -> {
                loadChat(evento.chatId)
            }
            is ChatEvent.SendMessage -> {
                sendMessage(evento.chatId, evento.contenido, evento.remitente)
            }
            is ChatEvent.CreateChat -> {
                createChat(evento.usuario1, evento.usuario2, evento.productoId)
            }
            is ChatEvent.OnMessageTextChange -> {
                _chatUiState.value = _chatUiState.value.copy(
                    nuevoMensajeTexto = evento.texto
                )
            }
        }
    }
    
    private fun loadChats(usuarioId: Long) {
        viewModelScope.launch {
            _chatsListUiState.value = _chatsListUiState.value.copy(isLoading = true)
            
            try {
                val chats = repository.getChatsPorUsuario(usuarioId)
                _chatsListUiState.value = _chatsListUiState.value.copy(
                    chats = chats,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _chatsListUiState.value = _chatsListUiState.value.copy(
                    error = "Error al cargar chats: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    private fun loadChat(chatId: Long) {
        viewModelScope.launch {
            _chatUiState.value = _chatUiState.value.copy(isLoading = true)
            
            try {
                val chat = repository.getChatPorId(chatId)
                val mensajes = repository.getMensajesPorChat(chatId)
                
                _chatUiState.value = _chatUiState.value.copy(
                    chat = chat,
                    mensajes = mensajes,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _chatUiState.value = _chatUiState.value.copy(
                    error = "Error al cargar chat: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    private fun sendMessage(chatId: Long, contenido: String, remitente: Usuario) {
        if (contenido.isBlank()) return
        
        viewModelScope.launch {
            try {
                val mensaje = Mensaje(
                    id = System.currentTimeMillis(),
                    chatId = chatId,
                    remitenteId = remitente.id,
                    nombreRemitente = remitente.nombre,
                    contenido = contenido,
                    fechaEnvio = System.currentTimeMillis(),
                    leido = false
                )
                
                repository.enviarMensaje(mensaje)
                loadChat(chatId)
                
                _chatUiState.value = _chatUiState.value.copy(
                    nuevoMensajeTexto = ""
                )
                
            } catch (e: Exception) {
                _chatUiState.value = _chatUiState.value.copy(
                    error = "Error al enviar mensaje: ${e.message}"
                )
            }
        }
    }
    
    private fun createChat(usuario1: Usuario, usuario2: Usuario, productoId: Long?) {
        viewModelScope.launch {
            try {
                val nuevoChat = Chat(
                    id = System.currentTimeMillis(),
                    usuario1Id = usuario1.id,
                    usuario2Id = usuario2.id,
                    nombreUsuario1 = usuario1.nombre,
                    nombreUsuario2 = usuario2.nombre,
                    productoId = productoId,
                    fechaCreacion = System.currentTimeMillis(),
                    ultimoMensaje = null,
                    fechaUltimoMensaje = System.currentTimeMillis()
                )
                
                repository.crearChat(nuevoChat)
                loadChat(nuevoChat.id)
                
            } catch (e: Exception) {
                _chatUiState.value = _chatUiState.value.copy(
                    error = "Error al crear chat: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _chatsListUiState.value = _chatsListUiState.value.copy(error = null)
        _chatUiState.value = _chatUiState.value.copy(error = null)
    }
    
    fun markMessagesAsRead(chatId: Long, usuarioId: Long) {
        viewModelScope.launch {
            try {
                repository.marcarMensajesComoLeidos(chatId, usuarioId)
                loadChat(chatId)
            } catch (e: Exception) {
                // Error silencioso para esta función
            }
        }
    }
}