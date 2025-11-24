package com.example.evparcial2.data.local.repository

import com.example.evparcial2.data.local.dao.DaoUsuario
import com.example.evparcial2.data.local.entities.EntidadUsuario
import kotlinx.coroutines.flow.Flow

class RepoUsuarios(private val daoUsuario: DaoUsuario = DaoUsuario()) {

    suspend fun insertarUsuario(usuario: EntidadUsuario): Long {
        return daoUsuario.insertar(usuario)
    }

    fun obtenerUsuarios(): Flow<List<EntidadUsuario>> {
        return daoUsuario.obtenerTodos()
    }

    suspend fun obtenerUsuarioPorId(id: Long): EntidadUsuario? {
        return daoUsuario.obtenerPorId(id)
    }

    suspend fun login(email: String, contrasena: String): EntidadUsuario? {
        return daoUsuario.login(email, contrasena)
    }

    suspend fun obtenerUsuarioPorEmail(email: String): EntidadUsuario? {
        return daoUsuario.obtenerPorEmail(email)
    }

    suspend fun actualizarUsuario(usuario: EntidadUsuario) {
        daoUsuario.actualizar(usuario)
    }
}