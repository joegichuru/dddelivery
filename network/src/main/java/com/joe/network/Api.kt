package com.joe.network

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.joe.network.model.Menu
import com.joe.network.model.Recipe
import com.joe.network.model.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @Mock
    @MockResponse(body = "menus.json")
    @GET("menu")
    fun getMenu(): Observable<ResponseData<Menu>>

    @Mock
    @MockResponse(body = "favorites.json")
    @GET("menu/favorites")
    fun getFavorites(): Observable<ResponseData<Menu>>

    @Mock
    @MockResponse(body = "menus.json")
    @GET("menu")
    fun searchMenu(@Query("search") query: String): Observable<ResponseData<Menu>>

    @Mock
    @MockResponse(body = "menus.json")
    @GET("menu/{id}/ingredients")
    fun getIngredients(@Path("id") menuId: Int): Observable<ResponseData<Recipe>>

    @Mock
    @MockResponse(body = "menus.json")
    @GET("menu/{id}/ingredients")
    fun searchIngredients(@Path("id") menuId: Int, @Query("search") query: String)
}