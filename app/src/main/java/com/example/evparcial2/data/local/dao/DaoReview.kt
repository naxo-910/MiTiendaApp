package com.example.evparcial2.data.local.dao

import com.example.evparcial2.data.local.entities.EntidadReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DaoReview {
    private val reviews = mutableListOf<EntidadReview>()

    // Reviews de ejemplo
    init {
        reviews.addAll(listOf(
            EntidadReview(
                id = 1,
                productoId = 1L,
                usuarioId = 1L,
                nombreUsuario = "María González",
                calificacion = 5,
                comentario = "Excelente propiedad, muy bien ubicada y en perfecto estado. La recomiendo totalmente.",
                fecha = System.currentTimeMillis() - 86400000 // Hace 1 día
            ),
            EntidadReview(
                id = 2,
                productoId = 1L,
                usuarioId = 2L,
                nombreUsuario = "Carlos Ruiz",
                calificacion = 4,
                comentario = "Muy buena casa, solo le faltaría un poco más de estacionamiento.",
                fecha = System.currentTimeMillis() - 172800000 // Hace 2 días
            ),
            EntidadReview(
                id = 3,
                productoId = 2L,
                usuarioId = 3L,
                nombreUsuario = "Ana López",
                calificacion = 5,
                comentario = "Departamento hermoso, muy moderno y con excelentes amenidades.",
                fecha = System.currentTimeMillis() - 259200000 // Hace 3 días
            )
        ))
    }

    suspend fun insertar(review: EntidadReview): Long {
        val nuevoId = (reviews.maxByOrNull { it.id }?.id ?: 0) + 1
        reviews.add(review.copy(id = nuevoId))
        return nuevoId
    }

    fun obtenerPorProducto(productoId: Long): Flow<List<EntidadReview>> {
        val reviewsProducto = reviews.filter { it.productoId == productoId && it.aprobado }
        return flowOf(reviewsProducto.sortedByDescending { it.fecha })
    }

    fun obtenerTodas(): Flow<List<EntidadReview>> = flowOf(reviews)

    suspend fun obtenerPorId(id: Long): EntidadReview? {
        return reviews.find { it.id == id }
    }

    suspend fun actualizar(review: EntidadReview) {
        val index = reviews.indexOfFirst { it.id == review.id }
        if (index != -1) {
            reviews[index] = review
        }
    }

    suspend fun eliminar(reviewId: Long) {
        reviews.removeAll { it.id == reviewId }
    }

    // Para moderación (admin)
    suspend fun aprobar(reviewId: Long) {
        val review = obtenerPorId(reviewId)
        review?.let {
            actualizar(it.copy(aprobado = true))
        }
    }

    suspend fun rechazar(reviewId: Long) {
        val review = obtenerPorId(reviewId)
        review?.let {
            actualizar(it.copy(aprobado = false))
        }
    }
}