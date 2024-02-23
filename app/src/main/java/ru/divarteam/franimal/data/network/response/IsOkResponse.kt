package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class IsOkResponse(
    @SerializedName("is_ok") val isOk: Boolean?
)