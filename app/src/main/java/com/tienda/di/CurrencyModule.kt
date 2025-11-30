package com.tienda.di

import com.tienda.data.remote.api.CurrencyApiService
import com.tienda.data.repository.CurrencyRepositoryImpl
import com.tienda.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrencyModule {
    
    @Provides
    @Singleton
    @Named("currency_retrofit")
    fun provideCurrencyRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://data.fixer.io/api/") // API gratuita para conversión de monedas
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideCurrencyApiService(@Named("currency_retrofit") retrofit: Retrofit): CurrencyApiService {
        return retrofit.create(CurrencyApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCurrencyRepository(currencyApiService: CurrencyApiService): CurrencyRepository {
        return CurrencyRepositoryImpl(currencyApiService)
    }
}