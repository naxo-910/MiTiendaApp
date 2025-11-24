package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.model.Producto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- ¡NUEVO! Estado para el formulario ---
data class UiStateFormProducto(
    val id: Long? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val stock: String = "",
    val categoria: String = "",
    val tipo: String = "",
    val ciudad: String = "",
    val pais: String = "",

    // Errores de validación
    val nombreError: String? = null,
    val precioError: String? = null,
    val stockError: String? = null,
    val categoriaError: String? = null,
    val tipoError: String? = null,
    val errorGeneral: String? = null
)

class ViewModelProductos : ViewModel() {
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // --- ¡NUEVO! Estados para el formulario ---
    private val _uiStateForm = MutableStateFlow(UiStateFormProducto())
    val uiStateForm = _uiStateForm.asStateFlow()

    private val _isLoadingForm = MutableStateFlow(false)
    val isLoadingForm = _isLoadingForm.asStateFlow()

    private val _eventoGuardadoExitoso = MutableSharedFlow<Unit>()
    val eventoGuardadoExitoso = _eventoGuardadoExitoso.asSharedFlow()

    init {
        // Tu lista de productos se queda intacta
        _productos.value = listOf(
            Producto(
                id = 1L,
                nombre = "Hostal Centro Histórico",
                descripcion = "Habitación compartida en el corazón de la ciudad. Incluye desayuno, WiFi gratis, cocina compartida y terraza con vista panorámica.",
                precio = 35000.0,
                stock = 8,
                categoria = "Económico",
                tipo = "compartida",
                ciudad = "Santiago",
                pais = "Chile",
                imagenUrl = "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=400"
            ),
            Producto(
                id = 2L,
                nombre = "Hostal Boutique Premium",
                descripcion = "Habitación privada con baño propio, aire acondicionado, TV, minibar y balcón. Incluye spa, piscina en azotea y servicio de conserjería 24h.",
                precio = 95000.0,
                stock = 4,
                categoria = "Premium",
                tipo = "privada",
                ciudad = "Valparaíso",
                pais = "Chile",
                imagenUrl = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=400"
            ),
            Producto(
                id = 3L,
                nombre = "Hostal Mochileros",
                descripcion = "Litera en dormitorio mixto de 6 camas. Perfecto para viajeros jóvenes. Incluye casilleros, área común, cocina equipada y tours gratuitos.",
                precio = 18000.0,
                stock = 12,
                categoria = "Backpacker",
                tipo = "litera",
                ciudad = "San Pedro de Atacama",
                pais = "Chile",
                imagenUrl = "https://images.unsplash.com/photo-1586105251261-72a756497a11?w=400"
            ),
            Producto(
                id = 4L,
                nombre = "Hostal Familiar Garden",
                descripcion = "Habitación familiar para hasta 4 personas con baño privado, nevera y zona de estar. Jardín, área de juegos y desayuno buffet incluido.",
                precio = 120000.0,
                stock = 6,
                categoria = "Familiar",
                tipo = "familiar",
                ciudad = "Pucón",
                pais = "Chile",
                imagenUrl = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400"
            ),
            Producto(
                id = 5L,
                nombre = "Eco-Hostal Montaña",
                descripcion = "Cabaña ecológica con vista a la montaña. Habitación doble con baño compartido, energía solar, huerto orgánico y actividades de senderismo.",
                precio = 55000.0,
                stock = 8,
                categoria = "Eco-turismo",
                tipo = "doble",
                ciudad = "El Calafate",
                pais = "Argentina",
                imagenUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=400"
            ),
            Producto(
                id = 6L,
                nombre = "Hostal Playa Bonita",
                descripcion = "Habitación doble a 50m de la playa. Incluye desayuno, alquiler de tablas de surf, hamacas y bar en la playa con música en vivo.",
                precio = 42000.0,
                stock = 10,
                categoria = "Playa",
                tipo = "doble",
                ciudad = "Viña del Mar",
                pais = "Chile",
                imagenUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=400"
            ),
            Producto(
                id = 7L,
                nombre = "Urban Backpackers Lima",
                descripcion = "Litera en dormitorio de 8 camas en el corazón de Miraflores. WiFi, cocina, terraza con vista al mar y tours gastronómicos incluidos.",
                precio = 25000.0,
                stock = 16,
                categoria = "Backpacker",
                tipo = "litera",
                ciudad = "Lima",
                pais = "Perú",
                imagenUrl = "https://images.unsplash.com/photo-1586105251261-72a756497a11?w=400"
            ),
            Producto(
                id = 8L,
                nombre = "Hostal Colonial Boutique",
                descripcion = "Suite privada en edificio colonial restaurado. Baño de mármol, balcón francés, desayuno gourmet y servicio de spa.",
                precio = 150000.0,
                stock = 3,
                categoria = "Lujo",
                tipo = "suite",
                ciudad = "Cartagena",
                pais = "Colombia",
                imagenUrl = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=400"
            )
        )
    }

