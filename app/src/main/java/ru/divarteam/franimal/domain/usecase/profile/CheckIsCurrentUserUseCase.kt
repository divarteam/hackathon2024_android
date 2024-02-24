package ru.divarteam.franimal.domain.usecase.profile

import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class CheckIsCurrentUserUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(userId: Int) = preferenceRepository.currentUserId == userId
}