package com.example.evparcial2.data.repository

import com.example.evparcial2.data.api.HostelApiService
import com.example.evparcial2.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HostelRepository @Inject constructor(
    private val hostelApiService: HostelApiService
) {
    
    fun getAllHostels(): Flow<ApiResult<List<HostelDto>>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = hostelApiService.getAllHostels()
            if (response.isSuccessful) {
                val data = response.body()?.data ?: emptyList()
                emit(ApiResult.Success(data))
            } else {
                emit(ApiResult.Error("😞 Lo sentimos, no pudimos cargar los alojamientos en este momento. Inténtalo de nuevo.", response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Error desconocido"))
        }
    }

    fun getHostelById(id: String): Flow<ApiResult<HostelDto>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = hostelApiService.getHostelById(id)
            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    emit(ApiResult.Success(data))
                } else {
                    emit(ApiResult.Error("🏨 Este alojamiento ya no está disponible. Te sugerimos explorar otras opciones."))
                }
            } else {
                emit(ApiResult.Error("⚠️ Hubo un problema al obtener la información del alojamiento", response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "😞 Problema de conexión al buscar este alojamiento."))
        }
    }

    fun searchHostels(
        country: String? = null,
        city: String? = null,
        roomType: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null
    ): Flow<ApiResult<List<HostelDto>>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = hostelApiService.searchHostels(country, city, roomType, minPrice, maxPrice)
            if (response.isSuccessful) {
                val data = response.body()?.data ?: emptyList()
                emit(ApiResult.Success(data))
            } else {
                emit(ApiResult.Error("🔍 No pudimos completar tu búsqueda. Inténtalo con otros criterios.", response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "😞 Error de conexión durante la búsqueda."))
        }
    }
}

// Repositorios básicos mantienen funcionalidad local
class RepoChat {
    suspend fun obtenerChatsUsuario(usuarioId: Long): List<EntidadChat> {
        kotlinx.coroutines.delay(500)
        return emptyList()
    }
    
    suspend fun obtenerMensajesChat(chatId: Long): List<EntidadMensaje> {
        kotlinx.coroutines.delay(500)
        return emptyList()
    }
    
    suspend fun enviarMensaje(chatId: Long, contenido: String, emisorId: Long) {
        kotlinx.coroutines.delay(200)
    }
}

class RepoPedidos {
    suspend fun obtenerPedidosUsuario(usuarioId: Long): List<EntidadPedido> {
        kotlinx.coroutines.delay(500)
        return emptyList()
    }
    
    suspend fun crearPedido(pedido: EntidadPedido): Long {
        kotlinx.coroutines.delay(300)
        return System.currentTimeMillis()
    }
}

class RepoReviews {
    suspend fun obtenerReviewsProducto(productoId: Long): List<EntidadReview> {
        kotlinx.coroutines.delay(300)
        return emptyList()
    }
    
    suspend fun crearReview(review: EntidadReview): Long {
        kotlinx.coroutines.delay(200)
        return System.currentTimeMillis()
    }
    
    suspend fun obtenerCalificacionResumen(productoId: Long): CalificacionResumen {
        kotlinx.coroutines.delay(200)
        return CalificacionResumen(4.5f, 0)
    }
}

class RepoUsuarios {
    suspend fun obtenerUsuarios(): List<EntidadUsuario> {
        kotlinx.coroutines.delay(500)
        return emptyList()
    }
    
    suspend fun crearUsuario(usuario: EntidadUsuario): Long {
        kotlinx.coroutines.delay(300)
        return System.currentTimeMillis()
    }
    
    suspend fun autenticarUsuario(email: String, contrasena: String): Usuario? {
        kotlinx.coroutines.delay(500)
        return null
    }
    
    suspend fun crearUsuario(nombre: String, email: String, password: String): Usuario {
        kotlinx.coroutines.delay(500)
        // Simular creación de usuario
        return Usuario(
            id = System.currentTimeMillis(),
            nombre = nombre,
            email = email,
            rol = "cliente"
        )
    }
    
    // --- MÉTODOS PARA REVIEWS ---
    suspend fun crearReview(review: Review): Review {
        kotlinx.coroutines.delay(300)
        return review
    }
    
    suspend fun getReviewsPorProducto(productoId: Long): List<Review> {
        kotlinx.coroutines.delay(300)
        // Simular reviews de ejemplo
        return listOf(
            Review(
                id = 1,
                productoId = productoId,
                usuarioId = 1,
                nombreUsuario = "Juan Pérez",
                comentario = "Excelente hostal, muy limpio y cómodo. La atención fue muy buena.",
                calificacion = 5,
                fechaCreacion = System.currentTimeMillis() - 86400000 // 1 día atrás
            ),
            Review(
                id = 2,
                productoId = productoId,
                usuarioId = 2,
                nombreUsuario = "María González",
                comentario = "Buena ubicación y precio justo. Recomendado para viajeros.",
                calificacion = 4,
                fechaCreacion = System.currentTimeMillis() - 172800000 // 2 días atrás
            )
        )
    }
    
