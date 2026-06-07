package com.dishdeck.app.model

data class Recipe(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val servings: Int = 1,
    val ingredients: List<String> = emptyList(),
    val steps: String = "",
    val imageUrl: String = "",
    val isFavourite: Boolean = false
)