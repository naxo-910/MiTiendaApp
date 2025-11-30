package com.example.evparcial2.data.api

import com.example.evparcial2.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ReservaApiService {
    
    @GET("reservations")
    suspend fun getUserReservations(@Header("Authorization") token: String): Response<ReservaListResponse>
    
    @GET("reservations/{id}")
    suspend fun getReservationById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<ReservaResponse>
    
    @GET("reservations/user/{userId}")
    suspend fun getReservationsByUser(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<ReservaListResponse>
    
    @POST("reservations")
    suspend fun createReservation(
        @Header("Authorization") token: String,
        @Body request: CreateReservaRequest
    ): Response<ReservaResponse>
    
    @PUT("reservations/{id}")
    suspend fun updateReservation(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body request: CreateReservaRequest
    ): Response<ReservaResponse>
    
    @DELETE("reservations/{id}")
    suspend fun cancelReservation(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<Unit>
}