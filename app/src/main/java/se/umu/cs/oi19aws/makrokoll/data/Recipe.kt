package se.umu.cs.oi19aws.makrokoll.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import se.umu.cs.oi19aws.makrokoll.models.IngredientsModel

@Entity(tableName = "recipe_table")
@TypeConverters(DataConverter::class)
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val nrOfServings: Int,
    val ingredients: ArrayList<IngredientsModel>,
    val kcal: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int,
    val activeFilters: ArrayList<String>,
    val stepByStep: String,
    val image: String
)