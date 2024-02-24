package ru.divarteam.franimal.domain.usecase.shop

import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

class BuyProductUseCase @Inject constructor(
    private val franimalAPIService: FraminalAPIService,
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(productId: Int) = franimalAPIService.buyProduct(
        preferenceRepository.currentUserToken,
        productId
    )
}