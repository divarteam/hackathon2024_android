package ru.divarteam.franimal.domain.usecase.profile

import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.data.network.request.UpdateUserRequest
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val franimalAPIService: FraminalAPIService
) {
    operator fun invoke(
        age: Int?,
        fullname: String?,
        city: String?,
        mailIsPublic: Boolean?,
        phone: String?,
        phoneIsPublic: Boolean?,
        vkLink: String?,
        vkLinkIsPublic: Boolean?,
        tgLink: String?,
        tgLinkIsPublic: Boolean?,
        busyDates: List<String>?
    ) = franimalAPIService.updateUser(
        preferenceRepository.currentUserToken,
        UpdateUserRequest(
            age,
            fullname,
            city,
            mailIsPublic,
            phone,
            phoneIsPublic,
            vkLink,
            vkLinkIsPublic,
            tgLink,
            tgLinkIsPublic,
            busyDates
        )
    )
}