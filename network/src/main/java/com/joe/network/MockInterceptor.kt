package com.joe.network

import android.content.Context
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Mock api server through okhttp interceptor
 * will intercept all requests and modify the response to simulate an api
 */
class MockInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url()

        val responseString = when {
            url.toString().contains("orders") -> getMenus()
            url.toString().contains("ingredients") && url.toString()
                .contains("search") -> searchIngredients(url.queryParameter("search"))
            url.toString().contains("ingredients") -> getIngredients(url.queryParameter("category"))
            else -> ""
        }
        //get response
        val response = chain.proceed(chain.request())
        //re rewrite a mocked response and discard the previous response
        return response.newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(responseString)
            .body(
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseString.toByteArray(Charsets.UTF_8)
                )
            )
            .addHeader("content-type", "application/json")
            .build()

    }

    private fun getMenus(): String {
        return assetStreamer("orders.json")
    }

    private fun getIngredients(category: String?): String {
        val string = assetStreamer("ingredients.json")
        //filter out the data that belongs only to categoryId
        val jsonObject = JSONObject(string)
        val data = jsonObject.getJSONArray("data")
        val filtered = JSONArray()
        for (i in 0 until data.length()) {
            val item = data.getJSONObject(i)
            if (item.getString("categoryId").equals(category)) {
                filtered.put(item)
            }
        }
        jsonObject.put("data",filtered)
        return jsonObject.toString()
    }
    private fun searchIngredients(search: String?): String {
        val string = assetStreamer("ingredients.json")
        //filter out the data that belongs only to categoryId
        val jsonObject = JSONObject(string)
        val data = jsonObject.getJSONArray("data")
        val filtered = JSONArray()
        for (i in 0 until data.length()) {
            val item = data.getJSONObject(i)
            if (item.getString("title").contains(search!!,true)) {
                filtered.put(item)
            }
        }
        jsonObject.put("data",filtered)
        return jsonObject.toString()
    }

    /**
     * Helper function to read data from json files stored in assets
     */
    private fun assetStreamer(fileName: String): String {
        val stream = context.assets.open(fileName)
        val size = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        val json = buffer.toString(Charsets.UTF_8)
        return json
    }
}