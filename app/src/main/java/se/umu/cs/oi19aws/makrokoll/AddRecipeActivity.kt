package se.umu.cs.oi19aws.makrokoll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class AddRecipeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var nrOfServingsSpinner: Spinner
    private lateinit var measurementSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        initNrOfServingsSpinner()
        initMeasurementSpinner()
    }

    private fun initNrOfServingsSpinner(){
        nrOfServingsSpinner = findViewById(R.id.nr_of_servings_spinner)

        var adapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.numbers, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        nrOfServingsSpinner.adapter = adapter
        nrOfServingsSpinner.onItemSelectedListener = this
    }

    private fun initMeasurementSpinner(){
        measurementSpinner = findViewById(R.id.measurement_spinner)

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
}