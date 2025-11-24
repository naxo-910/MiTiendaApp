package com.example.evparcial2.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.evparcial2.domain.viewmodels.ViewModelProductos
import com.example.evparcial2.ui.components.common.CampoTexto
// import com.example.evparcial2.ui.components.common.BotonCargando // Ya no lo usamos
import kotlinx.coroutines.flow.collectLatest // <-- ¡IMPORT AÑADIDO!

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormProducto(
    vm: ViewModelProductos,
    productoId: Long?,
    onVolver: () -> Unit,
    onGuardar: () -> Unit
) {
    val uiState by vm.uiStateForm.collectAsState()
    val isLoading by vm.isLoadingForm.collectAsState()

    // Carga el producto si estamos editando
    LaunchedEffect(productoId) {
        vm.cargarProducto(productoId)
    }

    // Observa los eventos de guardado
    LaunchedEffect(key1 = Unit) {
        vm.eventoGuardadoExitoso.collectLatest { // <-- Esto ya no dará error
            onGuardar()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (productoId == null) "Nuevo Producto" else "Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // ... (Todos tus Campos de Texto se quedan igual) ...
            // nombre
            CampoTexto(
                valor = uiState.nombre,
                alCambiar = { vm.onFormChange(nombre = it) },
                etiqueta = "Nombre del producto *",
                hayError = uiState.nombreError != null,
                mensajeError = uiState.nombreError
            )
            Spacer(modifier = Modifier.height(16.dp))

            // descripcion
            CampoTexto(
                valor = uiState.descripcion,
                alCambiar = { vm.onFormChange(descripcion = it) },
                etiqueta = "Descripción",
                modificador = Modifier.height(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // precio
            CampoTexto(
                valor = uiState.precio,
                alCambiar = { vm.onFormChange(precio = it) },
                etiqueta = "Precio *",
                tipoTeclado = KeyboardType.Decimal,
                hayError = uiState.precioError != null,
                mensajeError = uiState.precioError
            )
            Spacer(modifier = Modifier.height(16.dp))

            // stock
            CampoTexto(
                valor = uiState.stock,
                alCambiar = { vm.onFormChange(stock = it) },
                etiqueta = "Stock disponible *",
                tipoTeclado = KeyboardType.Number,
                hayError = uiState.stockError != null,
                mensajeError = uiState.stockError
            )
            Spacer(modifier = Modifier.height(16.dp))

            // categoria
            CampoTexto(
                valor = uiState.categoria,
                alCambiar = { vm.onFormChange(categoria = it) },
                etiqueta = "Categoría (ej: Venta, Arriendo) *",
                hayError = uiState.categoriaError != null,
                mensajeError = uiState.categoriaError
            )
            Spacer(modifier = Modifier.height(16.dp))

            // tipo
            CampoTexto(
                valor = uiState.tipo,
                alCambiar = { vm.onFormChange(tipo = it) },
                etiqueta = "Tipo (ej: casa, depto, oficina) *",
                hayError = uiState.tipoError != null,
                mensajeError = uiState.tipoError
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- Error general ---
            if (uiState.errorGeneral != null) {
                Text(
                    text = uiState.errorGeneral!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- ¡¡ARREGLO!! Reemplazamos BotonCargando por un Button normal ---
            Button(
                onClick = {
                    vm.guardarProducto()
                },
                enabled = !isLoading, // El botón se deshabilita mientras carga
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (productoId == null) "Crear Producto" else "Actualizar Producto")
                }
            }
        }
    }
}