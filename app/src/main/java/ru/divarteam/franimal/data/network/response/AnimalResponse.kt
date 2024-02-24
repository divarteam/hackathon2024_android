package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AnimalResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("creation_dt") val creationDate: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("subtype") val subtype: String?,
    @SerializedName("photo_filename") val photoFilename: String?,
    @SerializedName("blood_group") val bloodGroup: String?,
    @SerializedName("blood_component") val bloodComponent: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("vaccinations") val vaccinations: List<String>?,
    @SerializedName("user_int_id") val userIntId: Int?,
)
