package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import com.example.evparcial2.data.model.Producto
import com.example.evparcial2.data.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel compartido que maneja el estado del carrito de compras.
 * Este ViewModel será compartido entre PantallaProductos, PantallaCarrito y PantallaInicio.
 */
@HiltViewModel
class CarritoViewModel @Inject constructor() : ViewModel() {

    // Guarda los items del carrito. La "Key" es el ID del producto (String).
    private val _items = MutableStateFlow<Map<String, ItemCarrito>>(emptyMap())
    val items = _items.asStateFlow() // La UI observará este Flow

    /**
     * Agrega un producto al carrito.
     * Si el producto ya existe, NO HACE NADA (porque es una propiedad).
     * Si es nuevo, lo añade al mapa con cantidad 1.
     * @return un Booleano: 'true' si se añadió, 'false' si ya existía.
     */
    fun agregarProducto(producto: Producto): Boolean {
        var seAgrego = false
        _items.update { carritoActual ->
            val mutableCarrito = carritoActual.toMutableMap()
            val idProducto = producto.id.toString()

            val itemExistente = mutableCarrito[idProducto]

            if (itemExistente == null) {
                // --- Producto nuevo ---
                mutableCarrito[idProducto] = ItemCarrito(
                    producto = producto,
                    cantidad = 1 // Cantidad siempre es 1
                )
                seAgrego = true
            }
            // Si ya existe, no hacemos nada y seAgrego se queda 'false'
            mutableCarrito
        }
        return seAgrego
    }

    /**
     * Elimina un producto completamente del carrito.
     * @param productoId El ID del producto a eliminar (ya viene como String)
     */
    fun eliminarProducto(productoId: String) {
        _items.update { carritoActual ->
            val mutableCarrito = carritoActual.toMutableMap()
            mutableCarrito.remove(productoId)
            mutableCarrito
        }
    }

    /**
     * Vacía completamente el carrito de compras.
     */
    fun vaciarCarrito() {
        _items.value = emptyMap()
    }

    /**
     * ¡¡FUNCIÓN AÑADIDA!!
     * Confirma el pedido (Ahora solo vacía el carrito,
     * la creación del pedido se hace en NavPrincipal)
     */
    fun confirmarPedido() {
        _items.value = emptyMap()
    }
}