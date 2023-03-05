package se.umu.cs.oi19aws.makrokoll

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.oi19aws.makrokoll.databinding.ActivityAddRecipeBinding
import kotlin.collections.ArrayList

class AddRecipeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var nrOfServingsSpinner: Spinner
    private lateinit var measurementSpinner: Spinner
    private var filterList = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNrOfServingsSpinner()
        initMeasurementSpinner()
        initFilterList()
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
        measurementSpinner = binding.measurementSpinner

        var adapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.measurements, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        measurementSpinner.adapter = adapter
        measurementSpinner.onItemSelectedListener = this
    }

    //TODO: Kanske ta bort dessa funktioner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var text = parent?.getItemAtPosition(position).toString()
        if (parent != null) {
            Toast.makeText(parent.context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun initFilterList(){
        filterList.add(binding.filterButtonFisk)
        filterList.add(binding.filterButtonFlask)
        filterList.add(binding.filterButtonFagel)
        filterList.add(binding.filterButtonMjolk)
        filterList.add(binding.filterButtonNot)
        filterList.add(binding.filterButtonVeg)
        filterList.add(binding.filterButtonVilt)
        filterList.add(binding.filterButtonAgg)
        filterList.add(binding.filterButtonOvrigt)

        for(btn in filterList){
            btn.setOnClickListener(){
                btn.isActivated = !btn.isActivated
            }
        }
    }
}