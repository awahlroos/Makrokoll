package se.umu.cs.oi19aws.makrokoll.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import se.umu.cs.oi19aws.makrokoll.R
//import se.umu.cs.oi19aws.makrokoll.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        //binding = ActivityHomeScreenBinding.inflate(layoutInflater)
    }

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
            override fun onQueryTextSubmit(query: String): Boolean {
                //TODO: Implementera med databasen
                //model.search(query)
                //setListData()
                search.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }


}