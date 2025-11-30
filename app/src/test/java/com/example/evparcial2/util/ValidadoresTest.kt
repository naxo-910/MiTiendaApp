package com.example.evparcial2.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidadoresTest {

    @Test
    fun `validarNombre debe retornar null para nombre válido`() {
        val resultado = Validadores.validarNombre("Juan Pérez")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarNombre debe retornar error para nombre vacío`() {
        val resultado = Validadores.validarNombre("")
        assertThat(resultado).isEqualTo("El nombre es obligatorio")
    }

    @Test
    fun `validarNombre debe retornar error para nombre con solo espacios`() {
        val resultado = Validadores.validarNombre("   ")
        assertThat(resultado).isEqualTo("El nombre es obligatorio")
    }

    @Test
    fun `validarNombre debe retornar error para nombre muy corto`() {
        val resultado = Validadores.validarNombre("Jo")
        assertThat(resultado).isEqualTo("Debe haber mínimo 3 caracteres")
    }

    @Test
    fun `validarNombre debe retornar error para nombre muy largo`() {
        val nombreLargo = "a".repeat(51)
        val resultado = Validadores.validarNombre(nombreLargo)
        assertThat(resultado).isEqualTo("Deben haber máximo 50 caracteres")
    }

    @Test
    fun `validarEmail debe retornar null para email válido`() {
        val resultado = Validadores.validarEmail("usuario@ejemplo.com")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarEmail debe retornar error para email vacío`() {
        val resultado = Validadores.validarEmail("")
        assertThat(resultado).isEqualTo("El email es obligatorio")
    }

    @Test
    fun `validarEmail debe retornar error para email inválido`() {
        val resultado = Validadores.validarEmail("email_invalido")
        assertThat(resultado).isEqualTo("Email inválido")
    }

    @Test
    fun `validarPassword debe retornar null para contraseña válida`() {
        val resultado = Validadores.validarPassword("password123")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarPassword debe retornar error para contraseña vacía`() {
        val resultado = Validadores.validarPassword("")
        assertThat(resultado).isEqualTo("La contraseña es obligatoria")
    }

    @Test
    fun `validarPassword debe retornar error para contraseña muy corta`() {
        val resultado = Validadores.validarPassword("12345")
        assertThat(resultado).isEqualTo("La contraseña debe tener al menos 6 caracteres")
    }

    @Test
    fun `validarPassword debe retornar error para contraseña muy larga`() {
        val passwordLargo = "a".repeat(51)
        val resultado = Validadores.validarPassword(passwordLargo)
        assertThat(resultado).isEqualTo("La contraseña no puede exceder 50 caracteres")
    }

    @Test
    fun `validarPrecio debe retornar null para precio válido`() {
        val resultado = Validadores.validarPrecio("10.50")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarPrecio debe retornar error para precio vacío`() {
        val resultado = Validadores.validarPrecio("")
        assertThat(resultado).isEqualTo("El precio es obligatorio")
    }

    @Test
    fun `validarPrecio debe retornar error para precio inválido`() {
        val resultado = Validadores.validarPrecio("precio_invalido")
        assertThat(resultado).isEqualTo("Precio inválido")
    }

    @Test
    fun `validarPrecio debe retornar error para precio menor al mínimo`() {
        val resultado = Validadores.validarPrecio("0.5")
        assertThat(resultado).isEqualTo("El precio debe ser mayor a 0")
    }

    @Test
    fun `validarStock debe retornar null para stock válido`() {
        val resultado = Validadores.validarStock("10")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarStock debe retornar error para stock vacío`() {
        val resultado = Validadores.validarStock("")
        assertThat(resultado).isEqualTo("El stock es obligatorio")
    }

    @Test
    fun `validarStock debe retornar error para stock inválido`() {
        val resultado = Validadores.validarStock("stock_invalido")
        assertThat(resultado).isEqualTo("Stock inválido")
    }

    @Test
    fun `validarStock debe retornar error para stock menor al mínimo`() {
        val resultado = Validadores.validarStock("0")
        assertThat(resultado).isEqualTo("El stock no puede ser negativo")
    }

    @Test
    fun `validarDescripcion debe retornar null para descripción válida`() {
        val resultado = Validadores.validarDescripcion("Esta es una descripción válida con más de 10 caracteres")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarDescripcion debe retornar error para descripción vacía`() {
        val resultado = Validadores.validarDescripcion("")
        assertThat(resultado).isEqualTo("La descripción es obligatoria")
    }

    @Test
    fun `validarDescripcion debe retornar error para descripción muy corta`() {
        val resultado = Validadores.validarDescripcion("Corta")
        assertThat(resultado).isEqualTo("La descripción debe tener al menos 10 caracteres")
    }

    @Test
    fun `validarDescripcion debe retornar error para descripción muy larga`() {
        val descripcionLarga = "a".repeat(501)
        val resultado = Validadores.validarDescripcion(descripcionLarga)
        assertThat(resultado).isEqualTo("La descripción no puede exceder 500 caracteres")
    }
}