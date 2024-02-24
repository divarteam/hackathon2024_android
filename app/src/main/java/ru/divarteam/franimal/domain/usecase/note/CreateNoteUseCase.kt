package ru.divarteam.franimal.domain.usecase.note

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import ru.divarteam.franimal.data.network.FraminalAPIService
import ru.divarteam.franimal.data.network.response.NoteResponse
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import ru.divarteam.franimal.util.photoUriToFile
import java.io.File
import java.util.Calendar
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(
    private val franimalAPIService: FraminalAPIService,
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(text: String) = franimalAPIService.createNote(
        preferenceRepository.currentUserToken,
        text
    )

    operator fun invoke(context: Context, text: String, uri: Uri): Single<Response<NoteResponse>> =
            franimalAPIService.createNote(
                preferenceRepository.currentUserToken,
                text,
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