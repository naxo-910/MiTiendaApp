package com.example.evparcial2.domain.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.evparcial2.util.Validadores

class ViewModelForm : ViewModel() {

    private val _estadoForm = mutableStateOf(EstadoForm())
    val estadoForm = _estadoForm

    fun cambiarCampo(campo: TipoCampo, valor: String) {
        _estadoForm.value = _estadoForm.value.copy(
            campos = _estadoForm.value.campos.toMutableMap().apply {
                put(campo, valor)
            },
            errores = validarCampo(campo, valor)
        )
    }

    private fun validarCampo(campo: TipoCampo, valor: String): Map<TipoCampo, String> {
        val errores = mutableMapOf<TipoCampo, String>()

        when (campo) {
            TipoCampo.NOMBRE -> {
                Validadores.validarNombre(valor)?.let { errores[campo] = it }
            }
            TipoCampo.PRECIO -> {
                Validadores.validarPrecio(valor)?.let { errores[campo] = it }
            }
            TipoCampo.STOCK -> {
                Validadores.validarStock(valor)?.let { errores[campo] = it }
            }
            else -> {}
        }

        return errores
    }

    fun formValido(): Boolean {
        return _estadoForm.value.errores.isEmpty() &&
                _estadoForm.value.campos[TipoCampo.NOMBRE]?.isNotBlank() == true &&
                _estadoForm.value.campos[TipoCampo.PRECIO]?.isNotBlank() == true
    }
}

data class EstadoForm(
    val campos: Map<TipoCampo, String> = emptyMap(),
    val errores: Map<TipoCampo, String> = emptyMap()
)

enum class TipoCampo {
    NOMBRE,
    DESCRIPCION,
    PRECIO,
    STOCK,
    CATEGORIA
}