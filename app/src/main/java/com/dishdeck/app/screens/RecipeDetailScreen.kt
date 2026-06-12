package com.dishdeck.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import com.dishdeck.app.screens.sampleRecipes
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dishdeck.app.navigation.Screen
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog
import com.dishdeck.app.ui.theme.DishDeckTheme

/**
 * Displays the full details of a selected recipe including
 * ingredients, steps, and a servings scaler.
 *
 * @param navController The navigation controller for screen navigation.
 * @param recipeId The ID of the recipe to display.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavHostController,
    recipeId: String?
) {
    val recipe = sampleRecipes.find { it.id == recipeId?.toIntOrNull() }
    var servings by remember { mutableStateOf(recipe?.servings ?: 1) }
    var showFullImage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.name ?: "Recipe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        recipe?.let {
                            val index = sampleRecipes.indexOf(it)
                            if (index != -1) {
                                sampleRecipes[index] = it.copy(isFavourite = !it.isFavourite)
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (recipe?.isFavourite == true)
                                Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Favourite"
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate("add_recipe?recipeId=${recipe?.id}")
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
            // 1. Recipe image - shows photo if available, otherwise placeholder
            if (!recipe?.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = recipe?.imageUrl,
                    contentDescription = recipe?.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showFullImage = true },
                    contentScale = ContentScale.Crop
                )
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Recipe Image")
                    }
                }
            }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Full screen dialog
            if (showFullImage) {
            Dialog(onDismissRequest = { showFullImage = false }) {
                AsyncImage(
                    model = recipe?.imageUrl,
                    contentDescription = recipe?.name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                    )
                }
            }

            // 2. Servings scaler
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

            // 3. Ingredients with scaling
            Text("Ingredients", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            recipe?.ingredients?.forEach { ingredient ->
                val scaledQuantity = ingredient.quantity *
                        (servings.toDouble() / recipe.servings.toDouble())
                val formattedQuantity = if (scaledQuantity % 1.0 == 0.0) {
                    scaledQuantity.toInt().toString()
                } else {
                    "%.1f".format(scaledQuantity)
                }
                Text(
                    text = "• $formattedQuantity ${ingredient.unit} ${ingredient.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Steps
            Text("Steps", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe?.steps ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    DishDeckTheme {
        RecipeDetailScreen(
            navController = rememberNavController(),
            recipeId = "3"
        )
    }
}