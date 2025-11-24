package com.example.evparcial2.data.local.datastore

import com.example.evparcial2.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Preferencias {
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual


    private val _estaLogueado = MutableStateFlow(false)
    val estaLogueado: StateFlow<Boolean> = _estaLogueado

    fun guardarUsuario(usuario: Usuario) {
        _usuarioActual.value = usuario
        _estaLogueado.value = true
    }


    fun cerrarSesion() {
        _usuarioActual.value = null
        _estaLogueado.value = false
    }
}