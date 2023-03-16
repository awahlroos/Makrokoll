package se.umu.cs.oi19aws.makrokoll.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import se.umu.cs.oi19aws.makrokoll.R
import se.umu.cs.oi19aws.makrokoll.data.RecipeViewModel

// Activity class to start the home screen
class HomeScreenActivity : AppCompatActivity() {

    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        //Setup for bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = supportFragmentManager.findFragmentById(R.id.discover_fragment_activity)
            ?.findNavController()

        navController?.let { bottomNavigationView.setupWithNavController(it) }

        //Setup for app bar
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.discover, R.id.saved, R.id.profile))

        if (navController != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
        }

    }
}