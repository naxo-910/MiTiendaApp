package com.example.evparcial2.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// ¡IMPORTANTE! Ahora usamos la Entidad de la base de datos
import com.example.evparcial2.data.local.entities.EntidadUsuario
// ¡Y TAMBIÉN EL MODELO DE LA UI!
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.data.local.repository.RepoUsuarios // ¡Importamos el Repositorio!
import com.example.evparcial2.util.Validadores
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- ESTADO PARA EL LOGIN ---
data class UiStateLogin(
    val email: String = "",
    val pass: String = "",
    val emailError: String? = null,
    val passError: String? = null
)

// --- ESTADO NUEVO PARA EL REGISTRO ---
data class UiStateRegistro(
    val nombre: String = "",
    val email: String = "",
    val pass: String = "",
    val passConfirm: String = "",
    val nombreError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,
    val passConfirmError: String? = null
)

// --- EVENTOS PARA EL LOGIN ---
sealed class EventoLogin {
    data class OnEmailChange(val email: String) : EventoLogin()
    data class OnPassChange(val pass: String) : EventoLogin()
    object OnLoginClicked : EventoLogin()
}

// --- EVENTOS NUEVOS PARA EL REGISTRO ---
sealed class EventoRegistro {
    data class OnNombreChange(val nombre: String) : EventoRegistro()
    data class OnEmailChange(val email: String) : EventoRegistro()
    data class OnPassChange(val pass: String) : EventoRegistro()
    data class OnPassConfirmChange(val pass: String) : EventoRegistro()
    object OnRegistroClicked : EventoRegistro()
}

// --- EVENTOS DE NAVEGACIÓN ---
sealed interface EventoDeNavegacion {
    object NavegarAInicio : EventoDeNavegacion
    object NavegarALogin : EventoDeNavegacion
}

// --- Función para convertir de Base de Datos -> UI (¡CORREGIDA!) ---
private fun EntidadUsuario.toUsuario(): Usuario {
    return Usuario(
        id = this.id, // <-- ¡¡ESTA ES LA LÍNEA QUE FALTA!!
        nombre = this.nombre,
        email = this.email,
        rol = this.rol,
        pass = this.contrasena
    )
}


class ViewModelUsuarios : ViewModel() {

    // --- ¡CONEXIÓN A LA BASE DE DATOS REAL! ---
    private val repoUsuarios = RepoUsuarios()

    // --- ¡¡NUEVO BLOQUE PARA CREAR ADMIN/CLIENTE!! ---
    init {
        viewModelScope.launch {
            // 1. Revisa si el admin ya existe en la base de datos
            val admin = repoUsuarios.obtenerUsuarioPorEmail("admin@demo.com")
            if (admin == null) {
                // 2. Si no existe, lo crea
                repoUsuarios.insertarUsuario(EntidadUsuario(
                    nombre = "Admin",
                    email = "admin@demo.com",
                    contrasena = "admin123", // Usamos 'contrasena'
                    rol = "admin"
                ))
            }

            // 3. Revisa si el cliente ya existe
            val cliente = repoUsuarios.obtenerUsuarioPorEmail("cliente@demo.com")
            if (cliente == null) {
                // 4. Si no existe, lo crea
                repoUsuarios.insertarUsuario(EntidadUsuario(
                    nombre = "Cliente",
                    email = "cliente@demo.com",
                    contrasena = "cliente123", // Usamos 'contrasena'
                    rol = "cliente"
                ))
            }
        }
    }

    // --- ESTADOS ---
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _uiStateLogin = MutableStateFlow(UiStateLogin())
    val uiState: StateFlow<UiStateLogin> = _uiStateLogin.asStateFlow()

    private val _uiStateRegistro = MutableStateFlow(UiStateRegistro())
    val uiStateRegistro: StateFlow<UiStateRegistro> = _uiStateRegistro.asStateFlow()

