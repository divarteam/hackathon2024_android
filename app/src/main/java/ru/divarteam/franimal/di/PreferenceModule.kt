package ru.divarteam.franimal.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import ru.divarteam.franimal.domain.repository.PreferenceRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {
    @Provides
    @Reusable
    fun providePreferenceRepository(@ApplicationContext context: Context): PreferenceRepository =
        PreferenceRepositoryImpl(
            context.getSharedPreferences(
                "franimal_preferences",
                Context.MODE_PRIVATE
            )
        )
}
