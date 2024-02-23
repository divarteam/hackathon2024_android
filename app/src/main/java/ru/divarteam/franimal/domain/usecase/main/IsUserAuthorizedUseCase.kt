package ru.divarteam.franimal.domain.usecase.main

import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class IsUserAuthorizedUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke() = preferenceRepository.currentUserToken.isNotEmpty()
}