package se.umu.cs.oi19aws.makrokoll.controllers

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.databinding.ActivityAddRecipeBinding
import se.umu.cs.oi19aws.makrokoll.models.IngredientsModel
import se.umu.cs.oi19aws.makrokoll.models.IngredientsRecyclerViewAdapter
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
    private var ingredientList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNrOfServingsSpinner()
        initMeasurementSpinner()
        setAddIngredientListener()
        initFilterList()
        setPublishListener()
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

        var adapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.measurements, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        measurementSpinner.adapter = adapter
        measurementSpinner.onItemSelectedListener = this
    }

    //TODO: Kanske ta bort dessa funktioner
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
            val weightVolume = binding.weightVolumeET.text.toString()
            val ingredientName = binding.ingredientNameET.text.toString().lowercase()

            recyclerView = binding.recyclerView
            updateIngredientModel(weightVolume, measurementUnit, ingredientName)
            val adapter = IngredientsRecyclerViewAdapter(this, ingredientModel)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            binding.

            binding.weightVolumeET.text.clear()
            binding.ingredientNameET.text.clear()
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
            val nameOfRecipe = binding.recipeNameTV.text.toString()
            //nrOfServings from spinner
            for(btn in filterList){
                if(btn.isActivated) {
                    //Store only the filter phrase, ie. "Beef" or "Egg"
                    activeFiltersList.add(resources.getResourceEntryName(btn.id).substring(12))
                }
            }
        }
    }

    private fun updateIngredientModel(weightVolume:String,
                                      measurementUnit:String,
                                      ingredientName:String) {
        ingredientModel.add(IngredientsModel(weightVolume, measurementUnit, ingredientName))
    }
}