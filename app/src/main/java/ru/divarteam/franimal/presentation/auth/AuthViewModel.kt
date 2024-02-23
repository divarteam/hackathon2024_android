package ru.divarteam.franimal.presentation.auth

import android.os.CountDownTimer
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
import ru.divarteam.franimal.domain.usecase.auth.CheckCodeUseCase
import ru.divarteam.franimal.domain.usecase.auth.CheckMailExistsUseCase
import ru.divarteam.franimal.domain.usecase.auth.SendCodeUseCase
import ru.divarteam.franimal.domain.usecase.auth.UpdateCurrentUserUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendCodeUseCase: SendCodeUseCase,
    private val checkCodeUseCase: CheckCodeUseCase,
    private val checkMailExistsUseCase: CheckMailExistsUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase
) : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    private val _timerSecondsRemain = MutableLiveData(0)
    val timerSecondsRemain: LiveData<Int>
        get() = _timerSecondsRemain

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val timer = object : CountDownTimer(30 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            _timerSecondsRemain.postValue(millisUntilFinished.floorDiv(1000).toInt())
        }

        override fun onFinish() {
            _timerSecondsRemain.postValue(0)
        }
    }

    fun updateUserData(
        newUserId: Int,
        newUserToken: String
    ) {
        updateCurrentUserUseCase(newUserId, newUserToken)
    }

    fun checkCode(
        email: String,
        code: String,
        doOnSuccess: (user: UserResponse?) -> Unit,
        doOnError: (errorText: String) -> Unit
    ) {
        _loading.postValue(true)

        checkMailExistsUseCase(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                _loading.postValue(false)
                doOnError(it.localizedMessage.orEmpty())
            }, onSuccess = {
                checkCodeUseCase(
                    email,
                    code,
                    if (it.body()?.mailExists == true)
                        CheckCodeUseCase.Type.AUTHORIZATION
                    else
                        CheckCodeUseCase.Type.REGISTRATION
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onError = {
                        _loading.postValue(false)
                        doOnError(it.localizedMessage.orEmpty())
                    }, onSuccess = {
                        _loading.postValue(false)
                        doOnSuccess(it.body())
                    })
                    .addTo(compositeDisposable)
            })
            .addTo(compositeDisposable)
    }

    fun sendCode(
        email: String,
        doOnSuccess: () -> Unit,
        doOnError: (errorText: String) -> Unit
    ) {

        timer.cancel()
        timer.start()

        checkMailExistsUseCase(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                timer.cancel()
                doOnError(it.localizedMessage.orEmpty())
            }, onSuccess = {
                sendCodeUseCase(
                    email,
                    if (it.body()?.mailExists == true)
                        SendCodeUseCase.Type.AUTHORIZATION
                    else
                        SendCodeUseCase.Type.REGISTRATION
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onError = {
                        timer.cancel()
                        doOnError(it.localizedMessage.orEmpty())
                    }, onSuccess = {
                        doOnSuccess()
                    })
                    .addTo(compositeDisposable)
            })
            .addTo(compositeDisposable)
    }
}