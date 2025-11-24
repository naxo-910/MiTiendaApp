package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.local.entities.EntidadPedido // ¡IMPORTANTE!
import com.example.evparcial2.data.local.repository.RepoPedidos // ¡IMPORTANTE!
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.domain.viewmodels.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado para guardar la lista de pedidos
data class PedidosUiState(
    val pedidos: List<EntidadPedido> = emptyList(),
    val isLoading: Boolean = true
)

class ViewModelPedidos : ViewModel() {

    private val repoPedidos = RepoPedidos()

    private val _uiState = MutableStateFlow(PedidosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarPedidos()
    }

    /**
     * Obtiene todos los pedidos de la base de datos y los pone en el state
     */
    fun cargarPedidos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // --- ¡¡ARREGLO!! Usamos el nombre 'obtenerPedidos' de tu repo ---
            repoPedidos.obtenerPedidos().collect { pedidos ->
                _uiState.update { it.copy(pedidos = pedidos, isLoading = false) }
            }
        }
    }

    /**
     * Esta es la función clave: Crea un nuevo pedido en la base de datos
     * ¡AHORA COINCIDE CON TU ENTIDAD!
     */
    fun crearNuevoPedido(usuario: Usuario, items: List<ItemCarrito>) {
        viewModelScope.launch {
            val total = items.sumOf { it.producto.precio * it.cantidad }

            val resumen = items.joinToString(separator = ", ") {
                "${it.producto.nombre} (x${it.cantidad})"
            }

            val nuevoPedido = EntidadPedido(
                // id se autogenera (valor por defecto 0)
                usuarioId = usuario.id, // ¡Usamos el ID del usuario!
                fechaPedido = System.currentTimeMillis(),
                total = total,
                estado = "Confirmado",
                direccionEntrega = "Dirección de prueba 123",
                productosJson = resumen
            )

            // --- ¡¡ARREGLO!! Usamos el nombre 'insertarPedido' de tu repo ---
            repoPedidos.insertarPedido(nuevoPedido)
        }
    }
}