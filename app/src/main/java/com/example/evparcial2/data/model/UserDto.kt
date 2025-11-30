package com.example.evparcial2.data.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val nombre: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("role")
    val rol: String,
    @SerializedName("created_at")
    val fechaCreacion: String
)

// Request para login
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

// Request para registro  
data class RegisterRequest(
    @SerializedName("name")
    val nombre: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String
)

// Response para autenticación
data class AuthResponse(
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("token")
    val token: String,
    @SerializedName("message")
    val message: String
)