package com.example.evparcial2.ui.components.reviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.evparcial2.data.model.Review
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.domain.viewmodels.ResumenCalificacion
import com.example.evparcial2.domain.viewmodels.ReviewEvent
import com.example.evparcial2.domain.viewmodels.ViewModelReviews
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SeccionReviews(
    reviews: List<Review>,
    resumenCalificacion: ResumenCalificacion,
    viewModelReviews: ViewModelReviews,
    usuarioActual: Usuario?,
    productoId: Long,
    comentario: String,
    calificacion: Int,
    onComentarioChange: (String) -> Unit,
    onCalificacionChange: (Int) -> Unit,
    onSubmitReview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Título de la sección
        Text(
            text = "Reseñas y Calificaciones",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Resumen de calificaciones
        ResumenCalificacionCard(resumenCalificacion)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Formulario para nueva reseña (solo si hay usuario logueado)
        if (usuarioActual != null) {
            NuevaReviewForm(
                comentario = comentario,
                calificacion = calificacion,
                onComentarioChange = onComentarioChange,
                onCalificacionChange = onCalificacionChange,
                onSubmit = onSubmitReview
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Lista de reseñas
        if (reviews.isNotEmpty()) {
            Text(
                text = "Todas las reseñas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }
            }
        } else {
            Text(
                text = "Aún no hay reseñas para este producto",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }
    }
}

@Composable
private fun ResumenCalificacionCard(resumen: ResumenCalificacion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calificación promedio
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (resumen.totalReviews > 0) String.format("%.1f", resumen.promedioCalificacion) else "0.0",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < resumen.promedioCalificacion.toInt()) 
                                Color(0xFFFFD700) else 
                                MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Text(
                    text = "${resumen.totalReviews} reseña${if (resumen.totalReviews != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Distribución de estrellas
            if (resumen.totalReviews > 0) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    for (estrellas in 5 downTo 1) {
                        val cantidad = resumen.distribucionEstrellas[estrellas] ?: 0
                        val porcentaje = if (resumen.totalReviews > 0) cantidad.toFloat() / resumen.totalReviews else 0f
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "$estrellas",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(12.dp)
                            )
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                            
                            LinearProgressIndicator(
                                progress = porcentaje,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp)
                                    .height(8.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            
                            Text(
                                text = "$cantidad",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(20.dp),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NuevaReviewForm(
    comentario: String,
    calificacion: Int,
    onComentarioChange: (String) -> Unit,
    onCalificacionChange: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Escribe una reseña",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Selector de calificación
            Text(
                text = "Calificación:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                repeat(5) { index ->
                    IconButton(
                        onClick = { onCalificacionChange(index + 1) }
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Calificar con ${index + 1} estrella${if (index > 0) "s" else ""}",
                            tint = if (index < calificacion) Color(0xFFFFD700) else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            // Campo de comentario
            OutlinedTextField(
                value = comentario,
                onValueChange = onComentarioChange,
                label = { Text("Tu comentario") },
                placeholder = { Text("Comparte tu experiencia...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                maxLines = 4,
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón enviar
            Button(
                onClick = onSubmit,
                enabled = comentario.isNotBlank(),
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar Reseña")
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.nombreUsuario,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Row {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < review.calificacion) Color(0xFFFFD700) else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = review.comentario,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = formatearFecha(review.fechaCreacion),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatearFecha(timestamp: Long): String {
    val fecha = Date(timestamp)
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formato.format(fecha)
}