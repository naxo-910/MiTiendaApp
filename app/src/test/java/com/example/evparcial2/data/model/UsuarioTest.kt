package com.example.evparcial2.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UsuarioTest {

    @Test
    fun `debe crear usuario con todos los campos`() {
        val usuario = Usuario(
            id = 1L,
            nombre = "Juan Pérez",
            email = "juan@ejemplo.com",
            rol = "cliente",
            contrasena = "password123"
        )

        assertThat(usuario.id).isEqualTo(1L)
        assertThat(usuario.nombre).isEqualTo("Juan Pérez")
        assertThat(usuario.email).isEqualTo("juan@ejemplo.com")
        assertThat(usuario.rol).isEqualTo("cliente")
        assertThat(usuario.contrasena).isEqualTo("password123")
    }

    @Test
    fun `debe crear usuario con contraseña por defecto vacía`() {
        val usuario = Usuario(
            id = 1L,
            nombre = "Ana López",
            email = "ana@ejemplo.com",
            rol = "admin"
        )

        assertThat(usuario.contrasena).isEqualTo("")
    }

    @Test
    fun `dos usuarios con mismos datos deben ser iguales`() {
        val usuario1 = Usuario(
            id = 1L,
            nombre = "María García",
            email = "maria@ejemplo.com",
            rol = "cliente",
            contrasena = "pass123"
        )

        val usuario2 = Usuario(
            id = 1L,
            nombre = "María García",
            email = "maria@ejemplo.com",
            rol = "cliente",
            contrasena = "pass123"
        )

        assertThat(usuario1).isEqualTo(usuario2)
        assertThat(usuario1.hashCode()).isEqualTo(usuario2.hashCode())
    }

    @Test
    fun `usuarios con diferentes datos no deben ser iguales`() {
        val usuario1 = Usuario(
            id = 1L,
            nombre = "Pedro",
            email = "pedro@ejemplo.com",
            rol = "cliente"
        )

        val usuario2 = Usuario(
            id = 2L,
            nombre = "Pedro",
            email = "pedro@ejemplo.com",
            rol = "cliente"
        )

        assertThat(usuario1).isNotEqualTo(usuario2)
    }

    @Test
    fun `toString debe contener información del usuario`() {
        val usuario = Usuario(
            id = 1L,
            nombre = "Test User",
            email = "test@ejemplo.com",
            rol = "test"
        )

        val toString = usuario.toString()
        assertThat(toString).contains("1")
        assertThat(toString).contains("Test User")
        assertThat(toString).contains("test@ejemplo.com")
        assertThat(toString).contains("test")
    }

    @Test
    fun `copy debe crear nueva instancia con campos modificados`() {
        val usuarioOriginal = Usuario(
            id = 1L,
            nombre = "Original",
            email = "original@ejemplo.com",
            rol = "cliente"
        )

        val usuarioCopiado = usuarioOriginal.copy(nombre = "Modificado")

        assertThat(usuarioCopiado.id).isEqualTo(usuarioOriginal.id)
        assertThat(usuarioCopiado.nombre).isEqualTo("Modificado")
        assertThat(usuarioCopiado.email).isEqualTo(usuarioOriginal.email)
        assertThat(usuarioCopiado.rol).isEqualTo(usuarioOriginal.rol)
    }
}