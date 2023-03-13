package se.umu.cs.oi19aws.makrokoll.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    fun getAllRecipe(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE id=:id")
    fun getRecipe(id:Int): LiveData<Recipe>
}