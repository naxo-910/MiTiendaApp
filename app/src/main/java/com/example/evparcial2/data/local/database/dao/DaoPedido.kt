package com.example.evparcial2.data.local.dao

import com.example.evparcial2.data.local.entities.EntidadPedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DaoPedido {
    private val pedidos = mutableListOf<EntidadPedido>()

    suspend fun insertar(pedido: EntidadPedido): Long {
        val nuevoId = (pedidos.maxByOrNull { it.id }?.id ?: 0) + 1
        pedidos.add(pedido.copy(id = nuevoId))
        return nuevoId
    }

    fun obtenerTodos(): Flow<List<EntidadPedido>> = flowOf(pedidos)

    suspend fun obtenerPorId(id: Long): EntidadPedido? {
        return pedidos.find { it.id == id }
    }

    fun obtenerPorUsuario(usuarioId: Long): Flow<List<EntidadPedido>> {
        val resultados = pedidos.filter { it.usuarioId == usuarioId }
        return flowOf(resultados)
    }

    suspend fun actualizar(pedido: EntidadPedido) {
        val index = pedidos.indexOfFirst { it.id == pedido.id }
        if (index != -1) {
            pedidos[index] = pedido
        }
    }
}