package com.example.evparcial2.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.evparcial2.data.model.Producto
import com.example.evparcial2.domain.viewmodels.CarritoViewModel
import com.example.evparcial2.domain.viewmodels.ViewModelProductos
import com.example.evparcial2.ui.components.common.PlantillaProducto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAlojamientos(
    gestorProductos: ViewModelProductos,
    gestorCarrito: CarritoViewModel,
    esAdmin: Boolean,
    usuario: com.example.evparcial2.data.model.Usuario? = null,
    onAgregarProducto: () -> Unit,
    onEditarProducto: (Producto) -> Unit,
    onVerDetalle: (Producto) -> Unit,
    onIrACarrito: () -> Unit,
    onIrALogin: () -> Unit = {},
    onIrAInicio: () -> Unit = {}
) {
    val productos by gestorProductos.listaAlojamientosCompleta.collectAsState()
    val itemsCarrito by gestorCarrito.items.collectAsState()
    var tipoHabitacionSeleccionada by remember { mutableStateOf("Todas las habitaciones") }
    var ciudadSeleccionada by remember { mutableStateOf("Todas las ciudades") }
    var paisSeleccionado by remember { mutableStateOf("Todos los países") }
    
    // Filtrar alojamientos según criterios seleccionados por el huésped
    val alojamientosFiltrados = remember(productos, tipoHabitacionSeleccionada, ciudadSeleccionada, paisSeleccionado) {
        productos.filter { alojamiento ->
            val cumpleRequisitosTipoHabitacion = when (tipoHabitacionSeleccionada) {
                "Todas las habitaciones" -> true
                "Habitación Privada" -> alojamiento.tipo == "privada" || alojamiento.tipo == "suite"
                "Habitación Compartida" -> alojamiento.tipo == "compartida" || alojamiento.tipo == "litera"
                "Habitación Familiar" -> alojamiento.tipo == "familiar" || alojamiento.tipo == "doble"
                else -> true
            }
            val cumpleRequisitosCiudad = ciudadSeleccionada == "Todas las ciudades" || alojamiento.ciudad == ciudadSeleccionada
            val cumpleRequisitosPais = paisSeleccionado == "Todos los países" || alojamiento.pais == paisSeleccionado
            
            cumpleRequisitosTipoHabitacion && cumpleRequisitosCiudad && cumpleRequisitosPais
        }
    }
    
    // Obtener listas únicas de destinos disponibles para el huésped
    val ciudadesDisponiblesParaReserva = remember(productos) {
        listOf("Todas las ciudades") + productos.map { it.ciudad }.distinct().sorted()
    }
    val paisesDisponiblesParaReserva = remember(productos) {
        listOf("Todos los países") + productos.map { it.pais }.distinct().sorted()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    if (usuario != null) {
                        IconButton(onClick = onIrAInicio) {
                            Icon(
                                Icons.Default.Home, 
                                contentDescription = "Inicio",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        IconButton(onClick = onIrALogin) {
                            Icon(
                                Icons.Default.Person, 
                                contentDescription = "Iniciar Sesión",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                actions = {
                    if (usuario != null) {
                        // Saludo personalizado al huésped
                        Text(
                            text = "😊 Bienvenido, ${usuario.nombre}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        if (!esAdmin) {
                            // Carrito de reservas del huésped con contador de alojamientos
                            IconButton(onClick = onIrACarrito) {
                                BadgedBox(
                                    badge = {
                                        if (itemsCarrito.isNotEmpty()) {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.error
                                            ) { Text("${itemsCarrito.size}") }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = "Ver mis reservas pendientes",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    } else {
                        // Invitación a registrarse para hacer reservas
                        Button(
                            onClick = onIrALogin,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("🔑 Iniciar Sesión")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (esAdmin) {
                FloatingActionButton(onClick = onAgregarProducto) {
                    Icon(Icons.Default.Add, contentDescription = "🏨 Agregar Nuevo Alojamiento")
                }
            }
        }
    ) { padding ->
        if (productos.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🏨 Aún no hay alojamientos disponibles",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Próximamente tendremos opciones de hospedaje para ti",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                if (esAdmin) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = onAgregarProducto) {
                        Text("✨ Agregar Primer Alojamiento")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(0.dp)
            ) {
                // Encabezado inspirador con gradiente
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column {
                            Text(
                                text = if (esAdmin) "🏨 Gestión de Alojamientos" else "🌍 Descubre Alojamientos Únicos",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (esAdmin) 
                                    "Administra tu inventario de alojamientos con facilidad"
                                else 
                                    "Tu próxima aventura comienza con la elección perfecta de hospedaje",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
                
                // Filtros personalizados para encontrar el alojamiento ideal
                if (!esAdmin) {
                    item {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Filtro por tipo de habitación preferida
                            Text(
                                text = "🚪 Tipo de Habitación Deseada",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                val tiposDeHabitacionDisponibles = listOf(
                                    "Todas las habitaciones", 
                                    "Habitación Privada", 
                                    "Habitación Compartida", 
                                    "Habitación Familiar"
                                )
                                items(tiposDeHabitacionDisponibles) { tipoHabitacion ->
                                    FilterChip(
                                        onClick = { tipoHabitacionSeleccionada = tipoHabitacion },
                                        label = { Text(tipoHabitacion) },
                                        selected = tipoHabitacionSeleccionada == tipoHabitacion,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                }
                            }
                            
                            // Filtro por país de destino
                            Text(
                                text = "🌍 País de Destino",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                items(paisesDisponiblesParaReserva) { paisDestino ->
                                    FilterChip(
                                        onClick = { 
                                            paisSeleccionado = paisDestino
                                            // Resetear filtro de ciudad al cambiar de país
                                            if (paisDestino != "Todos los países") ciudadSeleccionada = "Todas las ciudades"
                                        },
                                        label = { Text(paisDestino) },
                                        selected = paisSeleccionado == paisDestino,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    )
                                }
                            }
                            
                            // Filtro por ciudad específica
                            Text(
                                text = "🏙️ Ciudad de Preferencia",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val ciudadesFiltradasPorPais = if (paisSeleccionado == "Todos los países") {
                                    ciudadesDisponiblesParaReserva
                                } else {
                                    listOf("Todas las ciudades") + productos.filter { it.pais == paisSeleccionado }
                                        .map { it.ciudad }.distinct().sorted()
                                }
                                
                                items(ciudadesFiltradasPorPais) { ciudadDestino ->
                                    FilterChip(
                                        onClick = { ciudadSeleccionada = ciudadDestino },
                                        label = { Text(ciudadDestino) },
                                        selected = ciudadSeleccionada == ciudadDestino,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Resumen de resultados de búsqueda
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🏨 ${alojamientosFiltrados.size} alojamientos encontrados para tu búsqueda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        if (esAdmin) {
                            Text(
                                text = "💼 Modo Administrador",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                
                // Lista de productos
                items(
                    items = alojamientosFiltrados,
                    key = { producto -> producto.id }
                ) { producto ->
                    PlantillaProducto(
                        producto = producto,
                        esAdmin = esAdmin,
                        onVerDetalle = { onVerDetalle(producto) },
                        onAgregarCarrito = {
                            if (usuario != null && !esAdmin) {
                                gestorCarrito.agregarProducto(producto)
                            } else if (usuario == null) {
                                onIrALogin()
                            }
                        },
                        onEditarProducto = {
                            onEditarProducto(producto)
                        },
                        onEliminarProducto = {
                            gestorProductos.removerAlojamientoDelCatalogo(producto)
                        },
                        usuarioLogueado = usuario != null,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                
                // Espaciado inferior
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}