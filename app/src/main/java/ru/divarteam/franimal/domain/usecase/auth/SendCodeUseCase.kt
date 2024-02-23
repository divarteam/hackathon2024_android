package ru.divarteam.franimal.domain.usecase.auth

import ru.divarteam.franimal.data.network.FraminalAPIService
import javax.inject.Inject

class SendCodeUseCase @Inject constructor(
    private val framinalAPIService: FraminalAPIService
) {

    operator fun invoke(
        email: String,
        type: Type
    ) = when (type) {
        Type.AUTHORIZATION -> framinalAPIService.sendMailAuthCode(email)
        Type.REGISTRATION -> framinalAPIService.sendMailRegCode(email)
    }

    enum class Type {
        AUTHORIZATION, REGISTRATION
    }
}