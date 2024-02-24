package ru.divarteam.franimal.domain.usecase.auth

import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class UpdateCurrentUserUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(
        newUserId: Int,
        newUserToken: String
    ) {
        preferenceRepository.currentUserId = newUserId
        preferenceRepository.currentUserToken = "Bearer $newUserToken"
    }
}