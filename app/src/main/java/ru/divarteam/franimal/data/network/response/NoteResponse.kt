package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NoteResponse(
    @SerializedName("int_id") val intId: Int?,
    @SerializedName("creation_dt") val creationDate: String?,
    @SerializedName("user_int_id") val userIntId: Int?,
    @SerializedName("photo_filename") val photoFilename: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("like_amount") val likeAmount: Int?,
    @SerializedName("is_my") val isMy: Boolean?,
    @SerializedName("liked_by_me") val likedByMe: Boolean?,
    @SerializedName("user") var user: UserResponse?,
    @SerializedName("comments") val comments: List<CommentResponse>?
)