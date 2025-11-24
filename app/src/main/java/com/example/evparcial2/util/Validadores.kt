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
        // .toDoubleOrNull() ya maneja los espacios, así que aquí no es necesario .trim()
        return when {
            precio.isBlank() -> "El precio es obligatorio"
            precio.toDoubleOrNull() == null -> "Precio inválido"
            precio.toDouble() < Constantes.PRECIO_MINIMO -> "El precio debe ser mayor a 0"
            else -> null
        }
    }

    fun validarStock(stock: String): String? {
        // .toIntOrNull() ya maneja los espacios
        return when {
            stock.isBlank() -> "El stock es obligatorio"
            stock.toIntOrNull() == null -> "Stock inválido"
            stock.toInt() < Constantes.STOCK_MINIMO -> "El stock no puede ser negativo"
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
}