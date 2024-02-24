package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CommentResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("creation_dt") val creationDate: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("note_int_id") val noteIntId: Int?,
    @SerializedName("user_int_id") val userIntId: Int?,
    @SerializedName("user") val user: UserResponse?
)
