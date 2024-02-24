package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CardResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("creation_dt") val creationDate: String?,
    @SerializedName("is_public") val isPublic: Boolean?,
    @SerializedName("expiration_date") val expirationDate: String?,
    @SerializedName("animal_type") val animalType: String?,
    @SerializedName("reason") val reason: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("animal_blood_amount") val animalBloodAmount: Int?,
    @SerializedName("animal_blood_group") val animalBloodGroup: String?,
    @SerializedName("user_int_id") val userIntId: Int?,
    @SerializedName("user_who_helped_ids") val userWhoHelperIds: List<Int>?,
    @SerializedName("is_expired") val isExpired: Boolean?,
    @SerializedName("user") val user: UserResponse?
)