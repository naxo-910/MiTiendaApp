package com.example.evparcial2.data.local.dao

import com.example.evparcial2.data.local.entities.EntidadUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DaoUsuario {
    private val usuarios = mutableListOf<EntidadUsuario>()

    suspend fun insertar(usuario: EntidadUsuario): Long {
        val nuevoId = (usuarios.maxByOrNull { it.id }?.id ?: 0) + 1
        usuarios.add(usuario.copy(id = nuevoId))
        return nuevoId
    }

    fun obtenerTodos(): Flow<List<EntidadUsuario>> = flowOf(usuarios)

    suspend fun obtenerPorId(id: Long): EntidadUsuario? {
        return usuarios.find { it.id == id }
    }

    suspend fun login(email: String, contrasena: String): EntidadUsuario? {
        return usuarios.find { it.email == email && it.contrasena == contrasena }
    }

    suspend fun obtenerPorEmail(email: String): EntidadUsuario? {
        return usuarios.find { it.email == email }
    }

    suspend fun actualizar(usuario: EntidadUsuario) {
        val index = usuarios.indexOfFirst { it.id == usuario.id }
        if (index != -1) {
            usuarios[index] = usuario
        }
    }
}