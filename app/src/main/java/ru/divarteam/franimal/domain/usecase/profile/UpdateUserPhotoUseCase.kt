package ru.divarteam.franimal.domain.usecase.profile

import android.R.attr.data
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import ru.divarteam.franimal.util.photoUriToFile
import java.io.File
import java.time.Instant
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


class UpdateUserPhotoUseCase @Inject constructor(
    private val franimalAPIService: FraminalAPIService,
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(context: Context, uri: Uri): Single<Response<String>> =
        franimalAPIService.updateUserPhoto(
            preferenceRepository.currentUserToken,
            MultipartBody.Part.createFormData(
                "photo",
                Calendar.getInstance().time.toString(),
                RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    uri.photoUriToFile(context)
                )
            )
        )
}