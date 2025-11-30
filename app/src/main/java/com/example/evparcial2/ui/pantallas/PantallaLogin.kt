package com.example.evparcial2.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import com.example.evparcial2.domain.viewmodels.EventoLogin
import com.example.evparcial2.domain.viewmodels.ViewModelUsuarios
import com.example.evparcial2.ui.components.common.CampoTexto
import com.example.evparcial2.domain.viewmodels.EventoDeNavegacion

@Composable
fun PantallaInicioSesion(
    gestorUsuarios: ViewModelUsuarios,
    alIniciarSesionExitoso: () -> Unit,
    alNavegarÁRegistro: () -> Unit
) {
    val estadoInicioSesion by gestorUsuarios.estadoFormularioLogin.collectAsState()

    LaunchedEffect(key1 = Unit) {
        gestorUsuarios.notificacionNavegacion.collectLatest { evento ->
            when (evento) {
                is EventoDeNavegacion.NavegarAInicio -> {
                    alIniciarSesionExitoso()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header con ícono y título mejorado
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
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
                        Icons.Default.Hotel,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(bottom = 16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "🏨 Hostal Connect",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Tu puerta de entrada a experiencias únicas de hospedaje",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
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
                        "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    // Campos de entrada con diseño moderno
                    CampoTexto(
                        valor = estadoInicioSesion.correoElectronico,
                        alCambiar = { gestorUsuarios.procesarEventoInicioSesion(EventoLogin.OnEmailChange(it)) },
                        etiqueta = "Correo Electrónico"
                    )
                    val errorCorreo = estadoInicioSesion.mensajeErrorCorreo
                    if (errorCorreo != null) {
                        Text(
                            text = errorCorreo,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp, start = 16.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    CampoTexto(
                        valor = estadoInicioSesion.contrasenaUsuario,
                        alCambiar = { gestorUsuarios.procesarEventoInicioSesion(EventoLogin.OnPassChange(it)) },
                        etiqueta = "Contraseña",
                        tipoTeclado = KeyboardType.Password
                    )
                    val errorContrasena = estadoInicioSesion.mensajeErrorContrasena
                    if (errorContrasena != null) {
                        Text(
                            text = errorContrasena,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp, start = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón principal con diseño atractivo
                    Button(
                        onClick = {
                            gestorUsuarios.procesarEventoInicioSesion(EventoLogin.OnLoginClicked)
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
                            Icons.AutoMirrored.Filled.Login, 
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "Iniciar Sesión",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de registro con diseño elegante
                    OutlinedButton(
                        onClick = alNavegarÁRegistro,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder(
                            enabled = true
                        )
                    ) {
                        Icon(
                            Icons.Default.PersonAdd, 
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "¿Nuevo aquí? Crear cuenta",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}