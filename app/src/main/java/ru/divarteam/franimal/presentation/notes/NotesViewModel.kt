package ru.divarteam.franimal.presentation.notes

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
import ru.divarteam.franimal.data.network.response.NoteResponse
import ru.divarteam.franimal.domain.usecase.note.CreateNoteUseCase
import ru.divarteam.franimal.domain.usecase.note.GetAllNotesUseCase
import ru.divarteam.franimal.domain.usecase.note.LikeNoteUseCase
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val likeNoteUseCase: LikeNoteUseCase
) : ViewModel() {

    private val _notesList = MutableLiveData(listOf<NoteResponse>())
    val notesList: LiveData<List<NoteResponse>>
        get() = _notesList

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean>
        get() = _loading

    private val compositeDisposable = CompositeDisposable()

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

    fun likeNoteById(
        noteIntId: Int,
        doOnError: (String) -> Unit
    ) {
        likeNoteUseCase(noteIntId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    loadNotesList(doOnError)
                else {
                    doOnError(it.errorBody().toString())
                    _loading.postValue(false)
                }
            }).addTo(compositeDisposable)
    }

    fun loadNotesList(
        doOnError: (String) -> Unit
    ) {
        getAllNotesUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    _notesList.postValue(it.body())
                else
                    doOnError(it.errorBody().toString())
                _loading.postValue(false)
            }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}