package ru.divarteam.franimal.domain.usecase.auth

import ru.divarteam.franimal.data.network.FraminalAPIService
import javax.inject.Inject

class AuthViaVkUseCase @Inject constructor(
    private val franimalAPIService: FraminalAPIService
) {
    operator fun invoke(code: String) = franimalAPIService.authViaVk(code)
}