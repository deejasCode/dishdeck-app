package com.dishdeck.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dishdeck.app.navigation.Screen
import com.dishdeck.app.screens.sampleRecipes
import com.dishdeck.app.ui.theme.DishDeckTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavHostController,
    recipeId: String?
) {
    // Find the recipe from sample data using the id
    val recipe = sampleRecipes.find { it.id == recipeId?.toIntOrNull() }

    // State for servings scaler
    var servings by remember { mutableStateOf(recipe?.servings ?: 1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.name ?: "Recipe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.AddRecipe.route)
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()) {
                        Text("Recipe Image")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Servings scaler
            Text("Servings", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { if (servings > 1) servings-- }) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }
                Text(
                    text = servings.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { servings++ }) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ingredients
            Text("Ingredients", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            recipe?.ingredients?.forEach { ingredient ->
                Text("• $ingredient", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Steps
            Text("Steps", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe?.steps ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    DishDeckTheme {
        RecipeDetailScreen(
            navController = rememberNavController(),
            recipeId = "1"
        )
    }
}