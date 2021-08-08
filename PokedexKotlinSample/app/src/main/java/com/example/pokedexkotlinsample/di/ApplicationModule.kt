package com.example.pokedexkotlinsample.di

import com.example.pokedexkotlinsample.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.pokedexkotlinsample.data.api.PokemonService
import com.example.pokedexkotlinsample.data.repository.PokemonRepository
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun providePokemonService(okHttpClient: OkHttpClient): PokemonService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.POKEMON_API_DOMAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonService::class.java)
    }

    @Provides
    fun providePokemonRepository(api: PokemonService): PokemonRepository {
        return PokemonRepository(api)
    }
}