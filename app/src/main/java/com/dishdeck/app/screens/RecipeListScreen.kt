package com.dishdeck.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dishdeck.app.model.Recipe
import com.dishdeck.app.navigation.Screen
import androidx.compose.ui.tooling.preview.Preview
import com.dishdeck.app.ui.theme.DishDeckTheme
import androidx.navigation.compose.rememberNavController
import com.dishdeck.app.model.Ingredient

// Sample data to display while we don't have a backend yet
val sampleRecipes = listOf(
    Recipe(
        id = 1,
        name = "Desi Omelette",
        imageUrl = "https://unsplash.com/photos/omelette-on-black-plate-6rtm6a_aVyE",
        category = "Breakfast",
        servings = 1,
        ingredients = listOf(
            Ingredient("Eggs", 2.0, "whole"),
            Ingredient("Onion", 0.5, "whole"),
            Ingredient("Tomato", 0.5, "whole"),
            Ingredient("Green chilli", 1.0, "whole"),
            Ingredient("Fresh coriander", 1.0, "tbsp"),
            Ingredient("Salt", 0.5, "tsp"),
            Ingredient("Oil", 1.0, "tbsp")
        ),
        steps = "1. Beat eggs with salt.\n2. Finely chop onion, tomato, chilli and coriander.\n3. Mix vegetables into eggs.\n4. Heat oil in pan on medium heat.\n5. Pour egg mixture and cook until set.\n6. Fold and serve hot with roti.",
        isFavourite = true
    ),
    Recipe(
        id = 2,
        name = "Daal",
        imageUrl = "https://unsplash.com/photos/a-bowl-of-soup-and-a-piece-of-bread-on-a-table-qP5SO98yG8k",
        category = "Lunch",
        servings = 4,
        ingredients = listOf(
            Ingredient("Red lentils", 250.0, "grams"),
            Ingredient("Onion", 1.0, "whole"),
            Ingredient("Tomato", 2.0, "whole"),
            Ingredient("Garlic", 3.0, "cloves"),
            Ingredient("Ginger", 1.0, "tsp"),
            Ingredient("Turmeric", 0.5, "tsp"),
            Ingredient("Red chilli powder", 1.0, "tsp"),
            Ingredient("Salt", 1.0, "tsp"),
            Ingredient("Oil", 3.0, "tbsp"),
            Ingredient("Cumin seeds", 1.0, "tsp"),
            Ingredient("Fresh coriander", 2.0, "tbsp")
        ),
        steps = "1. Wash and boil lentils with turmeric and salt until soft.\n2. In a separate pan, heat oil and fry cumin seeds.\n3. Add chopped onion and fry until golden.\n4. Add garlic, ginger and tomatoes, cook until oil separates.\n5. Add red chilli powder and mix.\n6. Pour tarka over boiled daal.\n7. Garnish with fresh coriander and serve with rice or roti.",
        isFavourite = false
    ),
    Recipe(
        id = 3,
        name = "Chicken Pulao",
        imageUrl = "https://unsplash.com/photos/a-white-plate-topped-with-rice-and-meat-n7kqI1mISOY",
        category = "Dinner",
        servings = 4,
        ingredients = listOf(
            Ingredient("Basmati rice", 400.0, "grams"),
            Ingredient("Chicken", 500.0, "grams"),
            Ingredient("Onion", 2.0, "whole"),
            Ingredient("Whole spices", 1.0, "tbsp"),
            Ingredient("Garlic", 4.0, "cloves"),
            Ingredient("Ginger", 1.0, "tsp"),
            Ingredient("Salt", 1.5, "tsp"),
            Ingredient("Oil", 4.0, "tbsp"),
            Ingredient("Water", 700.0, "ml")
        ),
        steps = "1. Wash and soak rice for 30 minutes.\n2. Heat oil and fry whole spices until fragrant.\n3. Add sliced onions and fry until golden brown.\n4. Add garlic, ginger and chicken pieces.\n5. Cook chicken until it changes colour.\n6. Add water and salt, bring to boil.\n7. Drain and add soaked rice.\n8. Cook on high until water evaporates.\n9. Cover and steam on low heat for 15 minutes.\n10. Fluff and serve with raita.",
        isFavourite = false
)
)

val categories = listOf("All", "Breakfast", "Lunch", "Dinner", "Snacks")

/**
 * Displays the main recipe list screen with category filtering.
 * Users can browse all recipes, filter by category, and navigate
 * to recipe details or add a new recipe.
 *
 * @param navController The navigation controller for screen navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(navController: NavHostController) {

    // State: which category is selected
    var selectedCategory by remember { mutableStateOf("All") }

    // Filter recipes based on selected category
    val filteredRecipes = if (selectedCategory == "All") {
        sampleRecipes
    } else {
        sampleRecipes.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DishDeck") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Favourites.route)
                    }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favourites")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddRecipe.route)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category filter row
            LazyRow(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            // Recipe list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            navController.navigate("recipe_detail/${recipe.id}")
                        }
                    )
                }
            }
        }
    }
}

/**
 * Displays a single recipe card in the recipe list.
 *
 * @param recipe The recipe data to display.
 * @param onClick Callback triggered when the card is tapped.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recipe.category,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecipeListScreenPreview() {
    DishDeckTheme {
        RecipeListScreen(navController = rememberNavController())
    }
}