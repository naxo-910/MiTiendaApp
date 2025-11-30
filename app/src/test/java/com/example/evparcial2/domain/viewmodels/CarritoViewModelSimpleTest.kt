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
class CarritoViewModelSimpleTest {

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
    fun `agregar producto al carrito funciona`() = runTest {
        val seAgrego = viewModel.agregarProducto(productoDemo)
        
        assertThat(seAgrego).isTrue()
        val items = viewModel.items.first()
        assertThat(items).hasSize(1)
        assertThat(items["1"]?.producto?.nombre).isEqualTo("Habitación Individual")
    }

    @Test
    fun `agregar producto duplicado no lo agrega`() = runTest {
        // Agregar por primera vez
        val primerIntento = viewModel.agregarProducto(productoDemo)
        assertThat(primerIntento).isTrue()
        
        // Intentar agregar el mismo producto
        val segundoIntento = viewModel.agregarProducto(productoDemo)
        assertThat(segundoIntento).isFalse()
        
        val items = viewModel.items.first()
        assertThat(items).hasSize(1)
    }

    @Test
    fun `eliminar producto del carrito funciona`() = runTest {
        // Agregar producto
        viewModel.agregarProducto(productoDemo)
        var items = viewModel.items.first()
        assertThat(items).hasSize(1)
        
        // Eliminar producto
        viewModel.eliminarProducto("1")
        items = viewModel.items.first()
        assertThat(items).isEmpty()
    }

    @Test
    fun `vaciar carrito elimina todos los productos`() = runTest {
        // Agregar varios productos
        viewModel.agregarProducto(productoDemo)
        viewModel.agregarProducto(productoDemo.copy(id = 2L, nombre = "Producto 2"))
        
        var items = viewModel.items.first()
        assertThat(items).hasSize(2)
        
        // Vaciar carrito
        viewModel.vaciarCarrito()
        
        items = viewModel.items.first()
        assertThat(items).isEmpty()
    }

    @Test
    fun `confirmar pedido vacia el carrito`() = runTest {
        // Agregar producto
        viewModel.agregarProducto(productoDemo)
        var items = viewModel.items.first()
        assertThat(items).hasSize(1)
        
        // Confirmar pedido
        viewModel.confirmarPedido()
        
        items = viewModel.items.first()
        assertThat(items).isEmpty()
    }

    @Test
    fun `ItemCarrito calcula subtotal correctamente`() {
        val item = ItemCarrito(productoDemo, 3)
        val subtotal = item.subtotal
        
        assertThat(subtotal).isEqualTo(150000.0) // 50000 * 3
    }
}