package se.umu.cs.oi19aws.makrokoll.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_recipes", foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recipe_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Saved(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val recipe_id: Int
)