    // --- MÉTODOS PARA CHAT ---
    suspend fun crearChat(chat: Chat): Chat {
        kotlinx.coroutines.delay(300)
        return chat
    }
    
    suspend fun getChatsPorUsuario(usuarioId: Long): List<Chat> {
        kotlinx.coroutines.delay(300)
        // Simular chats de ejemplo
        return listOf(
            Chat(
                id = 1,
                usuario1Id = usuarioId,
                usuario2Id = 2,
                nombreUsuario1 = "Tú",
                nombreUsuario2 = "Asistente Virtual",
                productoId = 1,
                fechaCreacion = System.currentTimeMillis() - 3600000, // 1 hora atrás
                ultimoMensaje = "¡Hola! ¿En qué podemos ayudarte hoy?",
                fechaUltimoMensaje = System.currentTimeMillis() - 1800000 // 30 min atrás
            )
        )
    }
    
    suspend fun getChatPorId(chatId: Long): Chat? {
        kotlinx.coroutines.delay(300)
        return Chat(
            id = chatId,
            usuario1Id = 1,
            usuario2Id = 2,
            nombreUsuario1 = "Tú",
            nombreUsuario2 = "Soporte Hostal",
            productoId = 1,
            fechaCreacion = System.currentTimeMillis() - 3600000,
            ultimoMensaje = "Gracias por tu consulta",
            fechaUltimoMensaje = System.currentTimeMillis() - 1800000
        )
    }
    
    suspend fun getMensajesPorChat(chatId: Long): List<Mensaje> {
        kotlinx.coroutines.delay(300)
        // Simular mensajes de ejemplo
        return listOf(
            Mensaje(
                id = 1,
                chatId = chatId,
                remitenteId = 1,
                nombreRemitente = "Tú",
                contenido = "Hola, me interesa este hostal. ¿Tiene disponibilidad?",
                fechaEnvio = System.currentTimeMillis() - 3600000,
                leido = true
            ),
            Mensaje(
                id = 2,
                chatId = chatId,
                remitenteId = 2,
                nombreRemitente = "Soporte Hostal",
                contenido = "Hola! Sí tenemos disponibilidad. ¿Qué fechas necesitas?",
                fechaEnvio = System.currentTimeMillis() - 3000000,
                leido = true
            ),
            Mensaje(
                id = 3,
                chatId = chatId,
                remitenteId = 2,
                nombreRemitente = "Soporte Hostal",
                contenido = "Gracias por tu consulta, estamos para ayudarte.",
                fechaEnvio = System.currentTimeMillis() - 1800000,
                leido = false
            )
        )
    }
    
    suspend fun enviarMensaje(mensaje: Mensaje): Mensaje {
        kotlinx.coroutines.delay(200)
        return mensaje
    }
    
    suspend fun marcarMensajesComoLeidos(chatId: Long, usuarioId: Long) {
        kotlinx.coroutines.delay(200)
        // Simular marcado como leído
    }
    
    // --- MÉTODOS PARA PEDIDOS ---
    suspend fun crearPedido(pedido: Pedido): Pedido {
        kotlinx.coroutines.delay(500)
        return pedido
    }
    
    suspend fun getPedidosPorUsuario(usuarioId: Long): List<Pedido> {
        kotlinx.coroutines.delay(400)
        // Simular pedidos de ejemplo
        return listOf(
            Pedido(
                id = 1,
                usuarioId = usuarioId,
                nombreUsuario = "Usuario Demo",
                emailUsuario = "demo@test.com",
                items = emptyList(), // Se llenaría con items reales
                total = 150000.0,
                estado = "confirmado",
                fechaCreacion = System.currentTimeMillis() - 86400000, // 1 día atrás
                fechaActualizacion = System.currentTimeMillis() - 86400000,
                direccionEntrega = "Dirección de ejemplo 123",
                metodoPago = "Tarjeta de crédito"
            )
        )
    }
    
    suspend fun actualizarEstadoPedido(pedidoId: Long, nuevoEstado: String) {
        kotlinx.coroutines.delay(300)
        // Simular actualización de estado
    }
}

// --- REPOSITORIO COMBINADO PARA VIEWMODELS ---
@Singleton
class BasicRepositories @Inject constructor() {
    
