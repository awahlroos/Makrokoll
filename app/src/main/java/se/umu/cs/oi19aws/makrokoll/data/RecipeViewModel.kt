package se.umu.cs.oi19aws.makrokoll.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    val getAllRecipe: LiveData<List<Recipe>>
    private val repository: RecipeRepository
    //private lateinit var getRecipe: LiveData<Recipe>
    //private val getRecipe: LiveData<Recipe> = getRecipe(id)

    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        getAllRecipe = repository.getAllRecipe
    }

    fun getRecipe(id: Int) : LiveData<Recipe> {
        Log.d("TAG", "id: $id")
        return repository.getRecipe(id)
        //viewModelScope.launch(Dispatchers.IO) {
         //
        //}
        //return getRecipe
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecipe(recipe)
        }
    }
}