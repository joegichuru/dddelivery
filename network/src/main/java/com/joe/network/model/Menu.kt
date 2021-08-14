package com.joe.network.model

import java.util.*

data class Order(
    val id: Int,
    val title: String = "Test",
    val quantity: Int = 1,
    var createdAt: Date = Date(),
    var alertedAt: Date = Date(),
    var expiredAt: Date = Date(),
    val addOns: List<AddOn> = emptyList()
)

data class Recipe(
    val id: Int,
    val title: String,
    val categoryId: Int,
    val categoryName: String,
    val imageUrl: String,
    val quantity: String,
    val available: Boolean,
    val count: Int
)

data class AddOn(val id: Int, val title: String, val quantity: Int)