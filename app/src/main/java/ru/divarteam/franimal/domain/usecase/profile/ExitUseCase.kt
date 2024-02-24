package ru.divarteam.franimal.domain.usecase.profile

import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class ExitUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke() {
        preferenceRepository.currentUserToken = ""
        preferenceRepository.currentUserId = -1
    }
}