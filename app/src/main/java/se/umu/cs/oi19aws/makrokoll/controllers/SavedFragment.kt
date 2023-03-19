package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel
import se.umu.cs.oi19aws.makrokoll.databinding.FragmentSavedBinding
import se.umu.cs.oi19aws.makrokoll.models.RecipeCardModel
import se.umu.cs.oi19aws.makrokoll.models.SpacesItemDecorator

// Fragment to provide the saved recipes page. Here the user can browse all the saved recipes.
class SavedFragment : Fragment(), RecyclerViewInterface {

    private lateinit var viewModelRecipe:RecipeViewModel
    private lateinit var binding: FragmentSavedBinding
    private lateinit var recyclerView:RecyclerView
    private var recipeModel = ArrayList<RecipeCardModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        recyclerView = binding.recyclerView
        val adapter = this.activity?.let { RecipeCardRecyclerViewAdapter(it, recipeModel, this) }
        recyclerView.adapter = adapter
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRecyclerViewStyling(3)
        } else {
            setRecyclerViewStyling(2)
        }
        getSavedRecipes()
    }

    //Adds appropriate padding between columns in recycler view
    private fun setRecyclerViewStyling(columnCount: Int) {
        recyclerView.layoutManager = GridLayoutManager(this.activity, columnCount)
        removeItemDecorations(recyclerView)
        recyclerView.addItemDecoration(SpacesItemDecorator(columnCount, 22, 450))
    }

    private fun removeItemDecorations(recyclerView: RecyclerView){
        while (recyclerView.itemDecorationCount > 0) {
            recyclerView.removeItemDecorationAt(0)
        }
    }

    //Get all saved recipes from the database
    private fun getSavedRecipes() {
        viewModelRecipe.getSavedRecipes().observe(viewLifecycleOwner) { recipes ->
            recipeModel.clear()
            for (recipe in recipes) {
                val icon: String = if (recipe.activeFilters.size > 1) {
                    "multiple"
                } else {
                    recipe.activeFilters[0]
                }
                updateRecipeModel(
                    recipe.id,
                    recipe.name,
                    recipe.kcal,
                    recipe.protein,
                    recipe.fat,
                    recipe.carbs,
                    icon,
                    recipe.image
                )
            }
        }
    }

    //Update the recipe model which is used for storing the database response
    private fun updateRecipeModel(
        id: Int,
        name: String,
        kcal: Int,
        protein: Int,
        fat: Int,
        carbs: Int,
        icon: String,
        image: String
    ) {
        recipeModel.add(RecipeCardModel(id, name, kcal, protein, fat, carbs, icon, image))
        recyclerView.adapter?.notifyDataSetChanged()
    }
    
    //Open a detailed view of the clicked recipe
    override fun onItemClick(position: Int) {
        val i = Intent(activity, DetailedRecipeActivity::class.java)
        i.putExtra("id", recipeModel[position].getId())
        startActivity(i)
    }

}