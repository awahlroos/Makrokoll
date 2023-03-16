package se.umu.cs.oi19aws.makrokoll.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// View model for recipes. Communicates via the repository.
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    val getAllRecipe: LiveData<List<Recipe>>
    private val repository: RecipeRepository


    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        getAllRecipe = repository.getAllRecipe
    }

    fun getRecipe(id: Int): LiveData<Recipe> {
        return repository.getRecipe(id)
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecipe(recipe)
        }
    }

    fun searchRecipes(query: String): LiveData<List<Recipe>> {
        return repository.searchRecipes(query)
    }

    fun searchRecipesFilter(filter1:String,
                            filter2:String,
                            filter3:String,
                            filter4:String,
                            filter5:String,
                            filter6:String,
                            filter7:String,
                            filter8:String,
                            filter9:String): LiveData<List<Recipe>> {
        return repository.searchRecipesFilter(filter1,filter2,filter3,filter4,filter5,filter6,filter7, filter8, filter9)
    }

    fun addSavedRecipe(savedRecipe: Saved) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSavedRecipe(savedRecipe)
        }
    }

    fun recipeIsSaved(id: Int): LiveData<Boolean> {
        return repository.recipeIsSaved(id)
    }

    fun getSavedRecipes(): LiveData<List<Recipe>> {
        return repository.getSavedRecipes()
    }

    fun deleteSavedRecipe(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSavedRecipe(id)
        }
    }
}