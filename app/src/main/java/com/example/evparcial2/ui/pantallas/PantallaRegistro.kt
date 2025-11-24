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
fun PantallaRegistro(
    vm: ViewModelUsuarios,
    onRegistroExitoso: () -> Unit,
    onVolver: () -> Unit
) {
    val uiState by vm.uiStateRegistro.collectAsState()

    // Listener para el evento de navegación
    LaunchedEffect(key1 = Unit) {
        vm.eventoNavegacion.collectLatest { evento ->
            when (evento) {
                is EventoDeNavegacion.NavegarALogin -> {
                    onRegistroExitoso()
                }
                else -> { /* No hacer nada en otros eventos */ }
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
            onClick = onVolver,
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
                        valor = uiState.nombre,
                        alCambiar = { vm.onRegistroEvent(EventoRegistro.OnNombreChange(it)) },
                        etiqueta = "Nombre Completo"
                    )
                    val nombreError = uiState.nombreError
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
                        valor = uiState.email,
                        alCambiar = { vm.onRegistroEvent(EventoRegistro.OnEmailChange(it)) },
                        etiqueta = "Email",
                        tipoTeclado = KeyboardType.Email
                    )
                    val emailError = uiState.emailError
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
                        valor = uiState.pass,
                        alCambiar = { vm.onRegistroEvent(EventoRegistro.OnPassChange(it)) },
                        etiqueta = "Contraseña (mín. 6 caracteres)",
                        tipoTeclado = KeyboardType.Password
                    )
                    val passError = uiState.passError
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
                        valor = uiState.passConfirm,
                        alCambiar = { vm.onRegistroEvent(EventoRegistro.OnPassConfirmChange(it)) },
                        etiqueta = "Confirmar Contraseña",
                        tipoTeclado = KeyboardType.Password
                    )
                    val passConfirmError = uiState.passConfirmError
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
                            vm.onRegistroEvent(EventoRegistro.OnRegistroClicked)
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