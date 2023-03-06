package se.umu.cs.oi19aws.makrokoll.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.R

class IngredientsRecyclerViewAdapter(var context: Context,
                                     var ingredientList:ArrayList<IngredientsModel>) :
    RecyclerView.Adapter<IngredientsRecyclerViewAdapter.Companion.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.item_ingredient, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val removeButton = holder.itemView
            .findViewById<AppCompatImageButton>(R.id.removeIngredientButton)
        removeButton.setOnClickListener {
            ingredientList.removeAt(position)
            //This is used instead of "notifyItemRemoved(position)" to get the updated interval
            notifyDataSetChanged()
        }
        val text = ingredientList[position]
            .getWeightVolume() + " " + ingredientList[position].getMeasurementUnit()
        holder.weightVolAndUnit.text = text
        holder.ingredient.text = ingredientList[position].getIngredientName()
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    companion object{
        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var weightVolAndUnit:TextView = itemView.findViewById(R.id.measurement)
            var ingredient:TextView = itemView.findViewById(R.id.ingredient)
        }
    }
}