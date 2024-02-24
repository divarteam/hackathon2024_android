package ru.divarteam.franimal.domain.usecase.note

import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class RemoveCommentUseCase @Inject constructor(
    private val franimalAPIService: FraminalAPIService,
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(noteIntId: Int) =
        franimalAPIService.removeComment(
            preferenceRepository.currentUserToken,
            noteIntId
        )
}