package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.model.Producto
import com.example.evparcial2.data.model.HostelDto
import com.example.evparcial2.data.repository.ApiResult
import com.example.evparcial2.data.repository.HostelRepository
import com.example.evparcial2.data.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- ✨ Estado del formulario de alojamientos ---
data class EstadoFormularioAlojamiento(
    val idAlojamiento: Long? = null,
    val nombreAlojamiento: String = "",
    val descripcionDetallada: String = "",
    val precioNochePesos: String = "",
    val habitacionesDisponibles: String = "",
    val categoriaAlojamiento: String = "",
    val tipoHabitacion: String = "",
    val ciudadDestino: String = "",
    val paisDestino: String = "",

    // 🚨 Mensajes de validación amigables
    val errorNombreAlojamiento: String? = null,
    val errorPrecioNoche: String? = null,
    val errorHabitacionesDisponibles: String? = null,
    val errorCategoriaAlojamiento: String? = null,
    val errorTipoHabitacion: String? = null,
    val mensajeErrorGeneral: String? = null
)

@HiltViewModel
class ViewModelProductos @Inject constructor(
    private val hostelRepository: HostelRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {
    
    private val _listaAlojamientosCompleta = MutableStateFlow<List<Producto>>(emptyList())
    val listaAlojamientosCompleta: StateFlow<List<Producto>> = _listaAlojamientosCompleta.asStateFlow()

    // --- 🌐 Estados para conexión con servicios remotos ---
    private val _alojamientosDesdeApi = MutableStateFlow<List<HostelDto>>(emptyList())
    val alojamientosDesdeApi: StateFlow<List<HostelDto>> = _alojamientosDesdeApi.asStateFlow()
    
    private val _cargandoDatosRemotos = MutableStateFlow(false)
    val cargandoDatosRemotos: StateFlow<Boolean> = _cargandoDatosRemotos.asStateFlow()
    
    private val _mensajeErrorConexion = MutableStateFlow<String?>(null)
    val mensajeErrorConexion: StateFlow<String?> = _mensajeErrorConexion.asStateFlow()

    // --- 📝 Estados para formulario de alojamiento ---
    private val _estadoFormulario = MutableStateFlow(EstadoFormularioAlojamiento())
    val estadoFormulario = _estadoFormulario.asStateFlow()

    private val _procesandoGuardado = MutableStateFlow(false)
    val procesandoGuardado = _procesandoGuardado.asStateFlow()

    private val _notificacionGuardadoExitoso = MutableSharedFlow<Unit>()
    val notificacionGuardadoExitoso = _notificacionGuardadoExitoso.asSharedFlow()

    init {
        // 🚀 Cargar alojamientos remotos al inicializar la aplicación
        cargarAlojamientosDesdeServicioRemoto()
        
        // 🏨 Catálogo inicial de alojamientos locales como respaldo
        _listaAlojamientosCompleta.value = listOf(
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

    // --- 🏨 Preparar formulario para crear o editar alojamiento ---
    fun prepararFormularioAlojamiento(idAlojamiento: Long?) {
        if (idAlojamiento == null) {
            // ✨ Nuevo alojamiento: limpiar formulario para empezar de cero
            _estadoFormulario.value = EstadoFormularioAlojamiento()
        } else {
            // 🔍 Editar alojamiento existente: buscar en nuestro catálogo
            val alojamientoEncontrado = _listaAlojamientosCompleta.value.find { it.id == idAlojamiento }
            if (alojamientoEncontrado != null) {
                // 📝 Llenar formulario con datos del alojamiento encontrado
                _estadoFormulario.value = EstadoFormularioAlojamiento(
                    idAlojamiento = alojamientoEncontrado.id,
                    nombreAlojamiento = alojamientoEncontrado.nombre,
                    descripcionDetallada = alojamientoEncontrado.descripcion,
                    precioNochePesos = alojamientoEncontrado.precio.toString(),
                    habitacionesDisponibles = alojamientoEncontrado.stock.toString(),
                    categoriaAlojamiento = alojamientoEncontrado.categoria,
                    tipoHabitacion = alojamientoEncontrado.tipo,
                    ciudadDestino = alojamientoEncontrado.ciudad,
                    paisDestino = alojamientoEncontrado.pais
                )
            }
        }
    }

    // --- ✍️ Actualizar datos del formulario mientras el usuario escribe ---
    fun actualizarCamposFormulario(
        nombreAlojamiento: String? = null,
        descripcionDetallada: String? = null,
        precioNochePesos: String? = null,
        habitacionesDisponibles: String? = null,
        categoriaAlojamiento: String? = null,
        ciudadDestino: String? = null,
        paisDestino: String? = null,
        tipoHabitacion: String? = null
    ) {
        _estadoFormulario.update { estadoActual ->
            estadoActual.copy(
                nombreAlojamiento = nombreAlojamiento ?: estadoActual.nombreAlojamiento,
                descripcionDetallada = descripcionDetallada ?: estadoActual.descripcionDetallada,
                precioNochePesos = precioNochePesos ?: estadoActual.precioNochePesos,
                habitacionesDisponibles = habitacionesDisponibles ?: estadoActual.habitacionesDisponibles,
                categoriaAlojamiento = categoriaAlojamiento ?: estadoActual.categoriaAlojamiento,
                tipoHabitacion = tipoHabitacion ?: estadoActual.tipoHabitacion,
                ciudadDestino = ciudadDestino ?: estadoActual.ciudadDestino,
                paisDestino = paisDestino ?: estadoActual.paisDestino,
                // 🧽 Limpiar mensajes de error al escribir
                errorNombreAlojamiento = null,
                errorPrecioNoche = null,
                errorHabitacionesDisponibles = null,
                errorCategoriaAlojamiento = null,
                errorTipoHabitacion = null,
                mensajeErrorGeneral = null
            )
        }
    }

    // --- 💾 Validar y guardar alojamiento (crear nuevo o actualizar existente) ---
    fun guardarAlojamientoEnCatalogo() {
        _procesandoGuardado.value = true
        val datosFormulario = _estadoFormulario.value

        // --- 🔍 Validaciones con mensajes amigables ---
        val precioValidado = datosFormulario.precioNochePesos.toDoubleOrNull()
        val habitacionesValidadas = datosFormulario.habitacionesDisponibles.toIntOrNull()

        if (datosFormulario.nombreAlojamiento.isBlank() || datosFormulario.categoriaAlojamiento.isBlank() || datosFormulario.tipoHabitacion.isBlank()) {
            _estadoFormulario.update { 
                it.copy(
                    errorNombreAlojamiento = "🏨 Por favor ingresa un nombre para el alojamiento",
                    errorCategoriaAlojamiento = "🏷️ Selecciona una categoría",
                    errorTipoHabitacion = "🚪 Indica el tipo de habitación"
                ) 
            }
            _procesandoGuardado.value = false
            return
        }
        if (precioValidado == null || precioValidado <= 0) {
            _estadoFormulario.update { it.copy(errorPrecioNoche = "💵 Ingresa un precio válido mayor a $0") }
            _procesandoGuardado.value = false
            return
        }
        if (habitacionesValidadas == null || habitacionesValidadas < 0) {
            _estadoFormulario.update { it.copy(errorHabitacionesDisponibles = "🚪 Ingresa un número válido de habitaciones") }
            _procesandoGuardado.value = false
            return
        }
        // --- ✅ Fin de validaciones ---

        // Si todo está correcto, procedemos a guardar
        viewModelScope.launch {
            try {
                if (datosFormulario.idAlojamiento == null) {
                    // --- ✨ CREAR NUEVO ALOJAMIENTO ---
                    crearNuevoAlojamiento(
                        nombreAlojamiento = datosFormulario.nombreAlojamiento,
                        descripcionDetallada = datosFormulario.descripcionDetallada,
                        precioNochePesos = precioValidado,
                        habitacionesDisponibles = habitacionesValidadas,
                        categoriaAlojamiento = datosFormulario.categoriaAlojamiento,
                        tipoHabitacion = datosFormulario.tipoHabitacion
                    )
                } else {
                    // --- 🔄 ACTUALIZAR ALOJAMIENTO EXISTENTE ---
                    val alojamientoActualizado = Producto(
                        id = datosFormulario.idAlojamiento,
                        nombre = datosFormulario.nombreAlojamiento,
                        descripcion = datosFormulario.descripcionDetallada,
                        precio = precioValidado,
                        stock = habitacionesValidadas,
                        categoria = datosFormulario.categoriaAlojamiento,
                        tipo = datosFormulario.tipoHabitacion,
                        ciudad = datosFormulario.ciudadDestino,
                        pais = datosFormulario.paisDestino
                    )
                    actualizarAlojamientoExistente(alojamientoActualizado)
                }

                _procesandoGuardado.value = false
                _notificacionGuardadoExitoso.emit(Unit) // 🎉 Notificar éxito a la interfaz

            } catch (excepcion: Exception) {
                _procesandoGuardado.value = false
                _estadoFormulario.update { 
                    it.copy(mensajeErrorGeneral = "😞 Error inesperado: ${excepcion.message}") 
                }
            }
        }
    }

    // --- 🏨 Funciones de gestión del catálogo de alojamientos ---
    fun crearNuevoAlojamiento(
        nombreAlojamiento: String,
        descripcionDetallada: String,
        precioNochePesos: Double,
        habitacionesDisponibles: Int,
        categoriaAlojamiento: String,
        tipoHabitacion: String,
        ciudadDestino: String = "",
        paisDestino: String = "",
        urlImagenAlojamiento: String? = null
    ) {
        val idUnicoAlojamiento = System.currentTimeMillis()
        val nuevoAlojamiento = Producto(
            id = idUnicoAlojamiento,
            nombre = nombreAlojamiento,
            descripcion = descripcionDetallada,
            precio = precioNochePesos,
            stock = habitacionesDisponibles,
            categoria = categoriaAlojamiento,
            tipo = tipoHabitacion,
            ciudad = ciudadDestino,
            pais = paisDestino,
            imagenUrl = urlImagenAlojamiento
        )
        _listaAlojamientosCompleta.update { catalogoActual -> catalogoActual + nuevoAlojamiento }
    }

    fun actualizarAlojamientoExistente(alojamientoModificado: Producto) {
        _listaAlojamientosCompleta.update { catalogoActual ->
            catalogoActual.map {
                if (it.id == alojamientoModificado.id) alojamientoModificado else it
            }
        }
    }

    fun removerAlojamientoDelCatalogo(alojamientoAEliminar: Producto) {
        _listaAlojamientosCompleta.update { catalogoActual ->
            catalogoActual.filter { it.id != alojamientoAEliminar.id }
        }
    }

    // --- 🌐 FUNCIONES PARA SERVICIOS REMOTOS DE ALOJAMIENTOS ---

    /**
     * 🚀 Cargar todo el catálogo de alojamientos desde el servicio remoto
     */
    fun cargarAlojamientosDesdeServicioRemoto() {
        viewModelScope.launch {
            hostelRepository.getAllHostels().collect { resultado ->
                when (resultado) {
                    is ApiResult.Loading -> {
                        _cargandoDatosRemotos.value = true
                        _mensajeErrorConexion.value = null
                    }
                    is ApiResult.Success -> {
                        _cargandoDatosRemotos.value = false
                        _alojamientosDesdeApi.value = resultado.data
                        _mensajeErrorConexion.value = null
                        
                        // 🔄 Transformar datos remotos a formato local
                        val alojamientosConvertidos = resultado.data.map { alojamientoRemoto ->
                            Producto(
                                id = alojamientoRemoto.id.toLongOrNull() ?: System.currentTimeMillis(),
                                nombre = alojamientoRemoto.nombre,
                                descripcion = alojamientoRemoto.descripcion,
                                precio = alojamientoRemoto.precio,
                                stock = if (alojamientoRemoto.disponible) 10 else 0,
                                categoria = alojamientoRemoto.tipoHabitacion,
                                tipo = alojamientoRemoto.tipoHabitacion.lowercase(),
                                ciudad = alojamientoRemoto.ciudad,
                                pais = alojamientoRemoto.pais,
                                imagenUrl = alojamientoRemoto.imagenUrl,
                                promedioCalificacion = alojamientoRemoto.rating,
                                totalReviews = alojamientoRemoto.totalReviews
                            )
                        }
                        
                        // 🏨 Usar datos remotos si están disponibles, sino mantener locales
                        if (alojamientosConvertidos.isNotEmpty()) {
                            _listaAlojamientosCompleta.value = alojamientosConvertidos
                        }
                    }
                    is ApiResult.Error -> {
                        _cargandoDatosRemotos.value = false
                        _mensajeErrorConexion.value = resultado.message
                        // 🚫 En caso de error, mantener datos locales como respaldo
                    }
                }
            }
        }
    }

    /**
     * Busca hostels con filtros específicos
     */
    fun searchHostelsFromApi(
        pais: String? = null,
        ciudad: String? = null,
        tipoHabitacion: String? = null,
        precioMin: Int? = null,
        precioMax: Int? = null
    ) {
        viewModelScope.launch {
            hostelRepository.searchHostels(pais, ciudad, tipoHabitacion, precioMin, precioMax)
                .collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            _cargandoDatosRemotos.value = true
                            _mensajeErrorConexion.value = null
                        }
                        is ApiResult.Success -> {
                            _cargandoDatosRemotos.value = false
                            _alojamientosDesdeApi.value = result.data
                            _mensajeErrorConexion.value = null
                            
                            // Actualizar productos con resultados filtrados
                            val productosFiltrados = result.data.map { hostel ->
                                Producto(
                                    id = hostel.id.toLongOrNull() ?: System.currentTimeMillis(),
                                    nombre = hostel.nombre,
                                    descripcion = hostel.descripcion,
                                    precio = hostel.precio,
                                    stock = if (hostel.disponible) 10 else 0,
                                    categoria = hostel.tipoHabitacion,
                                    tipo = hostel.tipoHabitacion.lowercase(),
                                    ciudad = hostel.ciudad,
                                    pais = hostel.pais,
                                    imagenUrl = hostel.imagenUrl,
                                    promedioCalificacion = hostel.rating,
                                    totalReviews = hostel.totalReviews
                                )
                            }
                            _listaAlojamientosCompleta.value = productosFiltrados
                        }
                        is ApiResult.Error -> {
                            _cargandoDatosRemotos.value = false
                            _mensajeErrorConexion.value = result.message
                        }
                    }
                }
        }
    }

    /**
     * Refresca los datos desde el API
     */
    fun actualizarDatosDesdeServicioRemoto() {
        cargarAlojamientosDesdeServicioRemoto()
    }

    /**
     * 🧹 Limpiar mensaje de error de conexión
     */
    fun limpiarErrorDeConexion() {
        _mensajeErrorConexion.value = null
    }

    /**
     * Convierte precio USD a CLP usando API de tipos de cambio
     */
    fun convertPriceToClp(priceUsd: Double, onResult: (Double) -> Unit) {
        viewModelScope.launch {
            exchangeRateRepository.convertUsdToClp(priceUsd).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        onResult(result.data)
                    }
                    is ApiResult.Error -> {
                        // En caso de error, usar tasa fija aproximada
                        onResult(priceUsd * 900) // Tasa aproximada USD->CLP
                    }
                    is ApiResult.Loading -> {
                        // No hacer nada mientras carga
                    }
                }
            }
        }
    }
}