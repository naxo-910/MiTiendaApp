package com.example.evparcial2.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.evparcial2.domain.viewmodels.ViewModelPedidos // ¡IMPORTANTE!
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPedidos(
    vmPedidos: ViewModelPedidos, // <-- ¡Recibimos el VM!
    onVolver: () -> Unit
) {
    // Observamos el estado (pedidos, isLoading)
    val uiState by vmPedidos.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        when {
            // 1. Estado de Carga
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // 2. Estado Vacío
            uiState.pedidos.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tienes pedidos aún. Realiza tu primera compra.")
                }
            }

            // 3. Estado con Pedidos
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.pedidos, key = { it.id }) { pedido ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    "Pedido #${pedido.id}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    // --- ¡¡ARREGLO!! Usamos 'fechaPedido' ---
                                    "Fecha: ${pedido.fechaPedido.toFormattedDate()}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    // --- ¡¡ARREGLO!! Usamos 'productosJson' ---
                                    "Resumen: ${pedido.productosJson}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Total: $${pedido.total}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Pequeña función de ayuda para formatear la fecha
private fun Long.toFormattedDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd 'de' MMMM, yyyy 'a las' HH:mm", Locale.getDefault())
    return format.format(date)
}
