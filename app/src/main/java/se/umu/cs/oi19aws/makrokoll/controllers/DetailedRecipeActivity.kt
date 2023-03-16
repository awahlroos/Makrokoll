package se.umu.cs.oi19aws.makrokoll.controllers

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
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

// Class to show a detailed view of a recipe. Presents all the information about a specific recipe
class DetailedRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedRecipeBinding
    private lateinit var viewModelRecipe: RecipeViewModel
    private lateinit var saveButton: ImageButton
    private var recipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]

        recipeId = intent.getIntExtra("id", -1)
        saveButton = binding.saveButton

        //Set the button to activated if the recipe is already saved
        viewModelRecipe.recipeIsSaved(recipeId).observe(this) { isSaved ->
            if (isSaved == true) {
                saveButton.isActivated = true
            }
        }

        //Set listener to handle save button
        setSaveButtonListener()
        //Get the recipe which the user clicked
        getRecipe()
    }

    private fun getRecipe() {
        val recipe: LiveData<Recipe> = viewModelRecipe.getRecipe(recipeId)

        //When recipe is fetched
        recipe.observe(this) { detailedRecipe ->
            supportActionBar?.title = detailedRecipe.name

            setTextViews(detailedRecipe)
            setRecipeIcon(detailedRecipe)
            setRecipeImage(detailedRecipe)

            val adapter = IngredientsRecyclerViewAdapter(this, detailedRecipe.ingredients, false)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }

    }

    private fun setTextViews(detailedRecipe: Recipe) {
        binding.kcalText.text = getString(R.string.nr_kcal, detailedRecipe.kcal)
        binding.PFCText.text =
            getString(
                R.string.PFC_info,
                detailedRecipe.protein,
                detailedRecipe.fat,
                detailedRecipe.carbs
            )
        binding.recipeName.text = detailedRecipe.name
        binding.recipeDescription.text = detailedRecipe.description
        binding.servings.text = getString(R.string.nr_of_servings, detailedRecipe.nrOfServings)
        binding.stepByStep.text = detailedRecipe.stepByStep
    }

    private fun setRecipeIcon(detailedRecipe: Recipe) {
        if (detailedRecipe.activeFilters.size == 1) {
            binding.proteinIcon.setImageResource(
                //Get icon of single filter
                resources.getIdentifier(
                    "ic_${detailedRecipe.activeFilters.first()}",
                    "drawable",
                    "se.umu.cs.oi19aws.makrokoll"
                )
            )
        } else {
            binding.proteinIcon.setImageResource(
                //Get icon of multiple filters
                resources.getIdentifier(
                    "ic_multiple",
                    "drawable",
                    "se.umu.cs.oi19aws.makrokoll"
                )
            )
        }
    }

    private fun setRecipeImage(detailedRecipe: Recipe) {
        binding.recipeImage.setImageBitmap(
            //Get image from filename
            BitmapFactory.decodeFile(
                File(
                    applicationContext.filesDir,
                    detailedRecipe.image
                ).absolutePath
            )
        )
    }

    //Save or remove recipe from "Saved recipes"
    private fun setSaveButtonListener() {
        saveButton.setOnClickListener {
            val savedRecipe = Saved(0, recipeId)
            saveButton.isActivated = !saveButton.isActivated
            if (saveButton.isActivated) {
                viewModelRecipe.addSavedRecipe(savedRecipe)
                Toast.makeText(this, "Recept sparat", Toast.LENGTH_SHORT).show()
            } else {
                viewModelRecipe.deleteSavedRecipe(savedRecipe.recipe_id)
                Toast.makeText(this, "Recept borttaget från sparade", Toast.LENGTH_SHORT).show()
            }
        }
    }
}