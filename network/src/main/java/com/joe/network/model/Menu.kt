package com.joe.network.model

import java.util.*

data class Menu(
    val id: Int,
    val title: String,
    val quantity: Int,
    val createdAt: Date,
    val alertedAt: Date,
    val expiredAt: Date,
    val recipe: List<Recipe>
)

data class Recipe(val id: Int, val title: String, val quantity: String)