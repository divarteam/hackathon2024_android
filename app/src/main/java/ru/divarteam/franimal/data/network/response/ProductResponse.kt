package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProductResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("price") val price: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("photo_url") val photoUrl: String?,
    @SerializedName("user_int_ids") val userIntIds: List<Int>?,
    @SerializedName("is_my") val isMy: Boolean?,
    @SerializedName("can_buy") val canBuy: Boolean?,
    @SerializedName("users") val users: List<UserResponse>?
)
