package com.example.evparcial2.di

import com.example.evparcial2.data.api.ExchangeRateApiService
import com.example.evparcial2.data.api.HostelApiService
import com.example.evparcial2.data.api.ReservaApiService
import com.example.evparcial2.data.api.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideHostelApiService(@Named("hostel_api") retrofit: Retrofit): HostelApiService {
        return retrofit.create(HostelApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(@Named("hostel_api") retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReservaApiService(@Named("hostel_api") retrofit: Retrofit): ReservaApiService {
        return retrofit.create(ReservaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideExchangeRateApiService(@Named("exchange_api") retrofit: Retrofit): ExchangeRateApiService {
        return retrofit.create(ExchangeRateApiService::class.java)
    }
}