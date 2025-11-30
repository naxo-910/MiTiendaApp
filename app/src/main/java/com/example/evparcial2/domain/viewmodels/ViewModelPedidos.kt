package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.model.ItemCarrito
import com.example.evparcial2.data.model.Pedido
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.data.repository.BasicRepositories
import com.example.evparcial2.util.Constantes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- UI STATE PARA PEDIDOS ---
data class PedidosUiState(
    val pedidos: List<Pedido> = emptyList(),
    val pedidoSeleccionado: Pedido? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ViewModelPedidos @Inject constructor(
    private val repository: BasicRepositories
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PedidosUiState())
    val uiState: StateFlow<PedidosUiState> = _uiState.asStateFlow()
    
    fun loadPedidosUsuario(usuarioId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val pedidos = repository.getPedidosPorUsuario(usuarioId)
                _uiState.value = _uiState.value.copy(
                    pedidos = pedidos,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar pedidos: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    fun crearNuevoPedido(usuario: Usuario, items: List<ItemCarrito>) {
        if (items.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val totalPedido = items.sumOf { it.subtotal }
                
                val nuevoPedido = Pedido(
                    id = System.currentTimeMillis(),
                    usuarioId = usuario.id,
                    nombreUsuario = usuario.nombre,
                    emailUsuario = usuario.email,
                    items = items,
                    total = totalPedido,
                    estado = Constantes.ESTADO_PEDIDO_CONFIRMADO,
                    fechaCreacion = System.currentTimeMillis(),
                    fechaActualizacion = System.currentTimeMillis(),
                    direccionEntrega = "Direcci\u00f3n por definir",
                    metodoPago = "Pendiente"
                )
                
                val pedidoCreado = repository.crearPedido(nuevoPedido)
                
                // Recargar pedidos del usuario
                loadPedidosUsuario(usuario.id)
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al crear pedido: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    fun actualizarEstadoPedido(pedidoId: Long, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                repository.actualizarEstadoPedido(pedidoId, nuevoEstado)
                
                // Actualizar el pedido en la lista
                val pedidosActualizados = _uiState.value.pedidos.map { pedido ->
                    if (pedido.id == pedidoId) {
                        pedido.copy(
                            estado = nuevoEstado,
                            fechaActualizacion = System.currentTimeMillis()
                        )
                    } else {
                        pedido
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    pedidos = pedidosActualizados
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al actualizar pedido: ${e.message}"
                )
            }
        }
    }
    
    fun seleccionarPedido(pedido: Pedido?) {
        _uiState.value = _uiState.value.copy(
            pedidoSeleccionado = pedido
        )
    }
    
    fun cancelarPedido(pedidoId: Long) {
        actualizarEstadoPedido(pedidoId, "cancelado")
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    // Funci\u00f3n de utilidad para obtener estad\u00edsticas
    fun getEstadisticasPedidos(usuarioId: Long) {
        viewModelScope.launch {
            try {
                val pedidos = repository.getPedidosPorUsuario(usuarioId)
                
                val estadisticas = mapOf(
                    "total_pedidos" to pedidos.size,
                    "pedidos_confirmados" to pedidos.count { it.estado == Constantes.ESTADO_PEDIDO_CONFIRMADO },
                    "pedidos_enviados" to pedidos.count { it.estado == Constantes.ESTADO_PEDIDO_ENVIADO },
                    "pedidos_entregados" to pedidos.count { it.estado == Constantes.ESTADO_PEDIDO_ENTREGADO },
                    "total_gastado" to pedidos.sumOf { it.total }.toInt()
                )
                
                // Aqu\u00ed podr\u00edas emitir las estad\u00edsticas si necesitas mostrarlas en la UI
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al calcular estadísticas: ${e.message}"
                )
            }
        }
    }
}