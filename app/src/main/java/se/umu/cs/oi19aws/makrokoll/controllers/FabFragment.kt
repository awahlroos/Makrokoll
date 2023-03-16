package se.umu.cs.oi19aws.makrokoll.controllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import se.umu.cs.oi19aws.makrokoll.R

// Fragment class for managing the FAB to add a new recipe
class FabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for fragment
        val view = inflater.inflate(R.layout.fragment_fab, container, false)
        val fab = view.findViewById<ImageButton>(R.id.FloatingActionButton)
        fab.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_fabFragment_to_addRecipeActivity)
        }
        return view
    }
}