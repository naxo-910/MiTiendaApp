package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.local.entities.EntidadReview
import com.example.evparcial2.data.local.repository.RepoReviews
import com.example.evparcial2.data.model.CalificacionResumen
import com.example.evparcial2.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewsUiState(
    val reviews: List<EntidadReview> = emptyList(),
    val resumenCalificacion: CalificacionResumen = CalificacionResumen(0f, 0, emptyMap()),
    val isLoading: Boolean = false,
    val nuevaReview: NuevaReview = NuevaReview()
)

data class NuevaReview(
    val calificacion: Int = 0,
    val comentario: String = "",
    val calificacionError: String? = null,
    val comentarioError: String? = null
)

sealed class ReviewEvent {
    data class CargarReviews(val productoId: Long) : ReviewEvent()
    data class CambiarCalificacion(val calificacion: Int) : ReviewEvent()
    data class CambiarComentario(val comentario: String) : ReviewEvent()
    data class EnviarReview(val productoId: Long, val usuario: Usuario) : ReviewEvent()
    object LimpiarFormulario : ReviewEvent()
}

class ViewModelReviews : ViewModel() {
    
    private val repoReviews = RepoReviews()
    
    private val _uiState = MutableStateFlow(ReviewsUiState())
    val uiState: StateFlow<ReviewsUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: ReviewEvent) {
        when (event) {
            is ReviewEvent.CargarReviews -> {
                cargarReviews(event.productoId)
            }
            is ReviewEvent.CambiarCalificacion -> {
                _uiState.value = _uiState.value.copy(
                    nuevaReview = _uiState.value.nuevaReview.copy(
                        calificacion = event.calificacion,
                        calificacionError = null
                    )
                )
            }
            is ReviewEvent.CambiarComentario -> {
                _uiState.value = _uiState.value.copy(
                    nuevaReview = _uiState.value.nuevaReview.copy(
                        comentario = event.comentario,
                        comentarioError = null
                    )
                )
            }
            is ReviewEvent.EnviarReview -> {
                enviarReview(event.productoId, event.usuario)
            }
            is ReviewEvent.LimpiarFormulario -> {
                _uiState.value = _uiState.value.copy(
                    nuevaReview = NuevaReview()
                )
            }
        }
    }
    
    private fun cargarReviews(productoId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Cargar reviews
            repoReviews.obtenerReviewsPorProducto(productoId).collect { reviews ->
                _uiState.value = _uiState.value.copy(
                    reviews = reviews,
                    isLoading = false
                )
            }
            
            // Cargar resumen de calificación
            repoReviews.obtenerResumenCalificacion(productoId).collect { resumen ->
                _uiState.value = _uiState.value.copy(
                    resumenCalificacion = resumen
                )
            }
        }
    }
    
    private fun enviarReview(productoId: Long, usuario: Usuario) {
        val nuevaReview = _uiState.value.nuevaReview
        
        // Validación
        val calificacionError = if (nuevaReview.calificacion == 0) "Debes seleccionar una calificación" else null
        val comentarioError = if (nuevaReview.comentario.isBlank()) "El comentario es obligatorio" else null
        
        if (calificacionError != null || comentarioError != null) {
            _uiState.value = _uiState.value.copy(
                nuevaReview = nuevaReview.copy(
                    calificacionError = calificacionError,
                    comentarioError = comentarioError
                )
            )
            return
        }
        
        viewModelScope.launch {
            val review = EntidadReview(
                productoId = productoId,
                usuarioId = usuario.id,
                nombreUsuario = usuario.nombre,
                calificacion = nuevaReview.calificacion,
                comentario = nuevaReview.comentario
            )
            
            repoReviews.insertarReview(review)
            
            // Limpiar formulario
            _uiState.value = _uiState.value.copy(
                nuevaReview = NuevaReview()
            )
            
            // Recargar reviews
            cargarReviews(productoId)
        }
    }
}