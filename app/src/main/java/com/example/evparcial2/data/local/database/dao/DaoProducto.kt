package com.example.evparcial2.data.local.dao

import com.example.evparcial2.data.local.entities.EntidadProducto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DaoProducto {
    private val productos = mutableListOf<EntidadProducto>()

    // productos de ejemplo
    init {
        productos.addAll(listOf(
            EntidadProducto(
                id = 1,
                nombre = "Casa Familiar Norte",
                descripcion = "Hermosa casa de 3 dormitorios, 2 baños, con jardín y garage",
                precio = 250000.0,
                stock = 1,
                categoria = "Venta",
                tipo = "venta"
            ),
            EntidadProducto(
                id = 2,
                nombre = "Departamento Centro",
                descripcion = "Departamento moderno de 2 dormitorios, 1 baño, amoblado",
                precio = 1200.0,
                stock = 5,
                categoria = "Arriendo",
                tipo = "arriendo"
            ),
            EntidadProducto(
                id = 3,
                nombre = "Oficina Comercial",
                descripcion = "Oficina en zona comercial, 60m², ideal para emprendimientos",
                precio = 800.0,
                stock = 3,
                categoria = "Arriendo",
                tipo = "arriendo"
            ),
            EntidadProducto(
                id = 4,
                nombre = "Casa Playa",
                descripcion = "Casa frente al mar, 4 dormitorios, piscina, vista panorámica",
                precio = 450000.0,
                stock = 1,
                categoria = "Venta",
                tipo = "venta"
            ),
            EntidadProducto(
                id = 5,
                nombre = "Local Comercial",
                descripcion = "Local en centro comercial, alto tráfico, 40m²",
                precio = 1500.0,
                stock = 2,
                categoria = "Arriendo",
                tipo = "arriendo"
            )
        ))
    }

    suspend fun insertar(producto: EntidadProducto): Long {
        val nuevoId = (productos.maxByOrNull { it.id }?.id ?: 0) + 1
        productos.add(producto.copy(id = nuevoId))
        return nuevoId
    }

    fun obtenerTodos(): Flow<List<EntidadProducto>> = flowOf(productos)

    suspend fun obtenerPorId(id: Long): EntidadProducto? {
        return productos.find { it.id == id }
    }

    fun buscar(query: String): Flow<List<EntidadProducto>> {
        val resultados = productos.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true)
        }
        return flowOf(resultados)
    }

    fun obtenerPorTipo(tipo: String): Flow<List<EntidadProducto>> {
        val resultados = productos.filter { it.tipo == tipo }
        return flowOf(resultados)
    }

    suspend fun actualizar(producto: EntidadProducto) {
        val index = productos.indexOfFirst { it.id == producto.id }
        if (index != -1) {
            productos[index] = producto
        }
    }

    suspend fun eliminar(producto: EntidadProducto) {
        productos.removeAll { it.id == producto.id }
    }
}