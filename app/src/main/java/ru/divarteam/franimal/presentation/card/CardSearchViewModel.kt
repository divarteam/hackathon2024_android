package ru.divarteam.franimal.presentation.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.divarteam.franimal.data.network.response.CardResponse
import ru.divarteam.franimal.data.network.response.UserResponse
import ru.divarteam.franimal.domain.usecase.card.SearchCardsByQueue
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    private val searchCardsByQueue: SearchCardsByQueue
) : ViewModel() {

    private val _cardsList = MutableLiveData(listOf<CardResponse>())
    val cardsList: LiveData<List<CardResponse>>
        get() = _cardsList

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val querySubject = PublishSubject.create<String>()
    private var querySubjectReady = false
    private val compositeDisposable = CompositeDisposable()

    fun setupQuerySubject(doOnError: (String) -> Unit) {
        if (!querySubjectReady)
            querySubject
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe {
                    loadUsers(it, doOnError)
                }
                .addTo(compositeDisposable)
    }

    fun operateQuery(query: String) {
        querySubject.onNext(query)
    }

    private fun loadUsers(
        queue: String,
        doOnError: (String) -> Unit
    ) {
        _loading.postValue(true)
        searchCardsByQueue(queue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                it.printStackTrace()
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    _cardsList.postValue(it.body())
                else {
                    doOnError(it.errorBody().toString())
                    println(it.errorBody().toString())
                }
                _loading.postValue(false)
            }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}