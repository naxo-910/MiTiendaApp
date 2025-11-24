package com.example.evparcial2.data.local.repository

import com.example.evparcial2.data.local.dao.DaoReview
import com.example.evparcial2.data.local.entities.EntidadReview
import com.example.evparcial2.data.model.CalificacionResumen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepoReviews(private val daoReview: DaoReview = DaoReview()) {

    suspend fun insertarReview(review: EntidadReview): Long {
        return daoReview.insertar(review)
    }

    fun obtenerReviewsPorProducto(productoId: Long): Flow<List<EntidadReview>> {
        return daoReview.obtenerPorProducto(productoId)
    }

    fun obtenerResumenCalificacion(productoId: Long): Flow<CalificacionResumen> {
        return daoReview.obtenerPorProducto(productoId).map { reviews ->
            if (reviews.isEmpty()) {
                CalificacionResumen(
                    promedioCalificacion = 0f,
                    totalReviews = 0,
                    distribucionEstrellas = emptyMap()
                )
            } else {
                val promedio = reviews.map { it.calificacion }.average().toFloat()
                val distribucion = reviews.groupBy { it.calificacion }
                    .mapValues { it.value.size }
                
                CalificacionResumen(
                    promedioCalificacion = promedio,
                    totalReviews = reviews.size,
                    distribucionEstrellas = distribucion
                )
            }
        }
    }

    fun obtenerTodasReviews(): Flow<List<EntidadReview>> {
        return daoReview.obtenerTodas()
    }

    suspend fun aprobarReview(reviewId: Long) {
        daoReview.aprobar(reviewId)
    }

    suspend fun rechazarReview(reviewId: Long) {
        daoReview.rechazar(reviewId)
    }

    suspend fun eliminarReview(reviewId: Long) {
        daoReview.eliminar(reviewId)
    }
}