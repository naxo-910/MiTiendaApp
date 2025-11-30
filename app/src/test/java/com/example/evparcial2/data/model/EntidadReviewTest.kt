package com.example.evparcial2.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EntidadReviewTest {

    @Test
    fun `debe crear EntidadReview correctamente`() {
        val review = EntidadReview(
            id = 1L,
            calificacion = 4.5f,
            comentario = "Excelente lugar",
            fecha = "2024-01-15",
            nombreUsuario = "María González",
            productoId = 100L
        )

        assertThat(review.id).isEqualTo(1L)
        assertThat(review.calificacion).isEqualTo(4.5f)
        assertThat(review.comentario).isEqualTo("Excelente lugar")
        assertThat(review.fecha).isEqualTo("2024-01-15")
        assertThat(review.nombreUsuario).isEqualTo("María González")
        assertThat(review.productoId).isEqualTo(100L)
    }

    @Test
    fun `debe validar calificación en rango válido`() {
        val review = EntidadReview(
            id = 1L,
            calificacion = 3.7f,
            comentario = "Bueno",
            fecha = "2024-01-15",
            nombreUsuario = "Usuario",
            productoId = 100L
        )

        assertThat(review.calificacion).isAtLeast(0f)
        assertThat(review.calificacion).isAtMost(5f)
    }

    @Test
    fun `dos EntidadReview con mismos datos deben ser iguales`() {
        val review1 = EntidadReview(1L, 4.0f, "Buen servicio", "2024-01-15", "Juan", 100L)
        val review2 = EntidadReview(1L, 4.0f, "Buen servicio", "2024-01-15", "Juan", 100L)

        assertThat(review1).isEqualTo(review2)
        assertThat(review1.hashCode()).isEqualTo(review2.hashCode())
    }

    @Test
    fun `copy debe funcionar correctamente`() {
        val reviewOriginal = EntidadReview(
            id = 1L,
            calificacion = 3.0f,
            comentario = "Regular",
            fecha = "2024-01-15",
            nombreUsuario = "Usuario",
            productoId = 100L
        )

        val reviewModificado = reviewOriginal.copy(
            calificacion = 5.0f,
            comentario = "Excelente después de mejoras"
        )

        assertThat(reviewModificado.calificacion).isEqualTo(5.0f)
        assertThat(reviewModificado.comentario).isEqualTo("Excelente después de mejoras")
        assertThat(reviewModificado.id).isEqualTo(reviewOriginal.id)
        assertThat(reviewModificado.nombreUsuario).isEqualTo(reviewOriginal.nombreUsuario)
    }

    @Test
    fun `toString debe contener información del review`() {
        val review = EntidadReview(
            id = 1L,
            calificacion = 4.2f,
            comentario = "Muy bueno",
            fecha = "2024-01-15",
            nombreUsuario = "Test User",
            productoId = 100L
        )

        val toString = review.toString()
        assertThat(toString).contains("4.2")
        assertThat(toString).contains("Muy bueno")
        assertThat(toString).contains("Test User")
        assertThat(toString).contains("100")
    }

    @Test
    fun `debe manejar calificación cero`() {
        val review = EntidadReview(
            id = 1L,
            calificacion = 0f,
            comentario = "Muy malo",
            fecha = "2024-01-15",
            nombreUsuario = "Usuario",
            productoId = 100L
        )

        assertThat(review.calificacion).isEqualTo(0f)
    }

    @Test
    fun `debe manejar calificación máxima`() {
        val review = EntidadReview(
            id = 1L,
            calificacion = 5f,
            comentario = "Perfecto",
            fecha = "2024-01-15",
            nombreUsuario = "Usuario",
            productoId = 100L
        )

        assertThat(review.calificacion).isEqualTo(5f)
    }
}

class CalificacionResumenTest {

    @Test
    fun `debe crear CalificacionResumen correctamente`() {
        val resumen = CalificacionResumen(
            promedioCalificacion = 4.3f,
            totalReviews = 150
        )

        assertThat(resumen.promedioCalificacion).isEqualTo(4.3f)
        assertThat(resumen.totalReviews).isEqualTo(150)
    }

    @Test
    fun `debe manejar cero reviews`() {
        val resumen = CalificacionResumen(
            promedioCalificacion = 0f,
            totalReviews = 0
        )

        assertThat(resumen.promedioCalificacion).isEqualTo(0f)
        assertThat(resumen.totalReviews).isEqualTo(0)
    }

    @Test
    fun `debe manejar un solo review`() {
        val resumen = CalificacionResumen(
            promedioCalificacion = 5f,
            totalReviews = 1
        )

        assertThat(resumen.promedioCalificacion).isEqualTo(5f)
        assertThat(resumen.totalReviews).isEqualTo(1)
    }

    @Test
    fun `dos CalificacionResumen con mismos datos deben ser iguales`() {
        val resumen1 = CalificacionResumen(4.5f, 200)
        val resumen2 = CalificacionResumen(4.5f, 200)

        assertThat(resumen1).isEqualTo(resumen2)
        assertThat(resumen1.hashCode()).isEqualTo(resumen2.hashCode())
    }

    @Test
    fun `copy debe funcionar correctamente`() {
        val resumenOriginal = CalificacionResumen(4.0f, 100)
        val resumenModificado = resumenOriginal.copy(promedioCalificacion = 4.2f)

        assertThat(resumenModificado.promedioCalificacion).isEqualTo(4.2f)
        assertThat(resumenModificado.totalReviews).isEqualTo(resumenOriginal.totalReviews)
    }

    @Test
    fun `debe validar promedio en rango válido`() {
        val resumen = CalificacionResumen(3.8f, 75)

        assertThat(resumen.promedioCalificacion).isAtLeast(0f)
        assertThat(resumen.promedioCalificacion).isAtMost(5f)
    }

    @Test
    fun `debe validar total de reviews no negativo`() {
        val resumen = CalificacionResumen(4.1f, 50)

        assertThat(resumen.totalReviews).isAtLeast(0)
    }

    @Test
    fun `toString debe contener información del resumen`() {
        val resumen = CalificacionResumen(4.7f, 300)
        val toString = resumen.toString()

        assertThat(toString).contains("4.7")
        assertThat(toString).contains("300")
    }
}