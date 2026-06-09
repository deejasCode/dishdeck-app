package com.dishdeck.app

import com.dishdeck.app.model.Recipe
import com.dishdeck.app.model.Ingredient
import org.junit.Test
import org.junit.Assert.*

class DishDeckUnitTest {

    // Test 1: Recipe is created with correct values
    @Test
    fun recipe_createdWithCorrectValues() {
        // Arrange & Act
        val recipe = Recipe(
            id = 1,
            name = "Pasta Carbonara",
            category = "Dinner",
            servings = 2,
            ingredients = listOf(
                Ingredient("pasta", 200.0, "grams"),
                Ingredient("eggs", 2.0, "whole"),
                Ingredient("bacon", 100.0, "grams")
            ),
            steps = "Boil pasta. Fry bacon.",
            isFavourite = false
        )

        // Assert
        assertEquals(1, recipe.id)
        assertEquals("Pasta Carbonara", recipe.name)
        assertEquals("Dinner", recipe.category)
        assertEquals(2, recipe.servings)
        assertFalse(recipe.isFavourite)
    }

    // Test 2: Favourites filter returns only favourite recipes
    @Test
    fun favouritesFilter_returnsOnlyFavourites() {
        // Arrange
        val recipes = listOf(
            Recipe(id = 1, name = "Pasta", isFavourite = false),
            Recipe(id = 2, name = "Avocado Toast", isFavourite = true),
            Recipe(id = 3, name = "Caesar Salad", isFavourite = false)
        )

        // Act
        val favourites = recipes.filter { it.isFavourite }

        // Assert
        assertEquals(1, favourites.size)
        assertEquals("Avocado Toast", favourites[0].name)
    }

    // Test 3: Category filter returns correct recipes
    @Test
    fun categoryFilter_returnsCorrectRecipes() {
        // Arrange
        val recipes = listOf(
            Recipe(id = 1, name = "Pasta", category = "Dinner"),
            Recipe(id = 2, name = "Avocado Toast", category = "Breakfast"),
            Recipe(id = 3, name = "Caesar Salad", category = "Lunch")
        )

        // Act
        val dinnerRecipes = recipes.filter { it.category == "Dinner" }

        // Assert
        assertEquals(1, dinnerRecipes.size)
        assertEquals("Pasta", dinnerRecipes[0].name)
    }

    // Test 4: Servings default value is 1
    @Test
    fun recipe_defaultServingsIsOne() {
        // Arrange & Act
        val recipe = Recipe(name = "Test Recipe")

        // Assert
        assertEquals(1, recipe.servings)
    }

    // Test 5: Recipe ingredients list is correct
    @Test
    fun recipe_ingredientsListIsCorrect() {
        // Arrange
        val ingredients = listOf(
            Ingredient("pasta", 200.0, "grams"),
            Ingredient("eggs", 2.0, "whole"),
            Ingredient("bacon", 100.0, "grams")
        )

        // Act
        val recipe = Recipe(
            name = "Pasta",
            ingredients = ingredients
        )

        // Assert
        assertEquals(3, recipe.ingredients.size)
        assertTrue(recipe.ingredients.any { it.name == "eggs" })
    }
}