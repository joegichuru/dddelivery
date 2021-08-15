package com.joe.network

import com.joe.network.model.Order
import com.joe.network.model.Recipe
import com.joe.network.model.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("orders")
    fun getOrders(): Observable<ResponseData<Order>>

    @GET("ingredients")
    fun getIngredients(@Query("category") category: Int): Observable<ResponseData<Recipe>>

    @GET("ingredients")
    fun searchIngredients(@Query("search") query: String): Observable<ResponseData<Recipe>>

}