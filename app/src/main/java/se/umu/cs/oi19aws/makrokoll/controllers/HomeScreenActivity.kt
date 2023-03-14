package se.umu.cs.oi19aws.makrokoll.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel

//import se.umu.cs.oi19aws.makrokoll.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        //binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        //binding.bottomNavigationView
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = supportFragmentManager.findFragmentById(R.id.discover_fragment_activity)
            ?.findNavController()

        navController?.let { bottomNavigationView.setupWithNavController(it) }
        //val bottomNavigationView = binding.bottomNavigationView
        //val navController = findNavController(R.id.discover_fragment_activity)

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.discover, R.id.saved, R.id.profile))


        if (navController != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
        }

        //bottomNavigationView.setupWithNavController(navController)

    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actionbar, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView

        //Set listener to handle searches from the searchView in the actionbar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //TODO: Pröva ta bort denna
            override fun onQueryTextSubmit(query: String): Boolean {
                search.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //val fragment = supportFragmentManager.findFragmentById(R.id.asdasd) as DiscoverFragment
                //fragment.updateRecyclerView(newText)
                //TODO: Sätt recyclerView till recepten man sökt, kom ihåg att uppdatera lyssnare med observe
                val fragment = supportFragmentManager.findFragmentById(R.id.discover) as DiscoverFragment
                fragment.updateRecyclerView(newText)

                return false
            }
        })
        return true
    }

 */
}