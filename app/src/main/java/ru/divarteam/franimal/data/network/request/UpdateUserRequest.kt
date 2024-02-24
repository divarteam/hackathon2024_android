package ru.divarteam.franimal.data.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateUserRequest(
    @SerializedName("age") val age: Int?,
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("mail_is_public") val mailIsPublic: Boolean?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("phone_is_public") val phoneIsPublic: Boolean?,
    @SerializedName("vk_link") val vkLink: String?,
    @SerializedName("vk_link_is_public") val vkLinkIsPublic: Boolean?,
    @SerializedName("tg_link") val tgLink: String?,
    @SerializedName("tg_link_is_public") val tgLinkIsPublic: Boolean?,
    @SerializedName("busy_dates") val busyDates: List<String>?
)