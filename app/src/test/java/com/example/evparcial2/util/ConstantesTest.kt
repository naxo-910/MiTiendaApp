package com.example.evparcial2.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConstantesTest {

    @Test
    fun `debe tener valores correctos para la base de datos`() {
        assertThat(Constantes.NOMBRE_BD).isEqualTo("ventas_arriendos_db")
        assertThat(Constantes.VERSION_BD).isEqualTo(1)
    }

    @Test
    fun `debe tener roles definidos correctamente`() {
        assertThat(Constantes.ROL_CLIENTE).isEqualTo("cliente")
        assertThat(Constantes.ROL_ADMIN).isEqualTo("admin")
    }

    @Test
    fun `debe tener estados de pedido definidos correctamente`() {
        assertThat(Constantes.ESTADO_PEDIDO_CONFIRMADO).isEqualTo("confirmado")
        assertThat(Constantes.ESTADO_PEDIDO_ENVIADO).isEqualTo("enviado")
        assertThat(Constantes.ESTADO_PEDIDO_ENTREGADO).isEqualTo("entregado")
    }

    @Test
    fun `debe tener valores mínimos correctos`() {
        assertThat(Constantes.PRECIO_MINIMO).isEqualTo(1.0)
        assertThat(Constantes.STOCK_MINIMO).isEqualTo(1)
    }

    @Test
    fun `debe tener tiempo de animación correcto`() {
        assertThat(Constantes.TIEMPO_ANIMACION).isEqualTo(300L)
    }

    @Test
    fun `precio mínimo debe ser mayor que cero`() {
        assertThat(Constantes.PRECIO_MINIMO).isGreaterThan(0.0)
    }

    @Test
    fun `stock mínimo debe ser mayor que cero`() {
        assertThat(Constantes.STOCK_MINIMO).isGreaterThan(0)
    }

    @Test
    fun `tiempo de animación debe ser positivo`() {
        assertThat(Constantes.TIEMPO_ANIMACION).isGreaterThan(0L)
    }
}