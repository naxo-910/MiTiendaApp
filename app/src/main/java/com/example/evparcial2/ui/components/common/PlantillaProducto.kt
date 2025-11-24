package com.example.evparcial2.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.evparcial2.data.model.Producto
import com.example.evparcial2.domain.viewmodels.ReviewEvent
import com.example.evparcial2.domain.viewmodels.ViewModelReviews

@Composable
fun PlantillaProducto(
    producto: Producto,
    esAdmin: Boolean,
    onVerDetalle: () -> Unit,
    onAgregarCarrito: () -> Unit,
    onEditarProducto: () -> Unit,
    onEliminarProducto: () -> Unit,
    modifier: Modifier = Modifier,
    usuarioLogueado: Boolean = true,
    viewModelReviews: ViewModelReviews = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val reviewsUiState by viewModelReviews.uiState.collectAsState()
    
    // Cargar calificación cuando se crea el componente
    LaunchedEffect(producto.id) {
        viewModelReviews.onEvent(ReviewEvent.CargarReviews(producto.id))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onVerDetalle() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            ) {
                if (producto.imagenUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(producto.imagenUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = producto.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder si no hay imagen
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Image,
                            contentDescription = "Sin imagen",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            producto.nombre,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "${producto.ciudad}, ${producto.pais}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "$${String.format("%,.0f", producto.precio)} CLP/noche",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        // Mostrar calificación promedio
                        if (reviewsUiState.resumenCalificacion.totalReviews > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFFFD700)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "%.1f".format(reviewsUiState.resumenCalificacion.promedioCalificacion),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    " (${reviewsUiState.resumenCalificacion.totalReviews})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (!esAdmin && usuarioLogueado) {
                        Button(
                            onClick = onAgregarCarrito,
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Reservar", style = MaterialTheme.typography.bodyMedium)
                        }
                    } else if (!esAdmin && !usuarioLogueado) {
                        Button(
                            onClick = onAgregarCarrito,
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Iniciar para Reservar", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                producto.descripcion,
                maxLines = if (expanded) 10 else 2,
                overflow = TextOverflow.Ellipsis
            )

            if (producto.descripcion.length > 50) {
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Ver menos" else "Ver más")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (esAdmin) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Stock: ${producto.stock}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (producto.stock == 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )

                    Row {
                        IconButton(onClick = onEditarProducto) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = onEliminarProducto) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
}