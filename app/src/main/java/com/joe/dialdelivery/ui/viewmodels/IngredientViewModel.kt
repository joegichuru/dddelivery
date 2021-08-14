package com.joe.dialdelivery.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joe.network.Api
import com.joe.network.model.Order
import com.joe.network.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(private val api: Api):ViewModel() {
    val ingredients: MutableLiveData<List<Recipe>> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    fun fetchIngredients(categoryId: Int) {
        api.getIngredients(categoryId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loading.postValue(true)
                error.postValue(false)
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
                ingredients.postValue(it.data)
            }, {
                it.printStackTrace()
                error.postValue(true)
                loading.postValue(false)
            }, {
                error.postValue(false)
                loading.postValue(false)
            })
    }

}