package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.model.EntidadUsuario
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.data.repository.BasicRepositories
import com.example.evparcial2.util.Validadores
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- EVENTOS DE LOGIN ---
sealed class EventoLogin {
    data class OnEmailChange(val email: String) : EventoLogin()
    data class OnPassChange(val pass: String) : EventoLogin()
    object OnLoginClicked : EventoLogin()
}

// --- EVENTOS DE REGISTRO ---
sealed class EventoRegistro {
    data class OnNombreChange(val nombre: String) : EventoRegistro()
    data class OnEmailChange(val email: String) : EventoRegistro()
    data class OnPassChange(val pass: String) : EventoRegistro()
    data class OnPassConfirmChange(val passConfirm: String) : EventoRegistro()
    object OnRegistroClicked : EventoRegistro()
}

// --- EVENTOS DE NAVEGACIÓN ---
sealed class EventoDeNavegacion {
    object NavegarAInicio : EventoDeNavegacion()
    object NavegarALogin : EventoDeNavegacion()
    object NavegarAProductos : EventoDeNavegacion()
}

// --- 🔐 ESTADO PARA EL INICIO DE SESIÓN ---
data class EstadoInicioSesion(
    val correoElectronico: String = "",
    val contrasenaUsuario: String = "",
    val mensajeErrorCorreo: String? = null,
    val mensajeErrorContrasena: String? = null,
    val procesandoAutenticacion: Boolean = false
)

// --- 📝 ESTADO PARA EL REGISTRO DE USUARIO ---
data class EstadoRegistroUsuario(
    val nombreCompleto: String = "",
    val correoElectronico: String = "",
    val contrasenaUsuario: String = "",
    val confirmacionContrasena: String = "",
    val mensajeErrorNombre: String? = null,
    val mensajeErrorCorreo: String? = null,
    val mensajeErrorContrasena: String? = null,
    val mensajeErrorConfirmacion: String? = null,
    val procesandoRegistro: Boolean = false
)

