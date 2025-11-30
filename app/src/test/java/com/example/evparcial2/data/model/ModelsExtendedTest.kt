package com.example.evparcial2.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ReviewTest {

    @Test
    fun `debe crear Review correctamente`() {
        val review = Review(
            id = 1L,
            productoId = 10L,
            usuarioId = 5L,
            nombreUsuario = "Juan Pérez",
            comentario = "Excelente servicio",
            calificacion = 5,
            fechaCreacion = 1640995200000L
        )

        assertThat(review.id).isEqualTo(1L)
        assertThat(review.productoId).isEqualTo(10L)
        assertThat(review.usuarioId).isEqualTo(5L)
        assertThat(review.nombreUsuario).isEqualTo("Juan Pérez")
        assertThat(review.comentario).isEqualTo("Excelente servicio")
        assertThat(review.calificacion).isEqualTo(5)
        assertThat(review.fechaCreacion).isEqualTo(1640995200000L)
    }

    @Test
    fun `debe validar calificación válida`() {
        val review = Review(
            id = 1L,
            productoId = 10L,
            usuarioId = 5L,
            nombreUsuario = "Usuario",
            comentario = "Comentario",
            calificacion = 4,
            fechaCreacion = System.currentTimeMillis()
        )

        assertThat(review.calificacion).isIn(1..5)
    }

    @Test
    fun `dos reviews con mismos datos deben ser iguales`() {
        val review1 = Review(1L, 10L, 5L, "Juan", "Excelente", 5, 1640995200000L)
        val review2 = Review(1L, 10L, 5L, "Juan", "Excelente", 5, 1640995200000L)

        assertThat(review1).isEqualTo(review2)
        assertThat(review1.hashCode()).isEqualTo(review2.hashCode())
    }

    @Test
    fun `copy debe funcionar correctamente`() {
        val reviewOriginal = Review(
            id = 1L,
            productoId = 10L,
            usuarioId = 5L,
            nombreUsuario = "Usuario Original",
            comentario = "Comentario original",
            calificacion = 3,
            fechaCreacion = 1640995200000L
        )

        val reviewModificado = reviewOriginal.copy(
            calificacion = 5,
            comentario = "Comentario actualizado"
        )

        assertThat(reviewModificado.calificacion).isEqualTo(5)
        assertThat(reviewModificado.comentario).isEqualTo("Comentario actualizado")
        assertThat(reviewModificado.id).isEqualTo(reviewOriginal.id)
        assertThat(reviewModificado.usuarioId).isEqualTo(reviewOriginal.usuarioId)
    }
}

class ChatTest {

    @Test
    fun `debe crear Chat correctamente`() {
        val chat = Chat(
            id = 1L,
            usuario1Id = 10L,
            usuario2Id = 20L,
            nombreUsuario1 = "Usuario1",
            nombreUsuario2 = "Usuario2",
            productoId = 100L,
            fechaCreacion = 1640995200000L,
            ultimoMensaje = "Hola",
            fechaUltimoMensaje = 1640995300000L
        )

        assertThat(chat.id).isEqualTo(1L)
        assertThat(chat.usuario1Id).isEqualTo(10L)
        assertThat(chat.usuario2Id).isEqualTo(20L)
        assertThat(chat.nombreUsuario1).isEqualTo("Usuario1")
        assertThat(chat.nombreUsuario2).isEqualTo("Usuario2")
        assertThat(chat.productoId).isEqualTo(100L)
        assertThat(chat.fechaCreacion).isEqualTo(1640995200000L)
        assertThat(chat.ultimoMensaje).isEqualTo("Hola")
        assertThat(chat.fechaUltimoMensaje).isEqualTo(1640995300000L)
    }

