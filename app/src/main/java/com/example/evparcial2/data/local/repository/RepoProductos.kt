package com.example.evparcial2.data.local.repository

import com.example.evparcial2.data.local.dao.DaoProducto
import com.example.evparcial2.data.local.entities.EntidadProducto
import kotlinx.coroutines.flow.Flow

class RepoProductos(private val daoProducto: DaoProducto = DaoProducto()) {

    suspend fun insertarProducto(producto: EntidadProducto): Long {
        return daoProducto.insertar(producto)
    }

    fun obtenerProductos(): Flow<List<EntidadProducto>> {
        return daoProducto.obtenerTodos()
    }

    suspend fun obtenerProductoPorId(id: Long): EntidadProducto? {
        return daoProducto.obtenerPorId(id)
    }

    fun buscarProductos(query: String): Flow<List<EntidadProducto>> {
        return daoProducto.buscar(query)
    }

    fun obtenerProductosPorTipo(tipo: String): Flow<List<EntidadProducto>> {
        return daoProducto.obtenerPorTipo(tipo)
    }

    suspend fun actualizarProducto(producto: EntidadProducto) {
        daoProducto.actualizar(producto)
    }

    suspend fun eliminarProducto(producto: EntidadProducto) {
        daoProducto.eliminar(producto)
    }
}