package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AnimalResponse(
    @SerializedName("int_id") val intId: Int?,
//    @SerializedName("type") val type: String?,
//    @SerializedName("subtype") val subtype: String?,
//    @SerializedName("photo_url") val photoUrl: String?,
//    @SerializedName("blood_group") val bloodGroup: String?,
//    @SerializedName("blood_component") val bloodComponent: String?,
//    @SerializedName("nickname") val nickname: String?,
//    @SerializedName("age") val age: Int?,
//    @SerializedName("weight") val weight: String?,
)
