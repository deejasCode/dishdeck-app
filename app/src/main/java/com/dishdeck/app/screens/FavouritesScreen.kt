package com.dishdeck.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.dishdeck.app.ui.theme.DishDeckTheme

/**
 * Displays a filtered list of recipes marked as favourites.
 * Shows an empty state message when no favourites exist.
 *
 * @param navController The navigation controller for screen navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(navController: NavHostController) {

    val favouriteRecipes = sampleRecipes.filter { it.isFavourite }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourites") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (favouriteRecipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No favourites yet!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favouriteRecipes) { recipe ->
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

@Preview(showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    DishDeckTheme {
        FavouritesScreen(navController = rememberNavController())
    }
}