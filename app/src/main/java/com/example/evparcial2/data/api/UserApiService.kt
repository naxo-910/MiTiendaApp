package com.example.evparcial2.data.api

import com.example.evparcial2.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserDto>
    
    @PUT("user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body user: UserDto
    ): Response<UserDto>
    
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>
}