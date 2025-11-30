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
fun PantallaFormularioAlojamiento(
    gestorAlojamientos: ViewModelProductos,
    idAlojamientoAEditar: Long?,
    alVolver: () -> Unit,
    alGuardarExitosamente: () -> Unit
) {
    val estadoFormulario by gestorAlojamientos.estadoFormulario.collectAsState()
    val procesandoGuardado by gestorAlojamientos.procesandoGuardado.collectAsState()

    // 🏨 Preparar formulario al cargar (crear nuevo o editar existente)
    LaunchedEffect(idAlojamientoAEditar) {
        gestorAlojamientos.prepararFormularioAlojamiento(idAlojamientoAEditar)
    }

    // 🎉 Escuchar notificación de guardado exitoso
    LaunchedEffect(key1 = Unit) {
        gestorAlojamientos.notificacionGuardadoExitoso.collectLatest {
            alGuardarExitosamente()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(if (idAlojamientoAEditar == null) "🏨 Nuevo Alojamiento" else "✏️ Editar Alojamiento") 
                },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "🔙 Regresar")
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
            // 🏨 Nombre del alojamiento
            CampoTexto(
                valor = estadoFormulario.nombreAlojamiento,
                alCambiar = { gestorAlojamientos.actualizarCamposFormulario(nombreAlojamiento = it) },
                etiqueta = "🏨 Nombre del alojamiento *",
                hayError = estadoFormulario.errorNombreAlojamiento != null,
                mensajeError = estadoFormulario.errorNombreAlojamiento
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 📝 Descripción detallada
            CampoTexto(
                valor = estadoFormulario.descripcionDetallada,
                alCambiar = { gestorAlojamientos.actualizarCamposFormulario(descripcionDetallada = it) },
                etiqueta = "📝 Descripción del alojamiento",
                modificador = Modifier.height(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 💵 Precio por noche
            CampoTexto(
                valor = estadoFormulario.precioNochePesos,
                alCambiar = { gestorAlojamientos.actualizarCamposFormulario(precioNochePesos = it) },
                etiqueta = "💵 Precio por noche (CLP) *",
                tipoTeclado = KeyboardType.Decimal,
                hayError = estadoFormulario.errorPrecioNoche != null,
                mensajeError = estadoFormulario.errorPrecioNoche
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 🚪 Habitaciones disponibles
            CampoTexto(
                valor = estadoFormulario.habitacionesDisponibles,
                alCambiar = { gestorAlojamientos.actualizarCamposFormulario(habitacionesDisponibles = it) },
                etiqueta = "🚪 Habitaciones disponibles *",
                tipoTeclado = KeyboardType.Number,
                hayError = estadoFormulario.errorHabitacionesDisponibles != null,
                mensajeError = estadoFormulario.errorHabitacionesDisponibles
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 🏷️ Categoría del alojamiento
            CampoTexto(
                valor = estadoFormulario.categoriaAlojamiento,
                alCambiar = { gestorAlojamientos.actualizarCamposFormulario(categoriaAlojamiento = it) },
                etiqueta = "🏷️ Categoría (Económico, Premium, Lujo) *",
                hayError = estadoFormulario.errorCategoriaAlojamiento != null,
                mensajeError = estadoFormulario.errorCategoriaAlojamiento
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 🚪 Tipo de habitación
            CampoTexto(
                valor = estadoFormulario.tipoHabitacion,
                alCambiar = { gestorAlojamientos.actualizarCamposFormulario(tipoHabitacion = it) },
                etiqueta = "🚪 Tipo (privada, compartida, familiar) *",
                hayError = estadoFormulario.errorTipoHabitacion != null,
                mensajeError = estadoFormulario.errorTipoHabitacion
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 🚨 Mensaje de error general
            if (estadoFormulario.mensajeErrorGeneral != null) {
                Text(
                    text = estadoFormulario.mensajeErrorGeneral!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 💾 Botón de guardado inteligente
            Button(
                onClick = {
                    gestorAlojamientos.guardarAlojamientoEnCatalogo()
                },
                enabled = !procesandoGuardado, 
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (procesandoGuardado) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (idAlojamientoAEditar == null) "✨ Crear Alojamiento" else "🔄 Actualizar Alojamiento")
                }
            }
        }
    }
}