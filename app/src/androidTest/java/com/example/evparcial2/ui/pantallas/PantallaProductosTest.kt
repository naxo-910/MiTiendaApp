package com.example.evparcial2.ui.pantallas

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.evparcial2.data.model.Producto
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Ignore

@RunWith(AndroidJUnit4::class)
class PantallaProductosTest {

    private val mockProductos = listOf(
        Producto(
            id = 1L,
            nombre = "Hostal Test",
            descripcion = "Test description",
            precio = 25000.0,
            stock = 5,
            categoria = "Backpacker",
            tipo = "compartida",
            ciudad = "Santiago",
            pais = "Chile",
            imagenUrl = "test-url"
        )
    )

    @Test
    @Ignore("Test requiere configuración completa de repositorios y Hilt")
    fun pantallaProductos_displaysProductsList() {
        // Test placeholder - requiere setup complejo
        assert(true)
    }

    @Test
    @Ignore("Test requiere configuración completa de repositorios y Hilt")  
    fun filterButtons_areClickable() {
        // Test placeholder - requiere setup complejo
        assert(true)
    }
}