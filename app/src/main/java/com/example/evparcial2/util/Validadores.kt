package com.example.evparcial2.util

object Validadores {

    fun validarNombre(nombre: String): String? {
        val nombreLimpio = nombre.trim() // <-- ¡¡ARREGLO!!
        return when {
            nombreLimpio.isBlank() -> "El nombre es obligatorio"
            nombreLimpio.length < 3 -> "Debe haber mínimo 3 caracteres"
            nombreLimpio.length > 50 -> "Deben haber máximo 50 caracteres"
            else -> null
        }
    }

    fun validarPrecio(precio: String): String? {
        val precioDouble = precio.toDoubleOrNull()
        return when {
            precio.isBlank() -> "El precio es obligatorio"
            precioDouble == null -> "Precio inválido"
            precioDouble < Constantes.PRECIO_MINIMO -> "El precio debe ser mayor a 0"
            else -> null
        }
    }

    fun validarStock(stock: String): String? {
        val stockInt = stock.toIntOrNull()
        return when {
            stock.isBlank() -> "El stock es obligatorio"
            stockInt == null -> "Stock inválido"
            stockInt < Constantes.STOCK_MINIMO -> "El stock no puede ser negativo"
            else -> null
        }
    }

    fun validarEmail(email: String): String? {
        val emailLimpio = email.trim() // <-- ¡¡ARREGLO!!
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        return when {
            emailLimpio.isBlank() -> "El email es obligatorio"
            !regex.matches(emailLimpio) -> "Email inválido" // <-- Comprobamos el email limpio
            else -> null
        }
    }
    
    fun validarPassword(password: String): String? {
        val passwordLimpio = password.trim()
        return when {
            passwordLimpio.isBlank() -> "La contraseña es obligatoria"
            passwordLimpio.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            passwordLimpio.length > 50 -> "La contraseña no puede exceder 50 caracteres"
            else -> null
        }
    }

    fun validarDescripcion(descripcion: String): String? {
        val descripcionLimpia = descripcion.trim()
        return when {
            descripcionLimpia.isBlank() -> "La descripción es obligatoria"
            descripcionLimpia.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            descripcionLimpia.length > 500 -> "La descripción no puede exceder 500 caracteres"
            else -> null
        }
    }
}