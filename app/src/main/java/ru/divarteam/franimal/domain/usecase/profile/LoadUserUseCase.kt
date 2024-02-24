package ru.divarteam.franimal.domain.usecase.profile

import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val franimalAPIService: FraminalAPIService,
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(userId: Int) =
        if (userId == preferenceRepository.currentUserId || userId == -1)
            franimalAPIService.getMe(preferenceRepository.currentUserToken)
        else
            franimalAPIService.getUserById(preferenceRepository.currentUserToken, userId)
}