package se.umu.cs.oi19aws.makrokoll.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.R

// Class to handle ingredient recycler view adapter
class IngredientsRecyclerViewAdapter(
    var context: Context,
    var ingredientList: ArrayList<IngredientsModel>,
    private val deletable: Boolean
) : RecyclerView.Adapter<IngredientsRecyclerViewAdapter.Companion.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        var view: View = inflater.inflate(R.layout.item_ingredient_no_delete, parent, false)
        if (deletable) {
            view = inflater.inflate(R.layout.item_ingredient, parent, false)
        }

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(deletable) {
            val removeButton =
                holder.itemView.findViewById<AppCompatImageButton>(R.id.removeIngredientButton)
            removeButton.setOnClickListener {
                ingredientList.removeAt(position)
                notifyDataSetChanged()
            }
        }
        val text =
            ingredientList[position].getWeightVolume() + " " + ingredientList[position].getMeasurementUnit()
        holder.weightVolAndUnit.text = text
        holder.ingredient.text = ingredientList[position].getIngredientName()
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    companion object {
        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var weightVolAndUnit: TextView = itemView.findViewById(R.id.measurement)
            var ingredient: TextView = itemView.findViewById(R.id.ingredient)
        }
    }
}