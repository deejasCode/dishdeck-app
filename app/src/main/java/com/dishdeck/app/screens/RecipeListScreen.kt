package com.dishdeck.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

// Sample data to display while we don't have a backend yet
val sampleRecipes = listOf(
    Recipe(
        id = 1,
        name = "Pasta Carbonara",
        category = "Dinner",
        servings = 2,
        ingredients = listOf("pasta", "eggs", "bacon", "parmesan"),
        steps = "Boil pasta. Fry bacon. Mix eggs and cheese. Combine.",
        isFavourite = false
    ),
    Recipe(
        id = 2,
        name = "Avocado Toast",
        category = "Breakfast",
        servings = 1,
        ingredients = listOf("bread", "avocado", "salt", "lemon"),
        steps = "Toast bread. Mash avocado. Spread and season.",
        isFavourite = true
    ),
    Recipe(
        id = 3,
        name = "Caesar Salad",
        category = "Lunch",
        servings = 2,
        ingredients = listOf("lettuce", "croutons", "parmesan", "caesar dressing"),
        steps = "Chop lettuce. Add croutons. Dress and toss.",
        isFavourite = false
    )
)

val categories = listOf("All", "Breakfast", "Lunch", "Dinner", "Snacks")

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
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
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