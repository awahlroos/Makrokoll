package se.umu.cs.oi19aws.makrokoll.controllers

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.data.Recipe
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel
import se.umu.cs.oi19aws.makrokoll.databinding.ActivityAddRecipeBinding
import se.umu.cs.oi19aws.makrokoll.models.IngredientsModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// Class to handle activity for creating a new recipe.
// The user can make a new recipe with a name, description, nutrient info etc.
class AddRecipeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var nrOfServingsSpinner: Spinner
    private lateinit var measurementSpinner: Spinner
    private lateinit var activeFiltersList: ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var measurementUnit: String
    private lateinit var viewModelRecipe: RecipeViewModel

    private var filterList = ArrayList<Button>()
    private var nrOfServings = 0
    private var ingredientModel = ArrayList<IngredientsModel>()
    private var activeFiltersListBoolean = BooleanArray(9)

    private lateinit var file: File
    private lateinit var viewModelImage: ImageViewModel
    private val imageName = generateImageName()

    private val launcher = registerForActivityResult(
        //Use contract to take picture
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            viewModelImage.file = File(filesDir, imageName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelImage = ViewModelProvider(this)[ImageViewModel::class.java]
        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
        supportActionBar?.title = "Publicera nytt recept"

        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleRestore(savedInstanceState)
        initNrOfServingsSpinner()
        initMeasurementSpinner()
        setAddIngredientListener()
        initFilterList()
        setPublishListener()
        setUpImageHandler()
    }

    private fun handleRestore(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            //Restore boolean values for the filters (if active or not)
            activeFiltersListBoolean = savedInstanceState.getBooleanArray(FILTER_KEY)!!

            //Setup recyclerview again
            ingredientModel = savedInstanceState.getParcelableArrayList(INGREDIENT_KEY)!!
            recyclerView = binding.recyclerView
            val adapter = IngredientsRecyclerViewAdapter(this, ingredientModel, true)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun setUpImageHandler() {
        viewModelImage.bitmap.observe(this) {
            binding.postedImageIV.setImageBitmap(it)
        }

        //Set image in view model
        file = File(filesDir, imageName)
        if (file.exists() && viewModelImage.file == null) {
            viewModelImage.file = file
        }

        binding.addImageButton.setOnClickListener { takePicture() }
    }


    private fun initNrOfServingsSpinner() {
        nrOfServingsSpinner = binding.nrOfServingsSpinner

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.numbers, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        nrOfServingsSpinner.adapter = adapter
        nrOfServingsSpinner.onItemSelectedListener = this
    }

    private fun initMeasurementSpinner() {
        measurementSpinner = binding.measurementUnitSpinner

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.measurements, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        measurementSpinner.adapter = adapter
        measurementSpinner.onItemSelectedListener = this
    }

    //Spinner item is selected
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent == binding.nrOfServingsSpinner) {
            nrOfServings = parent.getItemAtPosition(position).toString().toInt()
        }
        if (parent == binding.measurementUnitSpinner) {
            measurementUnit = parent.getItemAtPosition(position).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }

    private fun setAddIngredientListener() {
        val addIngredientButton = binding.addIngredientButton

        addIngredientButton.setOnClickListener {
            val weightVolume = binding.weightVolumeET.text.toString()
            val ingredientName = binding.ingredientNameET.text.toString().lowercase()

            //Return if field is empty on submit
            if (weightVolume.isBlank()) {
                Toast.makeText(this, "Ange vikt/volym", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //Return if field is empty on submit
            if (ingredientName.isBlank()) {
                Toast.makeText(this, "Ange namn på ingrediens", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            addIngredient(weightVolume, ingredientName)
        }
    }

    //Add the ingredient to the ingredient model
    private fun addIngredient(weightVolume: String, ingredientName: String) {
        updateIngredientModel(weightVolume, measurementUnit, ingredientName)
        val adapter = IngredientsRecyclerViewAdapter(this, ingredientModel, true)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //Reset the field for next input
        binding.weightVolumeET.text.clear()
        binding.ingredientNameET.text.clear()
        binding.weightVolumeET.requestFocus()
    }

    private fun initFilterList() {
        filterList.add(binding.filterButtonFish)
        filterList.add(binding.filterButtonPork)
        filterList.add(binding.filterButtonBird)
        filterList.add(binding.filterButtonMilk)
        filterList.add(binding.filterButtonBeef)
        filterList.add(binding.filterButtonVeg)
        filterList.add(binding.filterButtonGame)
        filterList.add(binding.filterButtonEgg)
        filterList.add(binding.filterButtonOthers)

        for (btn in filterList) {
            //Activated state of the buttons is restored
            btn.isActivated = activeFiltersListBoolean[filterList.indexOf(btn)]
            btn.setOnClickListener {
                //Toggle between active / not active
                activeFiltersListBoolean[filterList.indexOf(btn)] =
                    !activeFiltersListBoolean[filterList.indexOf(btn)]
                btn.isActivated = !btn.isActivated
            }
        }
    }

    private fun setPublishListener() {
        val publishButton = binding.publishButton
        activeFiltersList = ArrayList()

        publishButton.setOnClickListener {
            //Fill the active filters list
            for (btn in filterList) {
                if (btn.isActivated) {
                    activeFiltersList.add(
                        //Store only the filter phrase, ie. "beef" or "egg"
                        resources.getResourceEntryName(btn.id).substring(12).lowercase()
                    )
                }
            }

            if (correctlyFilled()) {
                postToDatabase()
                finish()
            }
        }
    }

    //Check all input and verify it is correct
    private fun correctlyFilled(): Boolean {
        if (binding.recipeNameET.text.toString().isEmpty()) {
            showError("\"Namn på recept\" saknas", binding.recipeNameET)
            return false
        }

        if (binding.kcalET.text.toString().isEmpty()) {
            showError("Antal kalorier i \"Näringsvärde per portion\" saknas", binding.kcalET)
            return false
        }

        if (binding.proteinET.text.toString().isEmpty()) {
            showError("Protein (g) i \"Näringsvärde per portion\" saknas", binding.proteinET)
            return false
        }

        if (binding.fatET.text.toString().isEmpty()) {
            showError("Fett (g) i \"Näringsvärde per portion\" saknas", binding.fatET)
            return false
        }

        if (binding.carbsET.text.toString().isEmpty()) {
            showError("Kolhydrater (g) i \"Näringsvärde per portion\" saknas", binding.carbsET)
            return false
        }

        if (activeFiltersList.isEmpty()) {
            showError("\"Proteinkälla\" saknas")
            return false
        }

        if (binding.stepByStepET.text.toString().isEmpty()) {
            showError("\"Tillagning - steg för steg\" saknas", binding.stepByStepET)
            return false
        }

        if (viewModelImage.file == null) {
            showError("\"Bild\" saknas")
            return false
        }

        return true
    }

    private fun showError(message: String, view: View) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        view.requestFocus()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //Post recipe to databse
    private fun postToDatabase() {
        val name = binding.recipeNameET.text.toString()
        val description = binding.descriptionET.text.toString()
        val kcal = binding.kcalET.text.toString().toInt()
        val protein = binding.proteinET.text.toString().toInt()
        val fat = binding.fatET.text.toString().toInt()
        val carbs = binding.carbsET.text.toString().toInt()
        val stepByStep = binding.stepByStepET.text.toString()

        val recipe = Recipe(
            0,
            name,
            description,
            nrOfServings,
            ingredientModel,
            kcal,
            protein,
            fat,
            carbs,
            activeFiltersList,
            stepByStep,
            imageName
        )

        viewModelRecipe.addRecipe(recipe)
        Toast.makeText(this, "Recept tillagt!", Toast.LENGTH_LONG).show()
    }

    private fun updateIngredientModel(
        weightVolume: String, measurementUnit: String, ingredientName: String
    ) {
        ingredientModel.add(IngredientsModel(weightVolume, measurementUnit, ingredientName))
    }

    //Camera
    private fun takePicture() {

        //Store image to local storage
        val uri: Uri =
            FileProvider.getUriForFile(applicationContext, "$packageName.fileprovider", file)

        launcher.launch(uri)
    }

    //Update size if screen size changes due to change of orientation
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        viewModelImage.setSize(binding.postedImageIV.height, binding.postedImageIV.width)
    }

    //Function to generate unique image names to be stored in the database
    private fun generateImageName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val randomString = UUID.randomUUID().toString()
        return "img_$timeStamp-$randomString.jpg"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(INGREDIENT_KEY, ingredientModel)
        outState.putBooleanArray(FILTER_KEY, activeFiltersListBoolean)
    }

    companion object {
        private const val INGREDIENT_KEY = "AddRecipeActivity.Ingredient"
        private const val FILTER_KEY = "AddRecipeActivity.Filter"
    }

}