    // --- ¡NUEVO! Carga el producto a editar en el formulario ---
    fun cargarProducto(productoId: Long?) {
        if (productoId == null) {
            // Es un producto nuevo, reseteamos el form
            _uiStateForm.value = UiStateFormProducto()
        } else {
            // Es un producto existente, lo buscamos en la lista
            val producto = _productos.value.find { it.id == productoId }
            if (producto != null) {
                // Si lo encontramos, llenamos el formulario
                _uiStateForm.value = UiStateFormProducto(
                    id = producto.id,
                    nombre = producto.nombre,
                    descripcion = producto.descripcion,
                    precio = producto.precio.toString(),
                    stock = producto.stock.toString(),
                    categoria = producto.categoria,
                    tipo = producto.tipo,
                    ciudad = producto.ciudad,
                    pais = producto.pais
                )
            }
        }
    }

    // --- ¡NUEVO! Actualiza el estado del form mientras el usuario escribe ---
    fun onFormChange(
        nombre: String? = null,
        descripcion: String? = null,
        precio: String? = null,
        stock: String? = null,
        categoria: String? = null,
        ciudad: String? = null,
        pais: String? = null,
        tipo: String? = null
    ) {
        _uiStateForm.update { state ->
            state.copy(
                nombre = nombre ?: state.nombre,
                descripcion = descripcion ?: state.descripcion,
                precio = precio ?: state.precio,
                stock = stock ?: state.stock,
                categoria = categoria ?: state.categoria,
                tipo = tipo ?: state.tipo,
                ciudad = ciudad ?: state.ciudad,
                pais = pais ?: state.pais,
                // Reseteamos errores al escribir
                nombreError = null,
                precioError = null,
                stockError = null,
                categoriaError = null,
                tipoError = null,
                errorGeneral = null
            )
        }
    }

    // --- ¡NUEVO! Valida y decide si crear o actualizar ---
    fun guardarProducto() {
        _isLoadingForm.value = true
        val state = _uiStateForm.value

        // --- Validación ---
        val precioDouble = state.precio.toDoubleOrNull()
        val stockInt = state.stock.toIntOrNull()

        if (state.nombre.isBlank() || state.categoria.isBlank() || state.tipo.isBlank()) {
            _uiStateForm.update { it.copy(nombreError = "Campo obligatorio", categoriaError = "Campo obligatorio", tipoError = "Campo obligatorio") }
            _isLoadingForm.value = false
            return
        }
        if (precioDouble == null) {
            _uiStateForm.update { it.copy(precioError = "Precio inválido") }
            _isLoadingForm.value = false
            return
        }
        if (stockInt == null) {
            _uiStateForm.update { it.copy(stockError = "Stock inválido") }
            _isLoadingForm.value = false
            return
        }
        // --- Fin Validación ---

        // Si la validación pasa, creamos o actualizamos
        viewModelScope.launch {
            try {
                if (state.id == null) {
                    // --- CREAR ---
                    agregarProducto(
                        nombre = state.nombre,
                        descripcion = state.descripcion,
                        precio = precioDouble,
                        stock = stockInt,
                        categoria = state.categoria,
                        tipo = state.tipo
                    )
                } else {
                    // --- ACTUALIZAR ---
                    val productoActualizado = Producto(
                        id = state.id,
                        nombre = state.nombre,
                        descripcion = state.descripcion,
                        precio = precioDouble,
                        stock = stockInt,
                        categoria = state.categoria,
                        tipo = state.tipo,
                        ciudad = state.ciudad,
                        pais = state.pais
                    )
                    actualizarProducto(productoActualizado)
                }

                _isLoadingForm.value = false
                _eventoGuardadoExitoso.emit(Unit) // ¡Avisamos a la UI que navegue!

            } catch (e: Exception) {
                _isLoadingForm.value = false
                _uiStateForm.update { it.copy(errorGeneral = e.message) }
            }
        }
    }

    // --- Tus funciones originales (modificadas para .update()) ---
    fun agregarProducto(
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int,
        categoria: String,
        tipo: String,
        ciudad: String = "",
        pais: String = "",
        imagenUrl: String? = null
    ) {
        val nuevoId = System.currentTimeMillis()
        val nuevoProducto = Producto(
            id = nuevoId,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            stock = stock,
            categoria = categoria,
            tipo = tipo,
            ciudad = ciudad,
            pais = pais,
            imagenUrl = imagenUrl
        )
        _productos.update { listaActual -> listaActual + nuevoProducto }
    }

    fun actualizarProducto(producto: Producto) {
        _productos.update { listaActual ->
            listaActual.map {
                if (it.id == producto.id) producto else it
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        _productos.update { listaActual ->
            listaActual.filter { it.id != producto.id }
        }
    }
}