package ru.divarteam.franimal.domain.repository

import android.content.SharedPreferences

class PreferenceRepositoryImpl(val sharedPreferences: SharedPreferences) : PreferenceRepository {

    override var currentUserId: Int
        get() = sharedPreferences.getInt(PreferenceRepository.PREFERENCE_CURRENT_USER_ID, 0)
        set(value) {
            sharedPreferences.edit()
                .putInt(PreferenceRepository.PREFERENCE_CURRENT_USER_ID, value)
                .apply()
        }

    override var currentUserToken: String
        get() = sharedPreferences
            .getString(PreferenceRepository.PREFERENCE_CURRENT_USER_TOKEN, "")
            .orEmpty()
        set(value) {
            sharedPreferences.edit()
                .putString(PreferenceRepository.PREFERENCE_CURRENT_USER_TOKEN, value)
                .apply()
        }


}