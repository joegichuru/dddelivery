package com.joe.network

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.joe.network.model.Order
import com.joe.network.model.Recipe
import com.joe.network.model.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("orders")
    fun getOrders(): Observable<ResponseData<Order>>

    @GET("orders")
    fun searchMenu(@Query("search") query: String): Observable<ResponseData<Order>>

    @GET("ingredients")
    fun getIngredients(@Query("category") category: Int): Observable<ResponseData<Recipe>>

    @Mock
    @MockResponse(body = "orders.json")
    @GET("orders/{id}/ingredients")
    fun searchIngredients(@Path("id") orderId: Int, @Query("search") query: String)
}