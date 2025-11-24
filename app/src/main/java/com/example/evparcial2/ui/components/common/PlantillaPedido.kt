package com.example.evparcial2.ui.components.common

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.evparcial2.data.model.Pedido
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PlantillaPedido(pedido: Pedido, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Pedido #${pedido.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "$${pedido.total}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("${pedido.productos.size} productos")
            Text("Estado: ${pedido.estado}")
            Text("Fecha: ${SimpleDateFormat("dd/MM/yyyy").format(Date(pedido.fecha))}")
        }
    }
}