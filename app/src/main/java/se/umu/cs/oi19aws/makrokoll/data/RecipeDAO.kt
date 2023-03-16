package se.umu.cs.oi19aws.makrokoll.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDAO {
    // --------- All recipes ---------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    fun getAllRecipe(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE id=:id")
    fun getRecipe(id: Int): LiveData<Recipe>

    @Query("SELECT * FROM recipe_table WHERE name LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE activeFilters LIKE '%'||:filter1||'%'" +
            "OR activeFilters LIKE '%'||:filter2||'%'" +
            "OR activeFilters LIKE '%'||:filter3||'%'" +
            "OR activeFilters LIKE '%'||:filter4||'%'" +
            "OR activeFilters LIKE '%'||:filter5||'%'" +
            "OR activeFilters LIKE '%'||:filter6||'%'" +
            "OR activeFilters LIKE '%'||:filter7||'%'" +
            "OR activeFilters LIKE '%'||:filter8||'%'" +
            "OR activeFilters LIKE '%'||:filter9||'%'")
    fun searchRecipesFilter(filter1:String,
                            filter2:String,
                            filter3:String,
                            filter4:String,
                            filter5:String,
                            filter6:String,
                            filter7:String,
                            filter8:String,
                            filter9:String): LiveData<List<Recipe>>

    // --------- Saved recipes ---------
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSavedRecipe(saved: Saved)

    @Query("SELECT * FROM recipe_table WHERE id IN (SELECT recipe_id FROM saved_recipes)")
    fun getSavedRecipes(): LiveData<List<Recipe>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_recipes WHERE recipe_id=:id)")
    fun recipeIsSaved(id: Int): LiveData<Boolean>

    @Query("DELETE FROM saved_recipes WHERE recipe_id=:id")
    fun deleteSavedRecipes(id: Int)
}