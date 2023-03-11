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
import se.umu.cs.oi19aws.makrokoll.databinding.ActivityAddRecipeBinding
import se.umu.cs.oi19aws.makrokoll.models.IngredientsModel
import se.umu.cs.oi19aws.makrokoll.models.IngredientsRecyclerViewAdapter
import java.io.File
import kotlin.collections.ArrayList

class AddRecipeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var nrOfServingsSpinner: Spinner
    private lateinit var measurementSpinner: Spinner
    private var filterList = ArrayList<Button>()
    private lateinit var recyclerView:RecyclerView
    //TODO: Kolla om nrOfServings ska vara Int eller String, vilken som blir enklast
    private var nrOfServings = ""
    private var measurementUnit = ""
    private var ingredientModel = ArrayList<IngredientsModel>()

    private lateinit var file: File
    private lateinit var viewModel: ImageViewModel

    val launcher=registerForActivityResult(
        //Vi använder det färdiga kontraktet för att ta bilder
        ActivityResultContracts.TakePicture()) {
        if(it) {
            viewModel.file= File(filesDir, "mypic.jpg") //Uppdatera bilden
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNrOfServingsSpinner()
        initMeasurementSpinner()
        setAddIngredientListener()
        initFilterList()
        setPublishListener()

        viewModel.bitmap.observe(this) {
            binding.postedImageIV.setImageBitmap(it)
        }

        //Plats att spara bilden
        file=File(filesDir, "mypic.jpg")
        if(file.exists()&&viewModel.file==null) {
            viewModel.file=file
        }

        //Sätt en lyssnare för knappen
        binding.addImageButton.setOnClickListener { takePicture() }
    }


    private fun initNrOfServingsSpinner(){
        nrOfServingsSpinner = binding.nrOfServingsSpinner

        var adapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.numbers, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        nrOfServingsSpinner.adapter = adapter
        nrOfServingsSpinner.onItemSelectedListener = this
    }

    private fun initMeasurementSpinner(){
        measurementSpinner = binding.measurementUnitSpinner

        val adapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.measurements, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        measurementSpinner.adapter = adapter
        measurementSpinner.onItemSelectedListener = this
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent == binding.nrOfServingsSpinner) {
            nrOfServings = parent.getItemAtPosition(position).toString()
        }
        if (parent == binding.measurementUnitSpinner) {
            measurementUnit = parent.getItemAtPosition(position).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun setAddIngredientListener(){
        val addIngredientButton = binding.addIngredientButton

        addIngredientButton.setOnClickListener {
            if(binding.weightVolumeET.text.toString() != "") {
                if(binding.ingredientNameET.text.toString() != "") {
                    val weightVolume = binding.weightVolumeET.text.toString()
                    val ingredientName = binding.ingredientNameET.text.toString().lowercase()

                    recyclerView = binding.recyclerView
                    updateIngredientModel(weightVolume, measurementUnit, ingredientName)
                    val adapter = IngredientsRecyclerViewAdapter(this, ingredientModel)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    binding.weightVolumeET.text.clear()
                    binding.ingredientNameET.text.clear()
                    binding.weightVolumeET.requestFocus()
                } else Toast.makeText(this,"Ange namn på ingrediens", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Ange vikt/volym", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initFilterList(){
        filterList.add(binding.filterButtonFish)
        filterList.add(binding.filterButtonPork)
        filterList.add(binding.filterButtonBird)
        filterList.add(binding.filterButtonMilk)
        filterList.add(binding.filterButtonBeef)
        filterList.add(binding.filterButtonVeg)
        filterList.add(binding.filterButtonGame)
        filterList.add(binding.filterButtonEgg)
        filterList.add(binding.filterButtonOthers)

        for(btn in filterList){
            btn.setOnClickListener {
                //TODO: Spara undan tillstånd
                //TODO: Fixa så bara ett alternativ kan vara aktivt??
                //TODO: Alternativt: fixa ikon för flera val av protein
                btn.isActivated = !btn.isActivated
            }
        }
    }

    private fun setPublishListener(){
        val publishButton = binding.publishButton
        val activeFiltersList = ArrayList<String>()

        publishButton.setOnClickListener {
            for(btn in filterList){
                if(btn.isActivated) {
                    //Store only the filter phrase, ie. "Beef" or "Egg"
                    activeFiltersList.add(resources.getResourceEntryName(btn.id).substring(12))
                }
            }

            if(binding.recipeNameET.text.toString() != "") {
                if(activeFiltersList.isNotEmpty()) {
                    if(binding.stepByStepET.text.toString() != "") {
                        //Hämta nrOfServings också, lagrad i variabeln redan
                        val nameOfRecipe = binding.recipeNameET.text.toString()
                    } else {
                        Toast.makeText(this, "\"Tillagning - steg för steg\" saknas", Toast.LENGTH_LONG).show()
                        binding.stepByStepET.requestFocus()
                    }
                } else Toast.makeText(this, "\"Proteinkälla\" saknas", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "\"Namn på recept\" saknas", Toast.LENGTH_LONG).show()
                binding.recipeNameET.requestFocus()
            }
        }
    }

    private fun updateIngredientModel(weightVolume:String,
                                      measurementUnit:String,
                                      ingredientName:String) {
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
        viewModel.setSize(binding.postedImageIV.height,binding.postedImageIV.width)
    }
}