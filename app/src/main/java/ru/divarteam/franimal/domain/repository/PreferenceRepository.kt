package ru.divarteam.franimal.domain.repository

import android.content.SharedPreferences

interface PreferenceRepository {
    var currentUserId: Int
    var currentUserToken: String

    companion object {
        internal const val PREFERENCE_CURRENT_USER_ID = "franimal_current_user_id"
        internal const val PREFERENCE_CURRENT_USER_TOKEN = "franimal_current_user_token"
    }
}