package com.example.evparcial2.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ItemCarritoTest {

    private val productoEjemplo = Producto(
        id = 1L,
        nombre = "Habitación Test",
        descripcion = "Descripción de prueba",
        precio = 100.0,
        stock = 5,
        categoria = "hotel",
        tipo = "estándar"
    )

    @Test
    fun `debe crear ItemCarrito correctamente`() {
        val item = ItemCarrito(
            producto = productoEjemplo,
            cantidad = 2
        )

        assertThat(item.producto).isEqualTo(productoEjemplo)
        assertThat(item.cantidad).isEqualTo(2)
    }

    @Test
    fun `debe calcular subtotal correctamente`() {
        val item = ItemCarrito(
            producto = productoEjemplo,
            cantidad = 3
        )

        val subtotalEsperado = 100.0 * 3
        assertThat(item.subtotal).isEqualTo(subtotalEsperado)
    }

    @Test
    fun `subtotal debe ser cero para cantidad cero`() {
        val item = ItemCarrito(
            producto = productoEjemplo,
            cantidad = 0
        )

        assertThat(item.subtotal).isEqualTo(0.0)
    }

    @Test
    fun `subtotal debe manejar precios decimales`() {
        val productoDecimal = productoEjemplo.copy(precio = 99.99)
        val item = ItemCarrito(
            producto = productoDecimal,
            cantidad = 2
        )

        val subtotalEsperado = 99.99 * 2
        assertThat(item.subtotal).isEqualTo(subtotalEsperado)
    }

    @Test
    fun `debe manejar cantidad 1`() {
        val item = ItemCarrito(
            producto = productoEjemplo,
            cantidad = 1
        )

        assertThat(item.subtotal).isEqualTo(productoEjemplo.precio)
    }

    @Test
    fun `dos items con mismo producto y cantidad deben ser iguales`() {
        val item1 = ItemCarrito(productoEjemplo, 2)
        val item2 = ItemCarrito(productoEjemplo, 2)

        assertThat(item1).isEqualTo(item2)
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode())
    }

    @Test
    fun `items con diferente cantidad no deben ser iguales`() {
        val item1 = ItemCarrito(productoEjemplo, 2)
        val item2 = ItemCarrito(productoEjemplo, 3)

        assertThat(item1).isNotEqualTo(item2)
    }

    @Test
    fun `copy debe funcionar correctamente`() {
        val itemOriginal = ItemCarrito(productoEjemplo, 2)
        val itemCopiado = itemOriginal.copy(cantidad = 5)

        assertThat(itemCopiado.producto).isEqualTo(itemOriginal.producto)
        assertThat(itemCopiado.cantidad).isEqualTo(5)
        assertThat(itemCopiado.subtotal).isEqualTo(500.0)
    }

    @Test
    fun `toString debe contener información del item`() {
        val item = ItemCarrito(productoEjemplo, 3)
        val toString = item.toString()

        assertThat(toString).contains("3")
        assertThat(toString).contains("Habitación Test")
    }

    @Test
    fun `subtotal debe recalcularse al cambiar cantidad`() {
        val item = ItemCarrito(productoEjemplo, 2)
        assertThat(item.subtotal).isEqualTo(200.0)

        val itemModificado = item.copy(cantidad = 4)
        assertThat(itemModificado.subtotal).isEqualTo(400.0)
    }
}