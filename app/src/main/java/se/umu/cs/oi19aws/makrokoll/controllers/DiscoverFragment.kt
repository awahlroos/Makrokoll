package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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
import se.umu.cs.oi19aws.makrokoll.models.SpacesItemDecorator

// Fragment class to show the home page, or "Discover" page. Displays all the recipes
// in the database as well as providing the user with filtering and search options.
class DiscoverFragment : Fragment(), RecyclerViewInterface {

    private lateinit var binding: FragmentDiscoverBinding
    private var recipeModel = ArrayList<RecipeCardModel>()
    private lateinit var viewModelRecipe: RecipeViewModel
    private lateinit var recyclerView: RecyclerView
    private var filterButtonList = ArrayList<Button>()
    private var filterStringList = ArrayList<String>()
    private lateinit var mapButtonToString: Map<Button, String>
    private lateinit var mapStringToButton: Map<String, Button>
    private var queryString = ""

    private lateinit var buttonFish: Button
    private lateinit var buttonPork: Button
    private lateinit var buttonBird: Button
    private lateinit var buttonMilk: Button
    private lateinit var buttonBeef: Button
    private lateinit var buttonVeg: Button
    private lateinit var buttonGame: Button
    private lateinit var buttonEgg: Button
    private lateinit var buttonOthers: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        setClearButtonVisibility(false)
        binding.clearButton.setOnClickListener {
            getAllRecipes()
            queryString = ""
            setClearButtonVisibility(false)
        }
        setUpRecyclerView()
        if (savedInstanceState == null) {
            getAllRecipes()
        }
        setUpFilterList()
        return binding.root
    }

    //Set up the actionbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu(savedInstanceState)
    }

    //Configure a new recycler view when screen orientation changes (adds/removes one recipe column)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setUpRecyclerView()
    }

    //Set up the actionbar
    private fun setupMenu(savedInstanceState: Bundle?) {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider,
            androidx.core.view.MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actionbar, menu)
                val search = menu.findItem(R.id.search)
                val searchView = search.actionView as SearchView

                //Restore the last search if activity is recreated
                if (savedInstanceState != null) {
                    restoreSearch(savedInstanceState, searchView)
                }

                //Set listener to handle searches from the searchView in the actionbar
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        initQueryListener(query, search)
                        deactivateFilterButtons()
                        return false
                    }

                    //Ignore query while user is still typing
                    override fun onQueryTextChange(newText: String): Boolean {
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

    private fun restoreSearch(savedInstanceState: Bundle?, searchView: SearchView) {
        //Get the stored query
        queryString = savedInstanceState!!.getString(QUERY_KEY).toString()
        //If there was an active query, restore the query and view accordingly
        if (queryString.isNotBlank()) {
            searchView.setQuery(queryString, true)
            updateRecyclerViewSearch(queryString)
            setClearButtonVisibility(true)
        } else {
            //Get the stored filters
            filterStringList = savedInstanceState.getStringArrayList(FILTER_KEY)!!
            for (item in filterStringList) {
                mapStringToButton[item]?.isActivated = true
            }
            //No query and no filters, get all recipes
            if (filterStringList.isEmpty()) {
                getAllRecipes()
            } else {
                //Update to the filtered view
                updateRecyclerViewFilter()
            }
        }
    }

    private fun initQueryListener(query: String, search: MenuItem) {
        if (query.isNotBlank()) {
            queryString = query
        }
        search.collapseActionView()
        updateRecyclerViewSearch(query)
        if (query.isNotBlank()) {
            setClearButtonVisibility(true)
        } else {
            setClearButtonVisibility(false)
        }
    }

    private fun setClearButtonVisibility(visible: Boolean) {
        if (visible) {
            binding.clearButton.visibility = View.VISIBLE
        } else {
            binding.clearButton.visibility = View.INVISIBLE
        }
    }

    private fun setUpFilterList() {
        initFilterButtons()
        fillFilterButtonList()
        initButtonMapping()

        for (button in filterButtonList) {
            button.setOnClickListener {
                addFilterButtonListener(button)
            }
        }

    }

    private fun initFilterButtons() {
        buttonFish = binding.filterButtonFisk
        buttonPork = binding.filterButtonFlask
        buttonBird = binding.filterButtonFagel
        buttonMilk = binding.filterButtonMjolk
        buttonBeef = binding.filterButtonNot
        buttonVeg = binding.filterButtonVeg
        buttonGame = binding.filterButtonVilt
        buttonEgg = binding.filterButtonAgg
        buttonOthers = binding.filterButtonOvrigt
    }

    private fun fillFilterButtonList() {
        filterButtonList.add(buttonFish)
        filterButtonList.add(buttonPork)
        filterButtonList.add(buttonBird)
        filterButtonList.add(buttonMilk)
        filterButtonList.add(buttonBeef)
        filterButtonList.add(buttonVeg)
        filterButtonList.add(buttonGame)
        filterButtonList.add(buttonEgg)
        filterButtonList.add(buttonOthers)
    }

    private fun initButtonMapping() {
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

        mapStringToButton = mapOf(
            "fish" to buttonFish,
            "pork" to buttonPork,
            "bird" to buttonBird,
            "milk" to buttonMilk,
            "beef" to buttonBeef,
            "veg" to buttonVeg,
            "game" to buttonGame,
            "egg" to buttonEgg,
            "others" to buttonOthers
        )
    }

    private fun addFilterButtonListener(button: Button) {
        binding.recipeNameTV.text = getString(R.string.all_results)
        setClearButtonVisibility(false)
        queryString = ""

        button.isActivated = !button.isActivated
        if (button.isActivated) {
            filterStringList.add(mapButtonToString[button]!!)
        } else {
            filterStringList.remove(mapButtonToString[button]!!)
        }

        if (filterStringList.isEmpty()) {
            getAllRecipes()
        } else {
            updateRecyclerViewFilter()
        }
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.recyclerView
        val adapter = this.activity?.let { RecipeCardRecyclerViewAdapter(it, recipeModel, this) }
        recyclerView.adapter = adapter

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRecyclerViewStyling(3)
        } else {
            setRecyclerViewStyling(2)
        }
    }

    //Adds appropriate padding between columns in recycler view
    private fun setRecyclerViewStyling(columnCount: Int) {
        recyclerView.layoutManager = GridLayoutManager(this.activity, columnCount)
        removeItemDecorations(recyclerView)
        recyclerView.addItemDecoration(SpacesItemDecorator(columnCount, 22, 450))
    }

    private fun removeItemDecorations(recyclerView: RecyclerView) {
        while (recyclerView.itemDecorationCount > 0) {
            recyclerView.removeItemDecorationAt(0)
        }
    }

    //Get all recipes from the database
    private fun getAllRecipes() {
        binding.recipeNameTV.text = getString(R.string.all_results)
        viewModelRecipe.getAllRecipe.observe(viewLifecycleOwner) { recipes ->
            filterStringList.clear()
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

    //Open a detailed view of the clicked recipe
    override fun onItemClick(position: Int) {
        val i = Intent(activity, DetailedRecipeActivity::class.java)
        i.putExtra("id", recipeModel[position].getId())
        startActivity(i)
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

    //Update the recycler view on user search
    private fun updateRecyclerViewSearch(query: String) {
        viewModelRecipe.searchRecipes(query).observe(viewLifecycleOwner) { recipes ->
            recipeModel.clear()
            recyclerView.adapter?.notifyDataSetChanged()
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
            if (query.isNotBlank()) {
                binding.recipeNameTV.text = getString(R.string.search_string_results, query)
            }
        }
    }

    //Update the recycler view based on active filters
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
        ).observe(viewLifecycleOwner) { recipes ->
            recipeModel.clear()
            recyclerView.adapter?.notifyDataSetChanged()
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

    private fun deactivateFilterButtons() {
        for (button in filterButtonList) {
            button.isActivated = false
            if(filterStringList.isNotEmpty()){
                filterStringList.remove(mapButtonToString[button]!!)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(FILTER_KEY, filterStringList)
        outState.putString(QUERY_KEY, queryString)
    }

    companion object {
        private const val FILTER_KEY = "DiscoverFragment.Filter"
        private const val QUERY_KEY = "DiscoverFragment.Query"
    }
}