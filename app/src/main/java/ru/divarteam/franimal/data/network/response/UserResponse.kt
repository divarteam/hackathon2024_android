package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("age") val age: Int?,
    @SerializedName("coins") val coins: Int?,
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("photo_url") val photoUrl: String?,
    @SerializedName("mail") val mail: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("phone_is_public") val phoneIsPublic: Boolean?,
    @SerializedName("vk_id") val vkId: String?,
    @SerializedName("vk_link") val vkLink: String?,
    @SerializedName("vk_link_is_public") val vkLinkIsPublic: Boolean?,
    @SerializedName("tg_id") val tgId: String?,
    @SerializedName("tg_link") val tgLink: String?,
    @SerializedName("tg_link_is_public") val tgLinkIsPublic: Boolean?,
    @SerializedName("busy_dates") val busyDates: List<String>?,
    @SerializedName("tokens") val tokens: List<String>?,
    @SerializedName("current_token") val currentToken: String?,
    @SerializedName("animals") val animals: List<AnimalResponse>?,
)