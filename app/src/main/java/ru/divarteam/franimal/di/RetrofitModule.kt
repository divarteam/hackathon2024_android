package ru.divarteam.franimal.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.data.network.RetrofitClient

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    private val BASE_URL = "https://api.hackathon2024.divarteam.ru/"

    @Provides
    @Reusable
    fun provideFraminalAPIService(): FraminalAPIService =
        RetrofitClient.getClient(BASE_URL).create(FraminalAPIService::class.java)
}