package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.data.Recipe
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel
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

        val id = intent.getIntExtra("id", -1)
        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
        Log.d("TAG", "id: $id")

        val recipe: LiveData<Recipe> = viewModelRecipe.getRecipe(id)

        recipe.observe(this) { recipe ->
            supportActionBar?.title = recipe.name
            binding.kcalText.text = getString(R.string.nr_kcal, recipe.kcal)
            binding.PFCText.text =
                getString(R.string.PFC_info, recipe.protein, recipe.fat, recipe.carbs)
            if (recipe.activeFilters.size == 1) {
                binding.proteinIcon.setImageResource(
                    resources.getIdentifier(
                        "ic_${recipe.activeFilters[0]}",
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
            Log.d("TAG", "image. ${recipe.image}")
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