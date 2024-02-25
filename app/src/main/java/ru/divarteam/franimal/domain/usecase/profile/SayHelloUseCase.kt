package ru.divarteam.franimal.domain.usecase.profile

import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class SayHelloUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val framinalAPIService: FraminalAPIService
) {
    operator fun invoke(targetUserIntId: Int) =
        framinalAPIService.sayHello(preferenceRepository.currentUserToken, targetUserIntId)
}