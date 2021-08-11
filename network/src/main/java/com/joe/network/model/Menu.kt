package com.joe.network.model

import java.util.*

data class Order(
    val id: Int,
    val title: String = "Test",
    val quantity: Int = 1,
    val createdAt: Date = Date(),
    val alertedAt: Date = Date(),
    val expiredAt: Date = Date(),
    var progress: Double = 0.0,
    val recipe: List<Recipe> = emptyList()
)

data class Recipe(
    val id: Int,
    val title: String,
    val categoryId:Int,
    val categoryName:String,
    val imageUrl:String,
    val quantity: String,
    val available: Boolean,
    val count: Int
)