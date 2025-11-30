package com.example.evparcial2.data.model

import com.google.common.collect.Range
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProductoTest {

    @Test
    fun `debe crear producto con todos los campos`() {
        val producto = Producto(
            id = 1L,
            nombre = "Habitación Deluxe",
            descripcion = "Habitación espaciosa con vista al mar",
            precio = 150.0,
            stock = 5,
            categoria = "hotel",
            tipo = "deluxe",
            ciudad = "Barcelona",
            pais = "España",
            imagenUrl = "https://ejemplo.com/imagen.jpg"
        )

        assertThat(producto.id).isEqualTo(1L)
        assertThat(producto.nombre).isEqualTo("Habitación Deluxe")
        assertThat(producto.descripcion).isEqualTo("Habitación espaciosa con vista al mar")
        assertThat(producto.precio).isEqualTo(150.0)
        assertThat(producto.stock).isEqualTo(5)
        assertThat(producto.categoria).isEqualTo("hotel")
        assertThat(producto.tipo).isEqualTo("deluxe")
        assertThat(producto.ciudad).isEqualTo("Barcelona")
        assertThat(producto.pais).isEqualTo("España")
        assertThat(producto.imagenUrl).isEqualTo("https://ejemplo.com/imagen.jpg")
    }

    @Test
    fun `debe crear producto con campos opcionales por defecto`() {
        val producto = Producto(
            id = 1L,
            nombre = "Habitación Básica",
            descripcion = "Habitación sencilla",
            precio = 80.0,
            stock = 3,
            categoria = "hostel",
            tipo = "básica"
        )

        assertThat(producto.ciudad).isEqualTo("")
        assertThat(producto.pais).isEqualTo("")
        assertThat(producto.imagenUrl).isNull()
        assertThat(producto.promedioCalificacion).isEqualTo(4.5f)
        assertThat(producto.totalReviews).isEqualTo(0)
    }

    @Test
    fun `dos productos con mismos datos deben ser iguales`() {
        val producto1 = Producto(
            id = 1L,
            nombre = "Suite",
            descripcion = "Suite presidencial",
            precio = 300.0,
            stock = 1,
            categoria = "hotel",
            tipo = "suite"
        )

        val producto2 = Producto(
            id = 1L,
            nombre = "Suite",
            descripcion = "Suite presidencial",
            precio = 300.0,
            stock = 1,
            categoria = "hotel",
            tipo = "suite"
        )

        assertThat(producto1).isEqualTo(producto2)
        assertThat(producto1.hashCode()).isEqualTo(producto2.hashCode())
    }

    @Test
    fun `productos con diferentes precios no deben ser iguales`() {
        val producto1 = Producto(
            id = 1L,
            nombre = "Habitación",
            descripcion = "Descripción",
            precio = 100.0,
            stock = 2,
            categoria = "hotel",
            tipo = "estándar"
        )

        val producto2 = producto1.copy(precio = 150.0)

        assertThat(producto1).isNotEqualTo(producto2)
    }

    @Test
    fun `toString debe contener información del producto`() {
        val producto = Producto(
            id = 1L,
            nombre = "Test Room",
            descripcion = "Test Description",
            precio = 99.99,
            stock = 4,
            categoria = "test",
            tipo = "test"
        )

        val toString = producto.toString()
        assertThat(toString).contains("1")
        assertThat(toString).contains("Test Room")
        assertThat(toString).contains("99.99")
        assertThat(toString).contains("4")
    }

    @Test
    fun `copy debe crear nueva instancia con campos modificados`() {
        val productoOriginal = Producto(
            id = 1L,
            nombre = "Original",
            descripcion = "Descripción original",
            precio = 100.0,
            stock = 5,
            categoria = "hotel",
            tipo = "estándar"
        )

        val productoModificado = productoOriginal.copy(
            precio = 120.0,
            stock = 3
        )

        assertThat(productoModificado.id).isEqualTo(productoOriginal.id)
        assertThat(productoModificado.nombre).isEqualTo(productoOriginal.nombre)
        assertThat(productoModificado.precio).isEqualTo(120.0)
        assertThat(productoModificado.stock).isEqualTo(3)
    }

    @Test
    fun `debe validar precio válido`() {
        val producto = Producto(
            id = 1L,
            nombre = "Habitación",
            descripcion = "Descripción",
            precio = 75.50,
            stock = 2,
            categoria = "hostel",
            tipo = "compartida"
        )

        assertThat(producto.precio).isGreaterThan(0.0)
    }

    @Test
    fun `debe validar stock válido`() {
        val producto = Producto(
            id = 1L,
            nombre = "Habitación",
            descripcion = "Descripción",
            precio = 100.0,
            stock = 10,
            categoria = "hotel",
            tipo = "doble"
        )

        assertThat(producto.stock).isAtLeast(0)
    }

    @Test
    fun `debe manejar calificaciones correctamente`() {
        val producto = Producto(
            id = 1L,
            nombre = "Habitación",
            descripcion = "Descripción",
            precio = 100.0,
            stock = 2,
            categoria = "hotel",
            tipo = "estándar",
            promedioCalificacion = 4.8f,
            totalReviews = 25
        )

        assertThat(producto.promedioCalificacion).isEqualTo(4.8f)
        assertThat(producto.totalReviews).isEqualTo(25)
        assertThat(producto.promedioCalificacion).isIn(Range.closed(0f, 5f))
    }
}