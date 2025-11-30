package com.example.evparcial2.domain.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.evparcial2.data.model.Producto
import com.example.evparcial2.data.model.ItemCarrito
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CarritoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CarritoViewModel

    private val productoDemo = Producto(
        id = 1L,
        nombre = "Habitación Individual",
        descripcion = "Habitación cómoda para una persona",
        precio = 50000.0,
        stock = 10,
        categoria = "individual",
        tipo = "habitacion"
    )

    private val productoDemo2 = Producto(
        id = 2L,
        nombre = "Habitación Doble",
        descripcion = "Habitación para dos personas",
        precio = 80000.0,
        stock = 5,
        categoria = "doble",
        tipo = "habitacion"
    )

    @Before
    fun setUp() {
        viewModel = CarritoViewModel()
    }

    @Test
    fun `carrito inicial esta vacio`() = runTest {
        val items = viewModel.items.first()
        assertThat(items).isEmpty()
    }

    @Test
    fun `agregar producto nuevo al carrito retorna true`() = runTest {
        val resultado = viewModel.agregarProducto(productoDemo)
        
        assertThat(resultado).isTrue()
        
        val items = viewModel.items.first()
        assertThat(items).hasSize(1)
        assertThat(items["1"]?.producto).isEqualTo(productoDemo)
        assertThat(items["1"]?.cantidad).isEqualTo(1)
    }

    @Test
    fun `agregar producto existente no lo duplica y retorna false`() = runTest {
        // Agregar producto por primera vez
        viewModel.agregarProducto(productoDemo)
        
        // Intentar agregar el mismo producto
        val resultado = viewModel.agregarProducto(productoDemo)
        
        assertThat(resultado).isFalse()
        
        val items = viewModel.items.first()
        assertThat(items).hasSize(1) // Solo un item
        assertThat(items["1"]?.cantidad).isEqualTo(1) // Cantidad sigue siendo 1
    }

    @Test
    fun `agregar multiples productos diferentes funciona correctamente`() = runTest {
        viewModel.agregarProducto(productoDemo)
        viewModel.agregarProducto(productoDemo2)
        
        val items = viewModel.items.first()
        assertThat(items).hasSize(2)
        assertThat(items["1"]?.producto).isEqualTo(productoDemo)
        assertThat(items["2"]?.producto).isEqualTo(productoDemo2)
        assertThat(items["1"]?.cantidad).isEqualTo(1)
        assertThat(items["2"]?.cantidad).isEqualTo(1)
    }

    @Test
    fun `eliminar producto del carrito funciona correctamente`() = runTest {
        // Agregar productos
        viewModel.agregarProducto(productoDemo)
        viewModel.agregarProducto(productoDemo2)
        
        // Verificar que están en el carrito
        var items = viewModel.items.first()
        assertThat(items).hasSize(2)
        
        // Eliminar uno
        viewModel.eliminarProducto("1")
        
        // Verificar que se eliminó
        items = viewModel.items.first()
        assertThat(items).hasSize(1)
        assertThat(items["1"]).isNull()
        assertThat(items["2"]).isNotNull()
    }

    @Test
    fun `eliminar producto inexistente no causa error`() = runTest {
        // Carrito vacío
        viewModel.eliminarProducto("999")
        
        val items = viewModel.items.first()
        assertThat(items).isEmpty()
    }

    @Test
    fun `vaciar carrito elimina todos los items`() = runTest {
        // Agregar productos
        viewModel.agregarProducto(productoDemo)
        viewModel.agregarProducto(productoDemo2)
        
        // Verificar que hay items
        var items = viewModel.items.first()
        assertThat(items).hasSize(2)
        
        // Vaciar carrito
        viewModel.vaciarCarrito()
        
        // Verificar que está vacío
        items = viewModel.items.first()
        assertThat(items).isEmpty()
    }

    @Test
    fun `confirmar pedido vacia el carrito`() = runTest {
        // Agregar productos
        viewModel.agregarProducto(productoDemo)
        viewModel.agregarProducto(productoDemo2)
        
        // Verificar que hay items
        var items = viewModel.items.first()
        assertThat(items).hasSize(2)
        
        // Confirmar pedido
        viewModel.confirmarPedido()
        
        // Verificar que se vació el carrito
        items = viewModel.items.first()
        assertThat(items).isEmpty()
    }

    @Test
    fun `ItemCarrito calcula subtotal correctamente`() {
        val item = ItemCarrito(productoDemo, 3)
        val subtotal = item.subtotal
        
        assertThat(subtotal).isEqualTo(150000.0) // 50000 * 3
    }

    @Test
    fun `ItemCarrito con cantidad cero tiene subtotal cero`() {
        val item = ItemCarrito(productoDemo, 0)
        val subtotal = item.subtotal
        
        assertThat(subtotal).isEqualTo(0.0)
    }

    @Test
    fun `flow de items se actualiza correctamente al agregar productos`() = runTest {
        var itemsActuales = viewModel.items.first()
        assertThat(itemsActuales).isEmpty()
        
        viewModel.agregarProducto(productoDemo)
        
        itemsActuales = viewModel.items.first()
        assertThat(itemsActuales).hasSize(1)
        assertThat(itemsActuales.containsKey("1")).isTrue()
    }
}