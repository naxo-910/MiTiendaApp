package com.example.evparcial2.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.evparcial2.domain.viewmodels.CarritoViewModel
import com.example.evparcial2.data.model.ItemCarrito
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(
    carritoViewModel: CarritoViewModel,
    onVolver: () -> Unit,
    onConfirmar: () -> Unit
) {
    val itemsMap by carritoViewModel.items.collectAsState()
    val itemsList = itemsMap.values.toList()

    val totalCarrito = itemsList.sumOf { it.producto.precio * it.cantidad }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis Reservas") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (itemsList.isNotEmpty()) {
                        IconButton(onClick = { carritoViewModel.vaciarCarrito() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Vaciar reservas")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (itemsList.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Total:", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "$${String.format("%,.0f", totalCarrito)} CLP",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Button(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "¡Reserva confirmada con éxito!"
                                        )
                                        delay(1500)
                                        onConfirmar()
                                    }
                                },
                                modifier = Modifier.width(180.dp)
                            ) {
                                Text("Confirmar Reserva")
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->

        if (itemsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(), 
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes reservas pendientes", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    items = itemsList,
                    key = { it.producto.id }
                ) { item ->
                    ItemCarritoCard(
                        item = item,
                        onEliminar = {
                            carritoViewModel.eliminarProducto(item.producto.id.toString())
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ItemCarritoCard(
    item: ItemCarrito,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(
                    "$${String.format("%,.0f", item.producto.precio)} CLP/noche",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}