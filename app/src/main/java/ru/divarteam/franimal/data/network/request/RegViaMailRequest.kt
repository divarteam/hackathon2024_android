package ru.divarteam.franimal.data.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegViaMailRequest(
    @SerializedName("mail") val mail: String,
    @SerializedName("mail_code") val mailCode: String
)