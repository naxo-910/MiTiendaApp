package com.example.evparcial2.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.evparcial2.data.model.Producto
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.domain.viewmodels.ReviewEvent
import com.example.evparcial2.domain.viewmodels.ViewModelReviews
import com.example.evparcial2.ui.components.reviews.SeccionReviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    producto: Producto,
    esAdmin: Boolean,
    usuario: Usuario?,
    onVolver: () -> Unit,
    onAgregarCarrito: (Producto) -> Unit,
    onIniciarChat: () -> Unit = {},
    viewModelReviews: ViewModelReviews = hiltViewModel()
) {
    val reviewsUiState by viewModelReviews.uiState.collectAsState()
    
    // Cargar reviews cuando se abre la pantalla
    LaunchedEffect(producto.id) {
        viewModelReviews.onEvent(ReviewEvent.LoadReviewsForProduct(producto.id))
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        // Botón de volver flotante
        IconButton(
            onClick = onVolver,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, 
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        // Botones flotantes modernos
        if (!esAdmin && usuario != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                // Botón de chat
                FloatingActionButton(
                    onClick = onIniciarChat,
                    modifier = Modifier.size(56.dp),
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat")
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Botón de agregar al carrito
                FloatingActionButton(
                    onClick = { onAgregarCarrito(producto) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Agregar al carrito")
                }
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Card de imagen principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
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
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = "Sin imagen",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Card de información principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        producto.nombre, 
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "$${String.format("%,.0f", producto.precio)} CLP/noche",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        "Descripción", 
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        producto.descripcion, 
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Card de detalles técnicos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Información Adicional",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    InfoDetalleSimple("Categoría: ${producto.categoria}")
                    InfoDetalleSimple("Stock disponible: ${producto.stock}")
                    if (esAdmin) {
                        InfoDetalleSimple("ID: ${producto.id}")
                    }
                }
            }
                
            Spacer(modifier = Modifier.height(24.dp))
                
            // Sección de Reviews
            SeccionReviews(
                reviews = reviewsUiState.reviews,
                resumenCalificacion = reviewsUiState.resumenCalificacion,
                viewModelReviews = viewModelReviews,
                usuarioActual = usuario,
                productoId = producto.id,
                comentario = reviewsUiState.nuevaReview.comentario,
                calificacion = reviewsUiState.nuevaReview.calificacion,
                onComentarioChange = { viewModelReviews.onEvent(ReviewEvent.OnComentarioChange(it)) },
                onCalificacionChange = { viewModelReviews.onEvent(ReviewEvent.OnCalificacionChange(it)) },
                onSubmitReview = {
                    if (usuario != null) {
                        viewModelReviews.onEvent(ReviewEvent.OnSubmitReview(producto.id, usuario))
                    }
                }
            )
        }
    }
}

@Composable
fun InfoDetalleSimple(texto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(texto)
    }
}