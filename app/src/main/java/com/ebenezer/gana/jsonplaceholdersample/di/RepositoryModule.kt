package com.ebenezer.gana.jsonplaceholdersample.di

import com.ebenezer.gana.jsonplaceholdersample.data.repository.JsonPlaceholderRepository
import com.ebenezer.gana.jsonplaceholdersample.data.repository.JsonPlaceholderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun providePostsRepository(repository: JsonPlaceholderRepositoryImpl): JsonPlaceholderRepository

}