package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import se.umu.cs.oi19aws.makrokoll.models.IngredientsRecyclerViewAdapter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddRecipeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var nrOfServingsSpinner: Spinner
    private lateinit var measurementSpinner: Spinner
    private lateinit var activeFiltersList: ArrayList<String>
    private var filterList = ArrayList<Button>()
    private lateinit var recyclerView: RecyclerView
    private var nrOfServings = 0
    private lateinit var measurementUnit: String
    private var ingredientModel = ArrayList<IngredientsModel>()
    private lateinit var viewModelRecipe: RecipeViewModel

    private lateinit var file: File
    private lateinit var viewModelImage: ImageViewModel
    private val imageName = generateImageName()

    val launcher = registerForActivityResult(

        //Vi använder det färdiga kontraktet för att ta bilder
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            viewModelImage.file = File(filesDir, imageName) //Uppdatera bilden
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelImage = ViewModelProvider(this)[ImageViewModel::class.java]
        viewModelRecipe = ViewModelProvider(this)[RecipeViewModel::class.java]
        //viewModelImage.setContext(applicationContext)
        supportActionBar?.title = "Publicera nytt recept"

        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNrOfServingsSpinner()
        initMeasurementSpinner()
        setAddIngredientListener()
        initFilterList()
        setPublishListener()

        viewModelImage.bitmap.observe(this) {
            binding.postedImageIV.setImageBitmap(it)
        }

        //Plats att spara bilden
        file = File(filesDir, imageName)
        if (file.exists() && viewModelImage.file == null) {
            viewModelImage.file = file
        }

        //Sätt en lyssnare för knappen
        binding.addImageButton.setOnClickListener { takePicture() }
    }


    private fun initNrOfServingsSpinner() {
        nrOfServingsSpinner = binding.nrOfServingsSpinner

        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
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


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent == binding.nrOfServingsSpinner) {
            nrOfServings = parent.getItemAtPosition(position).toString().toInt()
        }
        if (parent == binding.measurementUnitSpinner) {
            measurementUnit = parent.getItemAtPosition(position).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun setAddIngredientListener() {
        val addIngredientButton = binding.addIngredientButton

        addIngredientButton.setOnClickListener {
            if (binding.weightVolumeET.text.toString() != "") {
                if (binding.ingredientNameET.text.toString() != "") {
                    val weightVolume = binding.weightVolumeET.text.toString()
                    val ingredientName = binding.ingredientNameET.text.toString().lowercase()

                    recyclerView = binding.recyclerView
                    updateIngredientModel(weightVolume, measurementUnit, ingredientName)
                    val adapter = IngredientsRecyclerViewAdapter(this, ingredientModel, true)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    binding.weightVolumeET.text.clear()
                    binding.ingredientNameET.text.clear()
                    binding.weightVolumeET.requestFocus()
                } else Toast.makeText(this, "Ange namn på ingrediens", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Ange vikt/volym", Toast.LENGTH_LONG).show()
            }
        }
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
            btn.setOnClickListener {
                //TODO: Spara undan tillstånd
                btn.isActivated = !btn.isActivated
            }
        }
    }

    private fun setPublishListener() {
        val publishButton = binding.publishButton
        activeFiltersList = ArrayList()

        publishButton.setOnClickListener {
            for (btn in filterList) {
                if (btn.isActivated) {
                    //Store only the filter phrase, ie. "Beef" or "Egg"
                    activeFiltersList.add(
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

        if(viewModelImage.file == null){
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

    private fun postToDatabase() {
        val name = binding.recipeNameET.text.toString()
        val description = binding.descriptionET.text.toString()
        //nrOfServings
        //ingredientModel
        val kcal = binding.kcalET.text.toString().toInt()
        val protein = binding.proteinET.text.toString().toInt()
        val fat = binding.fatET.text.toString().toInt()
        val carbs = binding.carbsET.text.toString().toInt()
        //activeFiltersList
        val stepByStep = binding.stepByStepET.text.toString()
        //val image = viewModelImage.file.toString()

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
        /* Bestäm vart filen ska sparas
         * Observera att Kamera-appen måste kunna skriva till platsen
         * där filen finns därav används en FileProvider
         */
        val uri: Uri

        //Spara filen i vår local
        // storage med en content-url. Det låter de oss spara filen var som helst utan att behöva
        // bry os om att kameraappen har rättighet at skriva dit. Dessutom har vi
        // om vi sparar filen på ett ställe vi har koll på kontroll över att ingen
        // annan app sabbar något
        uri = FileProvider.getUriForFile(applicationContext, "$packageName.fileprovider", file)

        //Starta aktiviteten
        launcher.launch(uri)
    }

    /*
     * Se till så att vi uppdaterar om storleken på vyn skulle ändras (tex pga rotation).
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        viewModelImage.setSize(binding.postedImageIV.height, binding.postedImageIV.width)
    }

    private fun generateImageName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val randomString = UUID.randomUUID().toString()
        return "img_$timeStamp-$randomString.jpg"
    }

}