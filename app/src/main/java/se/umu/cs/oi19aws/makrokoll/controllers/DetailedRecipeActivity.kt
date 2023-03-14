package se.umu.cs.oi19aws.makrokoll.controllers

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.data.Recipe
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel
import se.umu.cs.oi19aws.makrokoll.data.Saved
import se.umu.cs.oi19aws.makrokoll.databinding.ActivityDetailedRecipeBinding
import se.umu.cs.oi19aws.makrokoll.models.IngredientsRecyclerViewAdapter
import java.io.File

class DetailedRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedRecipeBinding
    private lateinit var viewModelRecipe: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getIntExtra("id", -1)
        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]

        val saveButton = binding.saveButton

        viewModelRecipe.recipeIsSaved(recipeId).observe(this) { isSaved ->
            if(isSaved == true){
                saveButton.isActivated = true
            }
        }

        saveButton.setOnClickListener {
            val savedRecipe = Saved(0,recipeId)
            saveButton.isActivated = !saveButton.isActivated
            if(saveButton.isActivated){
                viewModelRecipe.addSavedRecipe(savedRecipe)
                Toast.makeText(this, "Recept sparat", Toast.LENGTH_SHORT).show()
            } else {
                viewModelRecipe.deleteSavedRecipe(savedRecipe.recipe_id)
                Toast.makeText(this, "Recept borttaget från sparade", Toast.LENGTH_SHORT).show()
            }
        }

        val recipe: LiveData<Recipe> = viewModelRecipe.getRecipe(recipeId)

        recipe.observe(this) { recipe ->
            supportActionBar?.title = recipe.name
            binding.kcalText.text = getString(R.string.nr_kcal, recipe.kcal)
            binding.PFCText.text =
                getString(R.string.PFC_info, recipe.protein, recipe.fat, recipe.carbs)
            if (recipe.activeFilters.size == 1) {
                binding.proteinIcon.setImageResource(
                    resources.getIdentifier(
                        "ic_${recipe.activeFilters.first()}",
                        "drawable",
                        "se.umu.cs.oi19aws.makrokoll"
                    )
                )
            } else {
                binding.proteinIcon.setImageResource(
                    resources.getIdentifier(
                        "ic_multiple",
                        "drawable",
                        "se.umu.cs.oi19aws.makrokoll"
                    )
                )
            }
            binding.recipeImage.setImageBitmap(
                BitmapFactory.decodeFile(
                    File(
                        applicationContext.filesDir,
                        recipe.image
                    ).absolutePath
                )
            )
            binding.recipeName.text = recipe.name
            binding.recipeDescription.text = recipe.description
            binding.servings.text = getString(R.string.nr_of_servings, recipe.nrOfServings)
            //TODO: Inflate recyclerView here

            val adapter = IngredientsRecyclerViewAdapter(this, recipe.ingredients, false)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)

            binding.stepByStep.text = recipe.stepByStep
        }

    }
}