package com.example.evparcial2.data.api

import com.example.evparcial2.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface HostelApiService {
    
    @GET("hostels")
    suspend fun getAllHostels(): Response<HostelListResponse>
    
    @GET("hostels/{id}")
    suspend fun getHostelById(@Path("id") id: String): Response<HostelResponse>
    
    @GET("hostels/search")
    suspend fun searchHostels(
        @Query("country") country: String? = null,
        @Query("city") city: String? = null,
        @Query("room_type") roomType: String? = null,
        @Query("min_price") minPrice: Int? = null,
        @Query("max_price") maxPrice: Int? = null
    ): Response<HostelListResponse>
    
    @POST("hostels")
    suspend fun createHostel(@Body hostel: HostelDto): Response<HostelResponse>
    
    @PUT("hostels/{id}")
    suspend fun updateHostel(
        @Path("id") id: String,
        @Body hostel: HostelDto
    ): Response<HostelResponse>
    
    @DELETE("hostels/{id}")
    suspend fun deleteHostel(@Path("id") id: String): Response<Unit>
}