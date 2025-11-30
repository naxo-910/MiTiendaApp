package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.model.Review
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.data.repository.BasicRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- EVENTOS DE REVIEW ---
sealed class ReviewEvent {
    data class OnComentarioChange(val comentario: String) : ReviewEvent()
    data class OnCalificacionChange(val calificacion: Int) : ReviewEvent()
    data class OnSubmitReview(val productoId: Long, val usuario: Usuario) : ReviewEvent()
    data class LoadReviewsForProduct(val productoId: Long) : ReviewEvent()
}

// --- ESTADO PARA NUEVA REVIEW ---
data class NuevaReview(
    val comentario: String = "",
    val calificacion: Int = 5,
    val comentarioError: String? = null
)

// --- RESUMEN DE CALIFICACIONES ---
data class ResumenCalificacion(
    val promedioCalificacion: Double = 0.0,
    val totalReviews: Int = 0,
    val distribucionEstrellas: Map<Int, Int> = emptyMap()
)

// --- UI STATE REVIEWS ---
data class ReviewsUiState(
    val reviews: List<Review> = emptyList(),
    val nuevaReview: NuevaReview = NuevaReview(),
    val resumenCalificacion: ResumenCalificacion = ResumenCalificacion(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ViewModelReviews @Inject constructor(
    private val repository: BasicRepositories
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReviewsUiState())
    val uiState: StateFlow<ReviewsUiState> = _uiState.asStateFlow()
    
    fun onEvent(evento: ReviewEvent) {
        when (evento) {
            is ReviewEvent.OnComentarioChange -> {
                _uiState.value = _uiState.value.copy(
                    nuevaReview = _uiState.value.nuevaReview.copy(
                        comentario = evento.comentario,
                        comentarioError = null
                    )
                )
            }
            is ReviewEvent.OnCalificacionChange -> {
                _uiState.value = _uiState.value.copy(
                    nuevaReview = _uiState.value.nuevaReview.copy(
                        calificacion = evento.calificacion
                    )
                )
            }
            is ReviewEvent.OnSubmitReview -> {
                submitReview(evento.productoId, evento.usuario)
            }
            is ReviewEvent.LoadReviewsForProduct -> {
                loadReviewsForProduct(evento.productoId)
            }
        }
    }
    
    private fun submitReview(productoId: Long, usuario: Usuario) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val nuevaReview = currentState.nuevaReview
            
            // Validación
            if (nuevaReview.comentario.isBlank()) {
                _uiState.value = currentState.copy(
                    nuevaReview = nuevaReview.copy(
                        comentarioError = "El comentario es requerido"
                    )
                )
                return@launch
            }
            
            if (nuevaReview.comentario.length < 10) {
                _uiState.value = currentState.copy(
                    nuevaReview = nuevaReview.copy(
                        comentarioError = "El comentario debe tener al menos 10 caracteres"
                    )
                )
                return@launch
            }
            
            _uiState.value = currentState.copy(isLoading = true)
            
            try {
                val review = Review(
                    id = System.currentTimeMillis(),
                    productoId = productoId,
                    usuarioId = usuario.id,
                    nombreUsuario = usuario.nombre,
                    comentario = nuevaReview.comentario,
                    calificacion = nuevaReview.calificacion,
                    fechaCreacion = System.currentTimeMillis()
                )
                
                val reviewCreada = repository.crearReview(review)
                
                // Recargar reviews
                loadReviewsForProduct(productoId)
                
                // Reset form
                _uiState.value = _uiState.value.copy(
                    nuevaReview = NuevaReview(),
                    isLoading = false
                )
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    error = "Error al enviar la reseña: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    private fun loadReviewsForProduct(productoId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val reviews = repository.getReviewsPorProducto(productoId)
                val resumen = calcularResumenCalificacion(reviews)
                
                _uiState.value = _uiState.value.copy(
                    reviews = reviews,
                    resumenCalificacion = resumen,
                    isLoading = false,
                    error = null
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar reseñas: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    private fun calcularResumenCalificacion(reviews: List<Review>): ResumenCalificacion {
        if (reviews.isEmpty()) {
            return ResumenCalificacion()
        }
        
        val totalReviews = reviews.size
        val promedioCalificacion = reviews.map { it.calificacion }.average()
        val distribucionEstrellas = reviews
            .groupBy { it.calificacion }
            .mapValues { it.value.size }
        
        return ResumenCalificacion(
            promedioCalificacion = promedioCalificacion,
            totalReviews = totalReviews,
            distribucionEstrellas = distribucionEstrellas
        )
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}