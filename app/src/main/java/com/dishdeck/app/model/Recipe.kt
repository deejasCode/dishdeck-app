package com.dishdeck.app.model

/**
 * Represents a single ingredient with quantity and unit.
 *
 * @property name The ingredient name.
 * @property quantity The amount needed.
 * @property unit The unit of measurement (e.g. grams, cups, tbsp).
 */
data class Ingredient(
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

/**
 * Represents a recipe in the DishDeck app.
 *
 * @property id Unique identifier for the recipe.
 * @property name The name of the recipe.
 * @property category The meal category (e.g. Breakfast, Lunch, Dinner).
 * @property servings The base number of servings the recipe makes.
 * @property ingredients List of structured ingredients with quantities.
 * @property steps Step-by-step cooking instructions.
 * @property imageUrl URL or path to the recipe image.
 * @property isFavourite Whether the recipe is marked as a favourite.
 */
data class Recipe(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val servings: Int = 1,
    val ingredients: List<Ingredient> = emptyList(),
    val steps: String = "",
    val imageUrl: String = "",
    val isFavourite: Boolean = false
)