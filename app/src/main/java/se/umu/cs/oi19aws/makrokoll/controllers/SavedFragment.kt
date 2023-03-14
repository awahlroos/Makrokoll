package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel
import se.umu.cs.oi19aws.makrokoll.databinding.FragmentDiscoverBinding
import se.umu.cs.oi19aws.makrokoll.databinding.FragmentSavedBinding
import se.umu.cs.oi19aws.makrokoll.models.RecipeCardModel
import se.umu.cs.oi19aws.makrokoll.models.RecipeCardRecyclerViewAdapter
import se.umu.cs.oi19aws.makrokoll.models.SpacesItemDecorator


class SavedFragment : Fragment(), RecyclerViewInterface {

    private lateinit var viewModelRecipe:RecipeViewModel
    private lateinit var binding: FragmentSavedBinding
    private var recipeModel = ArrayList<RecipeCardModel>()
    private lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.recyclerView
        val adapter = this.activity?.let { RecipeCardRecyclerViewAdapter(it, recipeModel, this) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.activity, 2)
        recyclerView.addItemDecoration(SpacesItemDecorator(16, 450))
        getSavedRecipes()
    }

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

    override fun onItemClick(position: Int) {
        val i = Intent(activity, DetailedRecipeActivity::class.java)
        i.putExtra("id", recipeModel[position].getId())
        startActivity(i)
    }

}