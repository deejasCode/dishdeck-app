package com.dishdeck.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import androidx.core.content.ContextCompat
import java.io.File
import com.dishdeck.app.model.Recipe
import com.dishdeck.app.model.Ingredient
import com.dishdeck.app.ui.theme.DishDeckTheme


/**
 * Screen for adding a new recipe or editing an existing one.
 * Contains input fields for all recipe properties.
 *
 * @param navController The navigation controller for screen navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(navController: NavHostController, recipeId: String? = null) {

    // Find the recipe being edited (if any)
    val existingRecipe = remember(recipeId) {
        sampleRecipes.find { it.id == recipeId?.toIntOrNull() }
    }

    // State for each input field
    var recipeName by remember { mutableStateOf(existingRecipe?.name ?: "") }
    var selectedCategory by remember { mutableStateOf(existingRecipe?.category ?: "Breakfast") }
    var servings by remember { mutableStateOf((existingRecipe?.servings ?: 1).toFloat()) }
    var ingredients by remember {
        mutableStateOf(
            existingRecipe?.ingredients?.joinToString("\n") {
                "${it.quantity} ${it.unit} ${it.name}"
            } ?: ""
        )
    }
    var steps by remember { mutableStateOf(existingRecipe?.steps ?: "") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

// Launches the camera and saves the photo to tempImageUri
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedImageUri = tempImageUri
        }
    }

// Requests camera permission, then launches camera if granted
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            tempImageUri = uri
            cameraLauncher.launch(uri)
        }
    }

    val categories = listOf("Breakfast", "Lunch", "Dinner", "Snacks", "Dessert")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingRecipe != null) "Edit Recipe" else "Add Recipe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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

            // Camera capture for recipe photo
            Text("Recipe Photo", style = MaterialTheme.typography.labelLarge)

            if (capturedImageUri != null) {
                AsyncImage(
                    model = capturedImageUri,
                    contentDescription = "Captured recipe photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = {
                    val permission = android.Manifest.permission.CAMERA
                    if (ContextCompat.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED) {
                        val uri = createImageUri(context)
                        tempImageUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(permission)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (capturedImageUri != null) "Retake Photo" else "Take Photo of Recipe")
            }

            // Save button
            Button(
                onClick = {
                    val parsedIngredients = parseIngredients(ingredients)
                    val photoUri = capturedImageUri?.toString() ?: existingRecipe?.imageUrl ?: ""

                    if (existingRecipe != null) {
                        // Update existing recipe
                        val index = sampleRecipes.indexOf(existingRecipe)
                        if (index != -1) {
                            sampleRecipes[index] = existingRecipe.copy(
                                name = recipeName,
                                category = selectedCategory,
                                servings = servings.toInt(),
                                ingredients = parsedIngredients,
                                steps = steps,
                                imageUrl = photoUri
                            )
                        }
                    } else {
                        // Add new recipe
                        val newId = (sampleRecipes.maxOfOrNull { it.id } ?: 0) + 1
                        sampleRecipes.add(
                            Recipe(
                                id = newId,
                                name = recipeName,
                                category = selectedCategory,
                                servings = servings.toInt(),
                                ingredients = parsedIngredients,
                                steps = steps,
                                imageUrl = photoUri,
                                isFavourite = false
                            )
                        )
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (existingRecipe != null) "Update Recipe" else "Save Recipe")
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

/**
 * Creates a temporary file URI for storing a captured photo.
 *
 * @param context The application context.
 * @return A content URI pointing to the new image file.
 */
fun createImageUri(context: android.content.Context): Uri {
    val imageFile = File(
        context.getExternalFilesDir("Pictures"),
        "recipe_${System.currentTimeMillis()}.jpg"
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

/**
 * Parses a multi-line ingredients string into a list of Ingredient objects.
 * Expected format per line: "quantity unit name" (e.g. "500.0 grams Mutton").
 * Lines that don't match this format are skipped.
 *
 * @param text The raw ingredients text from the input field.
 * @return A list of parsed Ingredient objects.
 */
fun parseIngredients(text: String): List<Ingredient> {
    return text.lines()
        .mapNotNull { line ->
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return@mapNotNull null

            val parts = trimmed.split(" ", limit = 3)
            if (parts.size == 3) {
                val quantity = parts[0].toDoubleOrNull() ?: return@mapNotNull null
                Ingredient(name = parts[2], quantity = quantity, unit = parts[1])
            } else {
                null
            }
        }
}
