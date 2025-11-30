package com.example.evparcial2.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.evparcial2.domain.viewmodels.EventoDeNavegacion
import com.example.evparcial2.domain.viewmodels.EventoRegistro
import com.example.evparcial2.domain.viewmodels.ViewModelUsuarios
import com.example.evparcial2.ui.components.common.CampoTexto
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearCuenta(
    gestorUsuarios: ViewModelUsuarios,
    alCrearCuentaExitoso: () -> Unit,
    alVolver: () -> Unit
) {
    val estadoRegistro by gestorUsuarios.estadoFormularioRegistro.collectAsState()

    // Escuchar eventos de navegación
    LaunchedEffect(key1 = Unit) {
        gestorUsuarios.notificacionNavegacion.collectLatest { evento ->
            when (evento) {
                is EventoDeNavegacion.NavegarALogin -> {
                    alCrearCuentaExitoso()
                }
                else -> { /* No realizar acción */ }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        // Botón de volver flotante
        IconButton(
            onClick = alVolver,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, 
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "🏨 Crear Cuenta",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Encuentra tu alojamiento ideal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Card principal del formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Datos Personales",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // --- CAMPO NOMBRE ---
                    CampoTexto(
                        valor = estadoRegistro.nombreCompleto,
                        alCambiar = { gestorUsuarios.procesarEventoRegistroUsuario(EventoRegistro.OnNombreChange(it)) },
                        etiqueta = "Nombre Completo"
                    )
                    val nombreError = estadoRegistro.mensajeErrorNombre
                    if (nombreError != null) {
                        Text(
                            text = nombreError, 
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp, start = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CAMPO EMAIL ---
                    CampoTexto(
                        valor = estadoRegistro.correoElectronico,
                        alCambiar = { gestorUsuarios.procesarEventoRegistroUsuario(EventoRegistro.OnEmailChange(it)) },
                        etiqueta = "Email",
                        tipoTeclado = KeyboardType.Email
                    )
                    val emailError = estadoRegistro.mensajeErrorCorreo
                    if (emailError != null) {
                        Text(
                            text = emailError, 
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp, start = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CAMPO CONTRASEÑA ---
                    CampoTexto(
                        valor = estadoRegistro.contrasenaUsuario,
                        alCambiar = { gestorUsuarios.procesarEventoRegistroUsuario(EventoRegistro.OnPassChange(it)) },
                        etiqueta = "Contraseña (mín. 6 caracteres)",
                        tipoTeclado = KeyboardType.Password
                    )
                    val passError = estadoRegistro.mensajeErrorContrasena
                    if (passError != null) {
                        Text(
                            text = passError, 
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp, start = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CAMPO CONFIRMAR CONTRASEÑA ---
                    CampoTexto(
                        valor = estadoRegistro.confirmacionContrasena,
                        alCambiar = { gestorUsuarios.procesarEventoRegistroUsuario(EventoRegistro.OnPassConfirmChange(it)) },
                        etiqueta = "Confirmar Contraseña",
                        tipoTeclado = KeyboardType.Password
                    )
                    val passConfirmError = estadoRegistro.mensajeErrorConfirmacion
                    if (passConfirmError != null) {
                        Text(
                            text = passConfirmError, 
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp, start = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    // --- BOTÓN REGISTRAR MEJORADO ---
                    Button(
                        onClick = {
                            gestorUsuarios.procesarEventoRegistroUsuario(EventoRegistro.OnRegistroClicked)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.PersonAdd, 
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "Crear Cuenta",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}