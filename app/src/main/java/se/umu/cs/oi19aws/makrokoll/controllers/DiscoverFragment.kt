package se.umu.cs.oi19aws.makrokoll.controllers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.oi19aws.makrokoll.RecyclerViewInterface
import se.umu.cs.oi19aws.makrokoll.models.SpacesItemDecorator
import se.umu.cs.oi19aws.makrokoll.databinding.FragmentDiscoverBinding
import se.umu.cs.oi19aws.makrokoll.models.RecipeCardRecyclerViewAdapter
import se.umu.cs.oi19aws.makrokoll.models.RecipeCardModel


class DiscoverFragment : Fragment(), RecyclerViewInterface {

    private lateinit var binding:FragmentDiscoverBinding
    private var recipeModel = ArrayList<RecipeCardModel>()
    private lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //TODO: REMOVE THIS
        for(i in 1..20){
            updateRecipeModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpRecyclerView(){
        recyclerView = binding.recyclerView
        val adapter = this.activity?.let { RecipeCardRecyclerViewAdapter(it, recipeModel, this) }
        recyclerView.adapter = adapter
        //recyclerView.layoutManager = FlexboxLayoutManager(this.activity)
        recyclerView.layoutManager = GridLayoutManager(this.activity,2)
        recyclerView.addItemDecoration(SpacesItemDecorator(16, 450))


    }

    override fun onItemClick(position: Int) {
        Log.d("TAG", "Clicked: $position")
    }

    //TODO: REMOVE THIS and call it when user adds new recipe. Kom ihåg att notifiera lyssnare
    private fun updateRecipeModel() {
        recipeModel.add(RecipeCardModel("Currygryta med kyckling och ångkokt broccoli",409, 32,18,59,"bird" , "img_currygryta"))

    }
}