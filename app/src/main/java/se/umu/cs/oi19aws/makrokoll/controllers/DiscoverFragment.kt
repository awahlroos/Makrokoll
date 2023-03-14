package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.R
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
    private lateinit var filterButtonList: ArrayList<Button>
    private lateinit var filterStringList: ArrayList<String>
    private lateinit var mapButtonToString: Map<Button, String>
    private lateinit var searchView:SearchView

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
    ): View {
        if (!isInitialized) {
            viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
            binding = FragmentDiscoverBinding.inflate(inflater, container, false)
            binding.clearButton.visibility = View.INVISIBLE
            binding.clearButton.setOnClickListener {
                getAllRecipes()
                binding.clearButton.visibility = View.INVISIBLE
            }
            setUpRecyclerView()
            setUpFilterList()
            isInitialized = true
        }
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider,
            androidx.core.view.MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actionbar, menu)
                val search = menu.findItem(R.id.search)
                searchView = search.actionView as SearchView

                //Set listener to handle searches from the searchView in the actionbar
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        search.collapseActionView()
                        updateRecyclerViewSearch(query)
                        //TODO: REmove boilerplate code
                        if (query.isNotBlank()) {
                            binding.clearButton.visibility = View.VISIBLE
                        } else {
                            binding.clearButton.visibility = View.INVISIBLE
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        updateRecyclerViewSearch(newText)
                        if (newText.isNotBlank()) {
                            binding.clearButton.visibility = View.VISIBLE
                        } else {
                            binding.clearButton.visibility = View.INVISIBLE
                        }
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpFilterList() {

        filterButtonList = ArrayList()
        filterStringList = ArrayList()

        val buttonFish = binding.filterButtonFisk
        val buttonPork = binding.filterButtonFlask
        val buttonBird = binding.filterButtonFagel
        val buttonMilk = binding.filterButtonMjolk
        val buttonBeef = binding.filterButtonNot
        val buttonVeg = binding.filterButtonVeg
        val buttonGame = binding.filterButtonVilt
        val buttonEgg = binding.filterButtonAgg
        val buttonOthers = binding.filterButtonOvrigt

        filterButtonList.add(binding.filterButtonFisk)
        filterButtonList.add(binding.filterButtonFlask)
        filterButtonList.add(binding.filterButtonFagel)
        filterButtonList.add(binding.filterButtonMjolk)
        filterButtonList.add(binding.filterButtonNot)
        filterButtonList.add(binding.filterButtonVeg)
        filterButtonList.add(binding.filterButtonVilt)
        filterButtonList.add(binding.filterButtonAgg)
        filterButtonList.add(binding.filterButtonOvrigt)

        mapButtonToString = mapOf(
            buttonFish to "fish",
            buttonPork to "pork",
            buttonBird to "bird",
            buttonMilk to "milk",
            buttonBeef to "beef",
            buttonVeg to "veg",
            buttonGame to "game",
            buttonEgg to "egg",
            buttonOthers to "others",
        )

        for (button in filterButtonList) {
            button.setOnClickListener {
                //searchView.setQuery("", true)
                button.isActivated = !button.isActivated
                if (button.isActivated) {
                    Log.d("TAG", "Activated ${mapButtonToString[button]!!}")
                    filterStringList.add(mapButtonToString[button]!!)
                } else {
                    Log.d("TAG", "Removed ${mapButtonToString[button]!!}")
                    filterStringList.remove(mapButtonToString[button]!!)
                }

                if (filterStringList.isEmpty()) {
                    getAllRecipes()
                } else {
                    Log.d("TAG", "Not empty: ${filterStringList}")
                    updateRecyclerViewFilter()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.recyclerView
        val adapter = this.activity?.let { RecipeCardRecyclerViewAdapter(it, recipeModel, this) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.activity, 2)
        recyclerView.addItemDecoration(SpacesItemDecorator(16, 450))
        getAllRecipes()
    }

    private fun getAllRecipes() {
        viewModelRecipe.getAllRecipe.observe(viewLifecycleOwner) { recipes ->
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

    fun updateRecyclerViewSearch(query: String) {
        //Deactivate combined filter and search for less complex logic
        deactivateFilterButtons()
        //TODO : Remove boilerplate code
        viewModelRecipe.searchRecipes(query).observe(viewLifecycleOwner) { recipes ->
            recipeModel.clear()
            recyclerView.adapter?.notifyDataSetChanged()
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

    private fun updateRecyclerViewFilter() {

        val tempFilterStringList = ArrayList<String>(filterStringList)

        while (tempFilterStringList.size < 9) {
            tempFilterStringList.add("blank")
        }

        viewModelRecipe.searchRecipesFilter(
            tempFilterStringList[0],
            tempFilterStringList[1],
            tempFilterStringList[2],
            tempFilterStringList[3],
            tempFilterStringList[4],
            tempFilterStringList[5],
            tempFilterStringList[6],
            tempFilterStringList[7],
            tempFilterStringList[8]
        )
            .observe(viewLifecycleOwner) { recipes ->
                recipeModel.clear()
                recyclerView.adapter?.notifyDataSetChanged()
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

    private fun deactivateFilterButtons() {
        for (button in filterButtonList) {
            button.isActivated = false
            filterStringList.remove(mapButtonToString[button]!!)
        }
    }
}