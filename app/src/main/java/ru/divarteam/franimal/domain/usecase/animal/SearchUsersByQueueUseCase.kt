package ru.divarteam.franimal.domain.usecase.animal

import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class SearchUsersByQueueUseCase @Inject constructor(
    private val franimalAPIService: FraminalAPIService,
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(queue: String) = franimalAPIService.searchUsers(
        preferenceRepository.currentUserToken,
        queue
    )
}