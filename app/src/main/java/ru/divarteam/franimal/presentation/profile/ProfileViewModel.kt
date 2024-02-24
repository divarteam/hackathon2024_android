package ru.divarteam.franimal.presentation.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.divarteam.franimal.data.network.response.UserResponse
import ru.divarteam.franimal.domain.usecase.note.CreateNoteUseCase
import ru.divarteam.franimal.domain.usecase.note.LikeNoteUseCase
import ru.divarteam.franimal.domain.usecase.profile.CheckIsCurrentUserUseCase
import ru.divarteam.franimal.domain.usecase.profile.ExitUseCase
import ru.divarteam.franimal.domain.usecase.profile.LoadUserUseCase
import ru.divarteam.franimal.domain.usecase.profile.UpdateUserPhotoUseCase
import ru.divarteam.franimal.domain.usecase.profile.UpdateUserUseCase
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loadUserUseCase: LoadUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase,
    private val likeNoteUseCase: LikeNoteUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val checkIsCurrentUserUseCase: CheckIsCurrentUserUseCase,
    private val exitUseCase: ExitUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _userResponse = MutableLiveData(
        UserResponse(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null, null, null
        )
    )
    val userResponse: LiveData<UserResponse>
        get() = _userResponse

    fun likeNoteById(
        currentUserId: Int,
        noteIntId: Int,
        doOnError: (String) -> Unit,
        doOnBanned: () -> Unit
    ) {
        likeNoteUseCase(noteIntId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    loadUserById(currentUserId, doOnError, doOnBanned)
                else {
                    doOnError(it.errorBody().toString())
                    _loading.postValue(false)
                }
            }).addTo(compositeDisposable)
    }

    fun createNote(
        noteText: String,
        context: Context,
        uri: Uri?,
        doOnError: (String) -> Unit,
        doOnSuccess: () -> Unit
    ) {
        if (uri != null)
            createNoteUseCase(context, noteText, uri)
        else {
            createNoteUseCase(noteText)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
            }, onSuccess = {
                if (it.isSuccessful)
                    doOnSuccess()
                else
                    doOnError(it.errorBody().toString())
            }).addTo(compositeDisposable)
    }

    fun loadMe(
        doOnError: (String) -> Unit,
        doOnBanned: () -> Unit
    ) = loadUserById(-1, doOnError, doOnBanned)

    fun loadUserById(
        userId: Int,
        doOnError: (String) -> Unit,
        doOnBanned: () -> Unit
    ) {
        loadUserUseCase(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
            }, onSuccess = {
                if (it.code() == 403)
                    doOnBanned()
                if (it.isSuccessful)
                    _userResponse.postValue(it.body())
                else
                    doOnError(it.errorBody().toString())
            })
            .addTo(compositeDisposable)
    }

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun exit() {
        exitUseCase()
    }

    fun updateUser(
        age: Int?,
        fullname: String?,
        city: String?,
        mailIsPublic: Boolean?,
        phone: String?,
        phoneIsPublic: Boolean?,
        vkLink: String?,
        vkLinkIsPublic: Boolean?,
        tgLink: String?,
        tgLinkIsPublic: Boolean?,
        busyDates: List<String>?,
        doOnError: (String) -> Unit,
        doOnSuccess: (UserResponse) -> Unit
    ) {
        _loading.postValue(true)

        updateUserUseCase(
            age,
            fullname,
            city,
            mailIsPublic,
            phone,
            phoneIsPublic,
            vkLink,
            vkLinkIsPublic,
            tgLink,
            tgLinkIsPublic,
            busyDates
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful) {
                    _userResponse.postValue(it.body())
                    doOnSuccess(it.body()!!)
                } else
                    doOnError(it.errorBody().toString())

                _loading.postValue(false)
            }).addTo(compositeDisposable)
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun updateUserPhoto(
        context: Context,
        uri: Uri,
        doOnError: (String) -> Unit,
        doOnSuccess: (String) -> Unit
    ) {
        _loading.postValue(true)

        updateUserPhotoUseCase(context, uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    doOnSuccess(it.body().orEmpty())
                else
                    doOnError(it.errorBody().toString())
                _loading.postValue(false)
            }).addTo(compositeDisposable)
    }
}