    // --- MÉTODOS PARA USUARIOS ---
    suspend fun autenticarUsuario(email: String, contrasena: String): Usuario? {
        kotlinx.coroutines.delay(500)
        return null
    }
    
    suspend fun crearUsuario(nombre: String, email: String, password: String): Usuario {
        kotlinx.coroutines.delay(500)
        return Usuario(
            id = System.currentTimeMillis(),
            nombre = nombre,
            email = email,
            rol = "cliente"
        )
    }
    
    // --- MÉTODOS PARA REVIEWS ---
    suspend fun crearReview(review: Review): Review {
        kotlinx.coroutines.delay(300)
        return review
    }
    
    suspend fun getReviewsPorProducto(productoId: Long): List<Review> {
        kotlinx.coroutines.delay(300)
        return listOf(
            Review(
                id = 1,
                productoId = productoId,
                usuarioId = 1,
                nombreUsuario = "Juan Pérez",
                comentario = "Excelente hostal, muy limpio y cómodo.",
                calificacion = 5,
                fechaCreacion = System.currentTimeMillis() - 86400000
            ),
            Review(
                id = 2,
                productoId = productoId,
                usuarioId = 2,
                nombreUsuario = "María González",
                comentario = "Buena ubicación y precio justo.",
                calificacion = 4,
                fechaCreacion = System.currentTimeMillis() - 172800000
            )
        )
    }
    
    // --- MÉTODOS PARA CHAT ---
    suspend fun crearChat(chat: Chat): Chat {
        kotlinx.coroutines.delay(300)
        return chat
    }
    
    suspend fun getChatsPorUsuario(usuarioId: Long): List<Chat> {
        kotlinx.coroutines.delay(300)
        return listOf(
            Chat(
                id = 1,
                usuario1Id = usuarioId,
                usuario2Id = 2,
                nombreUsuario1 = "Tú",
                nombreUsuario2 = "Soporte Hostal",
                productoId = 1,
                fechaCreacion = System.currentTimeMillis() - 3600000,
                ultimoMensaje = "Gracias por tu consulta",
                fechaUltimoMensaje = System.currentTimeMillis() - 1800000
            )
        )
    }
    
    suspend fun getChatPorId(chatId: Long): Chat? {
        kotlinx.coroutines.delay(300)
        return Chat(
            id = chatId,
            usuario1Id = 1,
            usuario2Id = 2,
            nombreUsuario1 = "Tú",
            nombreUsuario2 = "Soporte Hostal",
            productoId = 1,
            fechaCreacion = System.currentTimeMillis() - 3600000,
            ultimoMensaje = "Gracias por tu consulta",
            fechaUltimoMensaje = System.currentTimeMillis() - 1800000
        )
    }
    
    suspend fun getMensajesPorChat(chatId: Long): List<Mensaje> {
        kotlinx.coroutines.delay(300)
        return listOf(
            Mensaje(
                id = 1,
                chatId = chatId,
                remitenteId = 1,
                nombreRemitente = "Tú",
                contenido = "Hola, me interesa este hostal.",
                fechaEnvio = System.currentTimeMillis() - 3600000,
                leido = true
            ),
            Mensaje(
                id = 2,
                chatId = chatId,
                remitenteId = 2,
                nombreRemitente = "Soporte Hostal",
                contenido = "Hola! ¿Qué fechas necesitas?",
                fechaEnvio = System.currentTimeMillis() - 3000000,
                leido = true
            )
        )
    }
    
    suspend fun enviarMensaje(mensaje: Mensaje): Mensaje {
        kotlinx.coroutines.delay(200)
        return mensaje
    }
    
    suspend fun marcarMensajesComoLeidos(chatId: Long, usuarioId: Long) {
        kotlinx.coroutines.delay(200)
    }
    
    // --- MÉTODOS PARA PEDIDOS ---
    suspend fun crearPedido(pedido: Pedido): Pedido {
        kotlinx.coroutines.delay(500)
        return pedido
    }
    
    suspend fun getPedidosPorUsuario(usuarioId: Long): List<Pedido> {
        kotlinx.coroutines.delay(400)
        return listOf(
            Pedido(
                id = 1,
                usuarioId = usuarioId,
                nombreUsuario = "Usuario Demo",
                emailUsuario = "demo@test.com",
                items = emptyList(),
                total = 150000.0,
                estado = "confirmado",
                fechaCreacion = System.currentTimeMillis() - 86400000,
                fechaActualizacion = System.currentTimeMillis() - 86400000,
                direccionEntrega = "Dirección de ejemplo 123",
                metodoPago = "Tarjeta de crédito"
            )
        )
    }
    
    suspend fun actualizarEstadoPedido(pedidoId: Long, nuevoEstado: String) {
        kotlinx.coroutines.delay(300)
    }
}