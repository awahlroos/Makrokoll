package se.umu.cs.oi19aws.makrokoll.models

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.controllers.RecyclerViewInterface
import java.io.File

// Class to handle ingredient recycler view adapter
class RecipeCardRecyclerViewAdapter(var context: Context,
                                    var recipesList:ArrayList<RecipeCardModel>,
                                    var recyclerViewInterface: RecyclerViewInterface
) :
    RecyclerView.Adapter<RecipeCardRecyclerViewAdapter.Companion.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.component_nutrient_info, parent, false)
        return MyViewHolder(view, recyclerViewInterface)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.recipeName.text = recipesList[position].getName()
        holder.kcalText.text = context.getString(R.string.nr_kcal, recipesList[position].getKCAL())
        holder.pfcText.text = context.getString(R.string.PFC_info,
            recipesList[position].getProtein(),
            recipesList[position].getFat(),
            recipesList[position].getCarbs())

        //Get icon name ending, like "beef" or "egg"
        val iconName = "ic_${recipesList[position].getIcon()}"
        holder.icon.setImageResource(context.resources.getIdentifier(iconName,"drawable","se.umu.cs.oi19aws.makrokoll"))
        val imageName = recipesList[position].getImage()

        holder.image.setImageBitmap(BitmapFactory.decodeFile(File(context.filesDir, imageName).absolutePath))
        holder.setOnClickListener()
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    companion object {
        class MyViewHolder(itemView: View, private val recyclerViewInterface: RecyclerViewInterface) : RecyclerView.ViewHolder(itemView) {
            var recipeName: TextView = itemView.findViewById(R.id.recipe_name)
            var kcalText: TextView = itemView.findViewById(R.id.kcal_text)
            var pfcText: TextView = itemView.findViewById(R.id.PFC_text)
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