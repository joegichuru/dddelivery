package com.joe.network.model

data class ResponseData<T>(val status: Status, val data: List<T>)
data class Status(val success: Boolean, val statusCode: Int, val message: String)