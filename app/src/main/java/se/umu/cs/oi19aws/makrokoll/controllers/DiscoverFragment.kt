package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.RecyclerViewInterface
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel
import se.umu.cs.oi19aws.makrokoll.databinding.FragmentDiscoverBinding
import se.umu.cs.oi19aws.makrokoll.models.RecipeCardModel
import se.umu.cs.oi19aws.makrokoll.models.RecipeCardRecyclerViewAdapter
import se.umu.cs.oi19aws.makrokoll.models.SpacesItemDecorator


class DiscoverFragment : Fragment(), RecyclerViewInterface {

    private lateinit var binding: FragmentDiscoverBinding
    private var recipeModel = ArrayList<RecipeCardModel>()
    private lateinit var viewModelRecipe: RecipeViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //val allRecipes = viewModelRecipe.getAllRecipe


        //val recipe: Recipe = viewModelRecipe.getRecipe(1)

        //updateRecipeModel("Curry", 12, 12, 12, 12, "beef", "img_currygryta")
    }

    private var isInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!isInitialized) {
            viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
            binding = FragmentDiscoverBinding.inflate(inflater, container, false)
            setUpRecyclerView()
            isInitialized = true
        }
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.recyclerView
        val adapter = this.activity?.let { RecipeCardRecyclerViewAdapter(it, recipeModel, this) }
        recyclerView.adapter = adapter
        //recyclerView.layoutManager = FlexboxLayoutManager(this.activity)
        recyclerView.layoutManager = GridLayoutManager(this.activity, 2)
        recyclerView.addItemDecoration(SpacesItemDecorator(16, 450))

        viewModelRecipe.getAllRecipe.observe(viewLifecycleOwner) { recipes ->
            recipeModel.clear()
            for (recipe in recipes) {
                var icon: String = if (recipe.activeFilters.size > 1) {
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

    override fun onItemClick(position: Int) {
        val i = Intent(activity, DetailedRecipeActivity::class.java)
        i.putExtra("id", recipeModel[position].getId())
        startActivity(i)
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
}