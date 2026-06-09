package com.dishdeck.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * Screen for adding a new recipe or editing an existing one.
 * Contains input fields for all recipe properties.
 *
 * @param navController The navigation controller for screen navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(navController: NavHostController) {

    // State for each input field
    var recipeName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Breakfast") }
    var servings by remember { mutableStateOf(1f) }
    var ingredients by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Recipe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Recipe Name
            Text("Recipe Name", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = recipeName,
                onValueChange = { recipeName = it },
                placeholder = { Text("Write recipe name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Category dropdown
            Text("Category", style = MaterialTheme.typography.labelLarge)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Servings slider
            Text(
                "Servings: ${servings.toInt()}",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                value = servings,
                onValueChange = { servings = it },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth()
            )

            // Ingredients
            Text("Ingredients", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                placeholder = { Text("Write ingredients") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // Steps
            Text("Steps", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it },
                placeholder = { Text("Write recipe procedure") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // Image upload placeholder
            Text("Upload Photo", style = MaterialTheme.typography.labelLarge)
            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose Photo")
            }

            // Save button
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Recipe")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecipeScreenPreview() {
    DishDeckTheme {
        AddRecipeScreen(navController = rememberNavController())
    }
}
