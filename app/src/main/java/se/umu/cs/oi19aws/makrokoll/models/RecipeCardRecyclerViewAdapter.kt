package se.umu.cs.oi19aws.makrokoll.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.RecyclerViewInterface

class RecipeCardRecyclerViewAdapter(var context: Context,
                                    var recipesList:ArrayList<RecipeCardModel>,
                                    var recyclerViewInterface: RecyclerViewInterface) :
    RecyclerView.Adapter<RecipeCardRecyclerViewAdapter.Companion.MyViewHolder>() {

    //private lateinit var recyclerViewInterface:RecyclerViewInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.component_nutrient_info, parent, false)
        return MyViewHolder(view, recyclerViewInterface)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val card = holder.itemView
        //    .findViewById<AppCompatImageButton>(R.id.top_container)
        //card.setOnClickListener {
            //TODO: Gå till receptsidan
        //    Log.d("TAG", "OnClick working")
            //notifyDataSetChanged()
        //}



        holder.recipe_name.text = recipesList[position].getName()
        holder.kcal_text.text = context.getString(R.string.kcal, recipesList[position].getKCAL())
        holder.pfc_text.text = context.getString(R.string.PFC_info,
            recipesList[position].getProtein(),
            recipesList[position].getFat(),
            recipesList[position].getCarbs())

        //ingredientList[position].getIcon() == "beef", "egg" etc, same as protein filter option.
        val iconName = "ic_${recipesList[position].getIcon()}"
        holder.icon.setImageResource(context.resources.getIdentifier(iconName,"drawable","se.umu.cs.oi19aws.makrokoll"))
        val imageName = "${recipesList[position].getImage()}"
        holder.image.setImageResource(context.resources.getIdentifier(imageName, "drawable", "se.umu.cs.oi19aws.makrokoll"))
        Log.d("TAG", "recipeName: ${holder.recipe_name.text}")
        holder.setOnClickListener()

    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    companion object {
        class MyViewHolder(itemView: View, private val recyclerViewInterface: RecyclerViewInterface) : RecyclerView.ViewHolder(itemView) {
            var recipe_name: TextView = itemView.findViewById(R.id.recipe_name)
            var kcal_text: TextView = itemView.findViewById(R.id.kcal_text)
            var pfc_text: TextView = itemView.findViewById(R.id.PFC_text)
            var icon: ImageView = itemView.findViewById(R.id.protein_icon)
            var image: ImageView = itemView.findViewById(R.id.recipe_image)

            fun setOnClickListener(){
                itemView.setOnClickListener{
                    if(adapterPosition != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(adapterPosition)
                    }
                }
            }
        }
    }
}