    @Test
    fun `debe crear Chat sin producto asociado`() {
        val chat = Chat(
            id = 1L,
            usuario1Id = 10L,
            usuario2Id = 20L,
            nombreUsuario1 = "Usuario1",
            nombreUsuario2 = "Usuario2",
            fechaCreacion = 1640995200000L,
            fechaUltimoMensaje = 1640995300000L
        )

        assertThat(chat.productoId).isNull()
        assertThat(chat.ultimoMensaje).isNull()
    }
}

class MensajeTest {

    @Test
    fun `debe crear Mensaje correctamente`() {
        val mensaje = Mensaje(
            id = 1L,
            chatId = 10L,
            remitenteId = 5L,
            nombreRemitente = "Juan",
            contenido = "Hola, ¿cómo estás?",
            fechaEnvio = 1640995200000L,
            leido = true
        )

        assertThat(mensaje.id).isEqualTo(1L)
        assertThat(mensaje.chatId).isEqualTo(10L)
        assertThat(mensaje.remitenteId).isEqualTo(5L)
        assertThat(mensaje.nombreRemitente).isEqualTo("Juan")
        assertThat(mensaje.contenido).isEqualTo("Hola, ¿cómo estás?")
        assertThat(mensaje.fechaEnvio).isEqualTo(1640995200000L)
        assertThat(mensaje.leido).isTrue()
    }

    @Test
    fun `debe crear Mensaje no leído por defecto`() {
        val mensaje = Mensaje(
            id = 1L,
            chatId = 10L,
            remitenteId = 5L,
            nombreRemitente = "Juan",
            contenido = "Mensaje",
            fechaEnvio = 1640995200000L
        )

        assertThat(mensaje.leido).isFalse()
    }
}

class PedidoTest {

    private val itemEjemplo = ItemCarrito(
        producto = Producto(
            id = 1L,
            nombre = "Habitación",
            descripcion = "Descripción",
            precio = 100.0,
            stock = 5,
            categoria = "hotel",
            tipo = "estándar"
        ),
        cantidad = 2
    )

    @Test
    fun `debe crear Pedido correctamente`() {
        val pedido = Pedido(
            id = 1L,
            usuarioId = 10L,
            nombreUsuario = "Juan Pérez",
            emailUsuario = "juan@email.com",
            items = listOf(itemEjemplo),
            total = 200.0,
            estado = "confirmado",
            fechaCreacion = 1640995200000L,
            fechaActualizacion = 1640995300000L,
            direccionEntrega = "Calle 123",
            metodoPago = "tarjeta"
        )

        assertThat(pedido.id).isEqualTo(1L)
        assertThat(pedido.usuarioId).isEqualTo(10L)
        assertThat(pedido.nombreUsuario).isEqualTo("Juan Pérez")
        assertThat(pedido.emailUsuario).isEqualTo("juan@email.com")
        assertThat(pedido.items).hasSize(1)
        assertThat(pedido.total).isEqualTo(200.0)
        assertThat(pedido.estado).isEqualTo("confirmado")
        assertThat(pedido.direccionEntrega).isEqualTo("Calle 123")
        assertThat(pedido.metodoPago).isEqualTo("tarjeta")
    }

    @Test
    fun `debe manejar pedido con múltiples items`() {
        val item2 = ItemCarrito(
            producto = Producto(
                id = 2L,
                nombre = "Suite",
                descripcion = "Suite de lujo",
                precio = 200.0,
                stock = 2,
                categoria = "hotel",
                tipo = "suite"
            ),
            cantidad = 1
        )

        val pedido = Pedido(
            id = 1L,
            usuarioId = 10L,
            nombreUsuario = "Juan Pérez",
            emailUsuario = "juan@email.com",
            items = listOf(itemEjemplo, item2),
            total = 400.0,
            estado = "confirmado",
            fechaCreacion = 1640995200000L,
            fechaActualizacion = 1640995300000L,
            direccionEntrega = "Calle 123",
            metodoPago = "efectivo"
        )

        assertThat(pedido.items).hasSize(2)
        assertThat(pedido.total).isEqualTo(400.0)
    }
}