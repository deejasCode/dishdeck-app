package com.dishdeck.backend.service;

import com.dishdeck.backend.model.Recipe;
import com.dishdeck.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // Get all recipes
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Get one recipe by id
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    // Add a new recipe
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // Update an existing recipe
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        updatedRecipe.setId(id);
        return recipeRepository.save(updatedRecipe);
    }

    // Delete a recipe
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
