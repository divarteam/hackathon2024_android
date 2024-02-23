package ru.divarteam.franimal.domain.usecase.auth

import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.data.network.request.RegViaMailRequest
import javax.inject.Inject

class CheckCodeUseCase @Inject constructor(
    private val framinalAPIService: FraminalAPIService
) {
    operator fun invoke(
        email: String,
        code: String,
        type: Type
    ) = when (type) {
        Type.AUTHORIZATION -> framinalAPIService.authViaMail(email, code)
        Type.REGISTRATION -> framinalAPIService.regViaMail(RegViaMailRequest(email, code))
    }

    enum class Type {
        AUTHORIZATION, REGISTRATION
    }
}