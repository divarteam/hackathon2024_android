package ru.divarteam.franimal.presentation.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.divarteam.franimal.data.network.response.NoteResponse
import ru.divarteam.franimal.data.network.response.UserResponse
import ru.divarteam.franimal.domain.usecase.note.CreateCommentUseCase
import ru.divarteam.franimal.domain.usecase.note.GetNoteUseCase
import ru.divarteam.franimal.domain.usecase.note.LikeNoteUseCase
import ru.divarteam.franimal.domain.usecase.note.RemoveCommentUseCase
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val createCommentUseCase: CreateCommentUseCase,
    private val removeCommentUseCase: RemoveCommentUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val likeNoteUseCase: LikeNoteUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _noteResponse = MutableLiveData<NoteResponse>(null)
    val noteResponse: LiveData<NoteResponse>
        get() = _noteResponse

    fun likeNoteById(
        noteIntId: Int,
        doOnError: (String) -> Unit
    ) {
        _loading.postValue(true)
        likeNoteUseCase(noteIntId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    updateNote(noteIntId, doOnError, {})
                else {
                    doOnError(it.errorBody().toString())
                    _loading.postValue(false)
                }
            }).addTo(compositeDisposable)
    }

    fun updateNote(
        noteIntId: Int,
        doOnError: (String) -> Unit,
        doOnSuccess: () -> Unit
    ) {
        _loading.postValue(true)
        getNoteUseCase(noteIntId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful) {
                    doOnSuccess()
                    _noteResponse.postValue(it.body())
                } else
                    doOnError(it.errorBody().toString())
                _loading.postValue(false)
            }).addTo(compositeDisposable)
    }

    fun createComment(
        noteIntId: Int,
        commentText: String,
        doOnError: (String) -> Unit,
        doOnSuccess: () -> Unit
    ) {
        _loading.postValue(true)
        createCommentUseCase(commentText, noteIntId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful) {
                    updateNote(noteIntId, doOnError, doOnSuccess)
                } else {
                    doOnError(it.errorBody().toString())
                    _loading.postValue(false)
                }
            }).addTo(compositeDisposable)
    }

    fun removeComment(
        noteIntId: Int,
        commentIntId: Int,
        doOnError: (String) -> Unit,
        doOnSuccess: () -> Unit
    ) {
        _loading.postValue(true)
        removeCommentUseCase(commentIntId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful) {
                    updateNote(noteIntId, doOnError, doOnSuccess)
                } else {
                    doOnError(it.errorBody().toString())
                    _loading.postValue(false)
                }
            }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}