    // --- CANAL DE NAVEGACIÓN ---
    private val _eventoNavegacion = MutableSharedFlow<EventoDeNavegacion>()
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    // --- MANEJADOR DE EVENTOS LOGIN ---
    fun onLoginEvent(evento: EventoLogin) {
        when (evento) {
            is EventoLogin.OnEmailChange -> {
                _uiStateLogin.update { it.copy(email = evento.email) }
            }
            is EventoLogin.OnPassChange -> {
                _uiStateLogin.update { it.copy(pass = evento.pass) }
            }
            is EventoLogin.OnLoginClicked -> {
                validarYLoguear()
            }
        }
    }

    // --- MANEJADOR DE EVENTOS REGISTRO ---
    fun onRegistroEvent(evento: EventoRegistro) {
        when (evento) {
            is EventoRegistro.OnNombreChange -> {
                _uiStateRegistro.update { it.copy(nombre = evento.nombre) }
            }
            is EventoRegistro.OnEmailChange -> {
                _uiStateRegistro.update { it.copy(email = evento.email) }
            }
            is EventoRegistro.OnPassChange -> {
                _uiStateRegistro.update { it.copy(pass = evento.pass) }
            }
            is EventoRegistro.OnPassConfirmChange -> {
                _uiStateRegistro.update { it.copy(passConfirm = evento.pass) }
            }
            is EventoRegistro.OnRegistroClicked -> {
                validarYRegistrar()
            }
        }
    }

    // --- LÓGICA DE LOGIN (¡CORREGIDA!) ---
    private fun validarYLoguear() {
        _uiStateLogin.update { it.copy(emailError = null, passError = null) }

        val email = _uiStateLogin.value.email
        val pass = _uiStateLogin.value.pass

        val errorEmail = Validadores.validarEmail(email)
        val errorPass = if (pass.isBlank()) "La contraseña es obligatoria" else null

        if (errorEmail != null || errorPass != null) {
            _uiStateLogin.update { it.copy(emailError = errorEmail, passError = errorPass) }
            return
        }

        viewModelScope.launch {
            // Usamos el nombre correcto del repo: 'contrasena'
            val entidadEncontrada = repoUsuarios.login(email = email, contrasena = pass)

            if (entidadEncontrada != null) {
                // Convertimos la Entidad (DB) en Usuario (UI)
                _usuarioActual.value = entidadEncontrada.toUsuario()
                _eventoNavegacion.emit(EventoDeNavegacion.NavegarAInicio)
            } else {
                _uiStateLogin.update { it.copy(passError = "Email o contraseña incorrectos") }
            }
        }
    }

    // --- LÓGICA DE REGISTRO (¡CORREGIDA!) ---
    private fun validarYRegistrar() {
        _uiStateRegistro.update { it.copy(
            nombreError = null,
            emailError = null,
            passError = null,
            passConfirmError = null
        )}

        val state = _uiStateRegistro.value
        val nombre = state.nombre
        val email = state.email
        val pass = state.pass
        val passConfirm = state.passConfirm

        viewModelScope.launch {
            val errorNombre = if (nombre.isBlank()) "El nombre es obligatorio" else null
            val errorEmail = Validadores.validarEmail(email)
            val errorPass = if (pass.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
            val errorPassConfirm = if (pass != passConfirm) "Las contraseñas no coinciden" else null
            val errorEmailExistente = if (errorEmail == null && repoUsuarios.obtenerUsuarioPorEmail(email) != null) "El email ya está registrado" else null

            if (errorNombre != null || errorEmail != null || errorPass != null || errorPassConfirm != null || errorEmailExistente != null) {
                _uiStateRegistro.update { it.copy(
                    nombreError = errorNombre,
                    emailError = errorEmail ?: errorEmailExistente,
                    passError = errorPass,
                    passConfirmError = errorPassConfirm
                )}
                return@launch
            }

            // Usamos el nombre correcto de la Entidad: 'contrasena'
            val nuevoUsuario = EntidadUsuario(
                nombre = nombre,
                email = email,
                contrasena = pass,
                rol = "cliente"
            )

            repoUsuarios.insertarUsuario(nuevoUsuario)
            _uiStateRegistro.value = UiStateRegistro()
            _eventoNavegacion.emit(EventoDeNavegacion.NavegarALogin)
        }
    }

    // --- CERRAR SESIÓN (Sin cambios) ---
    fun cerrarSesion() {
        _usuarioActual.value = null
    }
}