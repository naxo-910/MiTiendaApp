package com.example.evparcial2

import org.junit.Test
import org.junit.Assert.*

/**
 * Test unitario básico para verificar que el framework de testing funciona correctamente
 */
class BasicTest {
    
    @Test
    fun `verificar que las operaciones básicas funcionan`() {
        val resultado = 2 + 2
        assertEquals(4, resultado)
    }
    
    @Test  
    fun `verificar que las cadenas funcionan`() {
        val mensaje = "Hola"
        val saludo = "$mensaje mundo"
        assertEquals("Hola mundo", saludo)
    }
    
    @Test
    fun `verificar que las listas funcionan`() {
        val lista = listOf("Chile", "Argentina", "Perú")
        assertEquals(3, lista.size)
        assertTrue(lista.contains("Chile"))
    }
}