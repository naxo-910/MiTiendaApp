package com.example.evparcial2.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.evparcial2.data.local.entities.EntidadReview
import com.example.evparcial2.data.model.CalificacionResumen
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.domain.viewmodels.ReviewEvent
import com.example.evparcial2.domain.viewmodels.ViewModelReviews
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SeccionReviews(
    reviews: List<EntidadReview>,
    resumenCalificacion: CalificacionResumen,
    usuario: Usuario?,
    productoId: Long,
    viewModelReviews: ViewModelReviews,
    nuevaReviewCalificacion: Int,
    nuevaReviewComentario: String,
    calificacionError: String?,
    comentarioError: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Resumen de calificaciones
        if (resumenCalificacion.totalReviews > 0) {
            ResumenCalificaciones(resumenCalificacion)
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Formulario para nueva review (solo si está logueado)
        if (usuario != null) {
            FormularioNuevaReview(
                calificacion = nuevaReviewCalificacion,
                comentario = nuevaReviewComentario,
                calificacionError = calificacionError,
                comentarioError = comentarioError,
                onCalificacionChange = { viewModelReviews.onEvent(ReviewEvent.CambiarCalificacion(it)) },
                onComentarioChange = { viewModelReviews.onEvent(ReviewEvent.CambiarComentario(it)) },
                onEnviar = { viewModelReviews.onEvent(ReviewEvent.EnviarReview(productoId, usuario)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Lista de reviews
        Text(
            "Opiniones (${reviews.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        if (reviews.isEmpty()) {
            Text(
                "Aún no hay opiniones para esta propiedad",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }
            }
        }
    }
}

@Composable
fun ResumenCalificaciones(resumen: CalificacionResumen) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "%.1f".format(resumen.promedioCalificacion),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    EstrellasDeBarra(calificacion = resumen.promedioCalificacion)
                }
                Text(
                    "${resumen.totalReviews} opiniones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // Aquí puedes agregar distribución de estrellas si quieres
        }
    }
}

@Composable
fun FormularioNuevaReview(
    calificacion: Int,
    comentario: String,
    calificacionError: String?,
    comentarioError: String?,
    onCalificacionChange: (Int) -> Unit,
    onComentarioChange: (String) -> Unit,
    onEnviar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Escribe tu opinión",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // Selector de estrellas
            Text("Calificación:")
            Row(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                repeat(5) { index ->
                    val estrella = index + 1
                    Icon(
                        imageVector = if (estrella <= calificacion) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "$estrella estrellas",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onCalificacionChange(estrella) },
                        tint = if (estrella <= calificacion) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (calificacionError != null) {
                Text(
                    calificacionError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Campo de comentario
            OutlinedTextField(
                value = comentario,
                onValueChange = onComentarioChange,
                label = { Text("Tu comentario") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                isError = comentarioError != null
            )
            if (comentarioError != null) {
                Text(
                    comentarioError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onEnviar,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Enviar Opinión")
            }
        }
    }
}

@Composable
fun ReviewCard(review: EntidadReview) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    review.nombreUsuario,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(Date(review.fecha)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            EstrellasDeBarra(calificacion = review.calificacion.toFloat())
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                review.comentario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun EstrellasDeBarra(calificacion: Float) {
    Row {
        repeat(5) { index ->
            val estrella = index + 1
            Icon(
                imageVector = if (estrella <= calificacion) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (estrella <= calificacion) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}