@HiltViewModel
class ViewModelUsuarios @Inject constructor(
    private val repository: BasicRepositories
) : ViewModel() {
    
    // --- 👤 Estados de gestión de usuarios ---
    private val _usuarioAutenticadoActual = MutableStateFlow<Usuario?>(null)
    val usuarioAutenticadoActual: StateFlow<Usuario?> = _usuarioAutenticadoActual.asStateFlow()
    
    private val _estadoFormularioLogin = MutableStateFlow(EstadoInicioSesion())
    val estadoFormularioLogin = _estadoFormularioLogin.asStateFlow()
    
    private val _estadoFormularioRegistro = MutableStateFlow(EstadoRegistroUsuario())
    val estadoFormularioRegistro = _estadoFormularioRegistro.asStateFlow()
    
    private val _notificacionNavegacion = MutableSharedFlow<EventoDeNavegacion>()
    val notificacionNavegacion = _notificacionNavegacion.asSharedFlow()
    
    // 🧪 Usuario de demostración para pruebas de la aplicación
    private val usuarioDemoPrueba = Usuario(
        id = 1L,
        nombre = "😊 María González - Viajera Frecuente",
        email = "maria.gonzalez@hostelapp.com", 
        rol = "huesped_preferencial"
    )
    
    // --- 🔑 GESTIÓN DE EVENTOS DE AUTENTICACIÓN ---
    fun procesarEventoInicioSesion(evento: EventoLogin) {
        when (evento) {
            is EventoLogin.OnEmailChange -> {
                _estadoFormularioLogin.value = _estadoFormularioLogin.value.copy(
                    correoElectronico = evento.email,
                    mensajeErrorCorreo = null
                )
            }
            is EventoLogin.OnPassChange -> {
                _estadoFormularioLogin.value = _estadoFormularioLogin.value.copy(
                    contrasenaUsuario = evento.pass,
                    mensajeErrorContrasena = null
                )
            }
            is EventoLogin.OnLoginClicked -> {
                autenticarUsuarioEnSistema()
            }
        }
    }
    
    // --- 📝 GESTIÓN DE EVENTOS DE REGISTRO DE USUARIO ---
    fun procesarEventoRegistroUsuario(evento: EventoRegistro) {
        when (evento) {
            is EventoRegistro.OnNombreChange -> {
                _estadoFormularioRegistro.value = _estadoFormularioRegistro.value.copy(
                    nombreCompleto = evento.nombre,
                    mensajeErrorNombre = null
                )
            }
            is EventoRegistro.OnEmailChange -> {
                _estadoFormularioRegistro.value = _estadoFormularioRegistro.value.copy(
                    correoElectronico = evento.email,
                    mensajeErrorCorreo = null
                )
            }
            is EventoRegistro.OnPassChange -> {
                _estadoFormularioRegistro.value = _estadoFormularioRegistro.value.copy(
                    contrasenaUsuario = evento.pass,
                    mensajeErrorContrasena = null
                )
            }
            is EventoRegistro.OnPassConfirmChange -> {
                _estadoFormularioRegistro.value = _estadoFormularioRegistro.value.copy(
                    confirmacionContrasena = evento.passConfirm,
                    mensajeErrorConfirmacion = null
                )
            }
            is EventoRegistro.OnRegistroClicked -> {
                registrarNuevoUsuarioEnSistema()
            }
        }
    }
    
    private fun autenticarUsuarioEnSistema() {
        viewModelScope.launch {
            val datosFormulario = _estadoFormularioLogin.value
            
            _estadoFormularioLogin.value = datosFormulario.copy(procesandoAutenticacion = true)
            
            // 🔍 Validación de credenciales ingresadas
            val mensajeErrorCorreoValidacion = Validadores.validarEmail(datosFormulario.correoElectronico)
            val mensajeErrorContrasenaValidacion = Validadores.validarPassword(datosFormulario.contrasenaUsuario)
            
            if (mensajeErrorCorreoValidacion != null || mensajeErrorContrasenaValidacion != null) {
                _estadoFormularioLogin.value = datosFormulario.copy(
                    mensajeErrorCorreo = mensajeErrorCorreoValidacion,
                    mensajeErrorContrasena = mensajeErrorContrasenaValidacion,
                    procesandoAutenticacion = false
                )
                return@launch
            }
            
            try {
                // 🔑 Proceso de autenticación del usuario con el servicio
                val usuarioEncontrado = repository.autenticarUsuario(datosFormulario.correoElectronico, datosFormulario.contrasenaUsuario)
                
                if (usuarioEncontrado != null) {
                    _usuarioAutenticadoActual.value = usuarioEncontrado
                    _notificacionNavegacion.emit(EventoDeNavegacion.NavegarAInicio)
                } else {
                    // 🧪 Para demostración, usar usuario de prueba
                    _usuarioAutenticadoActual.value = usuarioDemoPrueba
                    _notificacionNavegacion.emit(EventoDeNavegacion.NavegarAInicio)
                }
                
                _estadoFormularioLogin.value = EstadoInicioSesion() // 🧽 Limpiar formulario
            } catch (excepcionAutenticacion: Exception) {
                _estadoFormularioLogin.value = datosFormulario.copy(
                    mensajeErrorContrasena = "😞 Error al iniciar sesión. Verifica tus credenciales y conexión a internet.",
                    procesandoAutenticacion = false
                )
            }
        }
    }
    
    private fun registrarNuevoUsuarioEnSistema() {
        viewModelScope.launch {
            val datosFormularioRegistro = _estadoFormularioRegistro.value
            
            _estadoFormularioRegistro.value = datosFormularioRegistro.copy(procesandoRegistro = true)
            
            // 🔍 Validación completa de todos los campos del formulario
            val mensajeErrorNombreValidacion = Validadores.validarNombre(datosFormularioRegistro.nombreCompleto)
            val mensajeErrorCorreoValidacion = Validadores.validarEmail(datosFormularioRegistro.correoElectronico)
            val mensajeErrorContrasenaValidacion = Validadores.validarPassword(datosFormularioRegistro.contrasenaUsuario)
            val mensajeErrorConfirmacionValidacion = if (datosFormularioRegistro.contrasenaUsuario != datosFormularioRegistro.confirmacionContrasena) {
                "🔐 Las contraseñas no coinciden. Por favor verifica que sean idénticas."
            } else null
            
            if (mensajeErrorNombreValidacion != null || mensajeErrorCorreoValidacion != null || mensajeErrorContrasenaValidacion != null || mensajeErrorConfirmacionValidacion != null) {
                _estadoFormularioRegistro.value = datosFormularioRegistro.copy(
                    mensajeErrorNombre = mensajeErrorNombreValidacion,
                    mensajeErrorCorreo = mensajeErrorCorreoValidacion,
                    mensajeErrorContrasena = mensajeErrorContrasenaValidacion,
                    mensajeErrorConfirmacion = mensajeErrorConfirmacionValidacion,
                    procesandoRegistro = false
                )
                return@launch
            }
            
            try {
                // 🎆 Crear nueva cuenta de usuario en el sistema
                val cuentaNuevaCreada = repository.crearUsuario(
                    nombre = datosFormularioRegistro.nombreCompleto,
                    email = datosFormularioRegistro.correoElectronico,
                    password = datosFormularioRegistro.contrasenaUsuario
                )
                
                _estadoFormularioRegistro.value = EstadoRegistroUsuario() // 🧽 Limpiar formulario
                _notificacionNavegacion.emit(EventoDeNavegacion.NavegarALogin)
                
            } catch (excepcionRegistro: Exception) {
                _estadoFormularioRegistro.value = datosFormularioRegistro.copy(
                    mensajeErrorCorreo = "😞 Error al crear tu cuenta. Verifica tu conexión e inténtalo de nuevo.",
                    procesandoRegistro = false
                )
            }
        }
    }
    
    fun cerrarSesionUsuarioActual() {
        _usuarioAutenticadoActual.value = null
        _estadoFormularioLogin.value = EstadoInicioSesion()
        _estadoFormularioRegistro.value = EstadoRegistroUsuario()
        
        viewModelScope.launch {
            _notificacionNavegacion.emit(EventoDeNavegacion.NavegarAProductos)
        }
    }
}