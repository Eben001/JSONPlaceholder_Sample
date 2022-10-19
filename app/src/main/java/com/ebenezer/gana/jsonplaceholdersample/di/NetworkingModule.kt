package com.ebenezer.gana.jsonplaceholdersample.di

import com.ebenezer.gana.jsonplaceholdersample.data.network.api.JsonPlaceholderApi
import com.ebenezer.gana.jsonplaceholdersample.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    fun provideFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()


    @Provides
    fun buildRetrofit(client: OkHttpClient, factory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()

    }

    @Provides
    fun buildApiService(retrofit: Retrofit): JsonPlaceholderApi =
        retrofit.create(JsonPlaceholderApi::class.java)


}