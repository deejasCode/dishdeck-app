package com.dishdeck.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dishdeck.app.screens.RecipeListScreen
import com.dishdeck.app.screens.RecipeDetailScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dishdeck.app.screens.AddRecipeScreen
import com.dishdeck.app.screens.FavouritesScreen

sealed class Screen(val route: String) {
    object RecipeList : Screen("recipe_list")
    object RecipeDetail : Screen("recipe_detail/{recipeId}")
    object AddRecipe : Screen("add_recipe?recipeId={recipeId}")
    object Favourites : Screen("favourites")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.RecipeList.route
    ) {
        composable(Screen.RecipeList.route) {
            RecipeListScreen(navController = navController)
        }
        composable(Screen.RecipeDetail.route) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            RecipeDetailScreen(navController = navController, recipeId = recipeId)
        }
        composable(
            route = Screen.AddRecipe.route,
            arguments = listOf(
                navArgument("recipeId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            AddRecipeScreen(navController = navController, recipeId = recipeId)
        }
        composable(Screen.Favourites.route) {
            FavouritesScreen(navController = navController)
        }
    }
}