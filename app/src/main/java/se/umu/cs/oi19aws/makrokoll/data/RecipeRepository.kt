package se.umu.cs.oi19aws.makrokoll.data

import androidx.lifecycle.LiveData

class RecipeRepository(private val recipeDao: RecipeDAO) {

    val getAllRecipe: LiveData<List<Recipe>> = recipeDao.getAllRecipe()

    fun addRecipe(recipe: Recipe) {
        recipeDao.addRecipe(recipe)
    }

    fun getRecipe(id: Int): LiveData<Recipe> {
        return recipeDao.getRecipe(id)
    }
}