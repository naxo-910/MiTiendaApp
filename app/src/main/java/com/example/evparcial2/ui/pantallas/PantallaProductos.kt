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
fun PantallaProductos(
    vm: ViewModelProductos,
    carritoViewModel: CarritoViewModel,
    esAdmin: Boolean,
    usuario: com.example.evparcial2.data.model.Usuario? = null,
    onAgregarProducto: () -> Unit,
    onEditarProducto: (Producto) -> Unit,
    onVerDetalle: (Producto) -> Unit,
    onVolver: () -> Unit,
    onIrACarrito: () -> Unit,
    onIrALogin: () -> Unit = {},
    onIrAInicio: () -> Unit = {}
) {
    val productos by vm.productos.collectAsState()
    val itemsCarrito by carritoViewModel.items.collectAsState()
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    var filtroCiudad by remember { mutableStateOf("Todas") }
    var filtroPais by remember { mutableStateOf("Todos") }
    
    // Filtrar productos según tipo de habitación, ciudad y país
    val productosFiltrados = remember(productos, filtroSeleccionado, filtroCiudad, filtroPais) {
        productos.filter { producto ->
            val cumpleTipo = when (filtroSeleccionado) {
                "Todos" -> true
                "Privada" -> producto.tipo == "privada" || producto.tipo == "suite"
                "Compartida" -> producto.tipo == "compartida" || producto.tipo == "litera"
                "Familiar" -> producto.tipo == "familiar" || producto.tipo == "doble"
                else -> true
            }
            val cumpleCiudad = filtroCiudad == "Todas" || producto.ciudad == filtroCiudad
            val cumplePais = filtroPais == "Todos" || producto.pais == filtroPais
            
            cumpleTipo && cumpleCiudad && cumplePais
        }
    }
    
    // Obtener listas únicas de ciudades y países
    val ciudadesDisponibles = remember(productos) {
        listOf("Todas") + productos.map { it.ciudad }.distinct().sorted()
    }
    val paisesDisponibles = remember(productos) {
        listOf("Todos") + productos.map { it.pais }.distinct().sorted()
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
                        // Mostrar nombre del usuario
                        Text(
                            text = "Hola, ${usuario.nombre}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        if (!esAdmin) {
                            // Botón de carrito con CONTADOR
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
                                        contentDescription = "Ver carrito",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    } else {
                        // Botón de login prominente
                        Button(
                            onClick = onIrALogin,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Iniciar Sesión")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (esAdmin) {
                FloatingActionButton(onClick = onAgregarProducto) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo Alojamiento")
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
                Text("No hay alojamientos disponibles")
                if (esAdmin) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onAgregarProducto) {
                        Text("Agregar Primer Alojamiento")
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
                // Header con gradiente
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
                                text = if (esAdmin) "🏨 Gestión de Alojamientos" else "🏨 Explora Alojamientos",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (esAdmin) 
                                    "Administra tu inventario de alojamientos"
                                else 
                                    "Encuentra tu estancia perfecta",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
                
                // Filtros
                if (!esAdmin) {
                    item {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Filtros por tipo
                            Text(
                                text = "Tipo de Habitación",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                val filtros = listOf("Todos", "Privada", "Compartida", "Familiar")
                                items(filtros) { filtro ->
                                    FilterChip(
                                        onClick = { filtroSeleccionado = filtro },
                                        label = { Text(filtro) },
                                        selected = filtroSeleccionado == filtro,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                }
                            }
                            
                            // Filtros por país
                            Text(
                                text = "País",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                items(paisesDisponibles) { pais ->
                                    FilterChip(
                                        onClick = { 
                                            filtroPais = pais
                                            // Reset ciudad filter when changing country
                                            if (pais != "Todos") filtroCiudad = "Todas"
                                        },
                                        label = { Text(pais) },
                                        selected = filtroPais == pais,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    )
                                }
                            }
                            
                            // Filtros por ciudad
                            Text(
                                text = "Ciudad",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val ciudadesFiltradas = if (filtroPais == "Todos") {
                                    ciudadesDisponibles
                                } else {
                                    listOf("Todas") + productos.filter { it.pais == filtroPais }
                                        .map { it.ciudad }.distinct().sorted()
                                }
                                
                                items(ciudadesFiltradas) { ciudad ->
                                    FilterChip(
                                        onClick = { filtroCiudad = ciudad },
                                        label = { Text(ciudad) },
                                        selected = filtroCiudad == ciudad,
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
                
                // Contador de resultados
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${productosFiltrados.size} alojamientos disponibles",
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
                    items = productosFiltrados,
                    key = { producto -> producto.id }
                ) { producto ->
                    PlantillaProducto(
                        producto = producto,
                        esAdmin = esAdmin,
                        onVerDetalle = { onVerDetalle(producto) },
                        onAgregarCarrito = {
                            if (usuario != null && !esAdmin) {
                                carritoViewModel.agregarProducto(producto)
                            } else if (usuario == null) {
                                onIrALogin()
                            }
                        },
                        onEditarProducto = {
                            onEditarProducto(producto)
                        },
                        onEliminarProducto = {
                            vm.eliminarProducto(producto)
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