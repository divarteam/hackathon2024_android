package ru.divarteam.franimal.presentation.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.divarteam.franimal.data.network.response.ProductResponse
import ru.divarteam.franimal.domain.usecase.shop.BuyProductUseCase
import ru.divarteam.franimal.domain.usecase.shop.LoadProductsUseCase
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val loadProductsUseCase: LoadProductsUseCase,
    private val buyProductUseCase: BuyProductUseCase
) : ViewModel() {

    private val _productsList = MutableLiveData(listOf<ProductResponse>())
    val productsList: LiveData<List<ProductResponse>>
        get() = _productsList

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    val compositeDisposable = CompositeDisposable()

    fun buyProduct(
        productId: Int,
        doOnError: (String) -> Unit
    ) {
        _loading.postValue(true)

        buyProductUseCase(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    loadProductsList(doOnError)
                else
                    doOnError(it.errorBody().toString())
                _loading.postValue(false)
            }).addTo(compositeDisposable)
    }

    fun loadProductsList(
        doOnError: (String) -> Unit
    ) {
        _loading.postValue(true)

        loadProductsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                doOnError(it.localizedMessage.orEmpty())
                _loading.postValue(false)
            }, onSuccess = {
                if (it.isSuccessful)
                    _productsList.postValue(it.body())
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