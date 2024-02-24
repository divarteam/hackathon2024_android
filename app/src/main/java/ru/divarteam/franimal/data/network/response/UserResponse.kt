package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("age") val age: Int?,
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("photo_filename") val photoFilename: String?,
    @SerializedName("mail") val mail: String?,
    @SerializedName("mail_is_public") val mailIsPublic: Boolean?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("phone_is_public") val phoneIsPublic: Boolean?,
    @SerializedName("vk_id") val vkId: String?,
    @SerializedName("vk_link") val vkLink: String?,
    @SerializedName("vk_link_is_public") val vkLinkIsPublic: Boolean?,
    @SerializedName("tg_id") val tgId: String?,
    @SerializedName("tg_link") val tgLink: String?,
    @SerializedName("tg_link_is_public") val tgLinkIsPublic: Boolean?,
    @SerializedName("busy_dates") val busyDates: List<String>?,
    @SerializedName("points") val points: Int?,
    @SerializedName("current_token") val currentToken: String?,
    @SerializedName("is_busy") val isBusy: Boolean?,
    @SerializedName("rank") val rank: String?,
    @SerializedName("needed_points") val neededPoints: Int?,
    @SerializedName("animals") val animals: List<AnimalResponse>?,
    @SerializedName("cards") val cards: List<CardResponse>?,
    @SerializedName("notes") val notes: List<NoteResponse>?
)