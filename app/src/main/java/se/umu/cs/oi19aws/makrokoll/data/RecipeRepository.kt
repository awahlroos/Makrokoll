package se.umu.cs.oi19aws.makrokoll.data

import androidx.lifecycle.LiveData

// Repository for managing database operations. Communicates with the DAO.
class RecipeRepository(private val recipeDao: RecipeDAO) {

    val getAllRecipe: LiveData<List<Recipe>> = recipeDao.getAllRecipe()

    fun addRecipe(recipe: Recipe) {
        recipeDao.addRecipe(recipe)
    }

    fun getRecipe(id: Int): LiveData<Recipe> {
        return recipeDao.getRecipe(id)
    }

    fun searchRecipes(query: String): LiveData<List<Recipe>> = recipeDao.searchRecipes(query)

    fun searchRecipesFilter(
        filter1:String,
        filter2:String,
        filter3:String,
        filter4:String,
        filter5:String,
        filter6:String,
        filter7:String,
        filter8:String,
        filter9:String
    ): LiveData<List<Recipe>> =
        recipeDao.searchRecipesFilter(filter1,filter2,filter3,filter4,filter5,filter6,filter7, filter8, filter9)

    fun addSavedRecipe(savedRecipe: Saved) {
        recipeDao.addSavedRecipe(savedRecipe)
    }

    fun getSavedRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getSavedRecipes()
    }

    fun recipeIsSaved(id: Int): LiveData<Boolean> {
        return recipeDao.recipeIsSaved(id)
    }

    fun deleteSavedRecipe(id: Int) {
        recipeDao.deleteSavedRecipes(id)
    }
}