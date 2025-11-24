package com.example.evparcial2.data.local.repository

import com.example.evparcial2.data.local.dao.DaoPedido
import com.example.evparcial2.data.local.entities.EntidadPedido
import kotlinx.coroutines.flow.Flow

class RepoPedidos(private val daoPedido: DaoPedido = DaoPedido()) {

    suspend fun insertarPedido(pedido: EntidadPedido): Long {
        return daoPedido.insertar(pedido)
    }

    fun obtenerPedidos(): Flow<List<EntidadPedido>> {
        return daoPedido.obtenerTodos()
    }

    suspend fun obtenerPedidoPorId(id: Long): EntidadPedido? {
        return daoPedido.obtenerPorId(id)
    }

    fun obtenerPedidosPorUsuario(usuarioId: Long): Flow<List<EntidadPedido>> {
        return daoPedido.obtenerPorUsuario(usuarioId)
    }

    suspend fun actualizarPedido(pedido: EntidadPedido) {
        daoPedido.actualizar(pedido)
    }
}