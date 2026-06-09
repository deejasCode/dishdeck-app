package com.dishdeck.backend;

import com.dishdeck.backend.model.Recipe;
import com.dishdeck.backend.repository.RecipeRepository;
import com.dishdeck.backend.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackendApplicationTests {

	@Mock
	private RecipeRepository recipeRepository;

	@InjectMocks
	private RecipeService recipeService;

	// Test 1: getAllRecipes returns all recipes
	@Test
	void getAllRecipes_returnsAllRecipes() {
		// Arrange
		Recipe recipe1 = new Recipe();
		recipe1.setName("Pasta Carbonara");
		recipe1.setCategory("Dinner");

		Recipe recipe2 = new Recipe();
		recipe2.setName("Avocado Toast");
		recipe2.setCategory("Breakfast");

		when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe1, recipe2));

		// Act
		List<Recipe> result = recipeService.getAllRecipes();

		// Assert
		assertEquals(2, result.size());
		assertEquals("Pasta Carbonara", result.get(0).getName());
	}

	// Test 2: getRecipeById returns correct recipe
	@Test
	void getRecipeById_returnsCorrectRecipe() {
		// Arrange
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		recipe.setName("Caesar Salad");

		when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

		// Act
		Optional<Recipe> result = recipeService.getRecipeById(1L);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("Caesar Salad", result.get().getName());
	}

	// Test 3: addRecipe saves and returns recipe
	@Test
	void addRecipe_savesAndReturnsRecipe() {
		// Arrange
		Recipe recipe = new Recipe();
		recipe.setName("Omelette");
		recipe.setCategory("Breakfast");

		when(recipeRepository.save(recipe)).thenReturn(recipe);

		// Act
		Recipe result = recipeService.addRecipe(recipe);

		// Assert
		assertNotNull(result);
		assertEquals("Omelette", result.getName());
		verify(recipeRepository, times(1)).save(recipe);
	}

	// Test 4: deleteRecipe calls repository
	@Test
	void deleteRecipe_callsRepository() {
		// Act
		recipeService.deleteRecipe(1L);

		// Assert
		verify(recipeRepository, times(1)).deleteById(1L);
	}
}