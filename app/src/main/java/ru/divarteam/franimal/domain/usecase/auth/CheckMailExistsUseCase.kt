package ru.divarteam.franimal.domain.usecase.auth

import ru.divarteam.franimal.data.network.FraminalAPIService
import javax.inject.Inject

class CheckMailExistsUseCase @Inject constructor(
    private val framinalAPIService: FraminalAPIService
) {
    operator fun invoke(
        email: String
    ) = framinalAPIService.checkMailExists(email)
}