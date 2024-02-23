package ru.divarteam.franimal.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MailExistsResponse(
    @SerializedName("mail_exists") val mailExists: Boolean?
)