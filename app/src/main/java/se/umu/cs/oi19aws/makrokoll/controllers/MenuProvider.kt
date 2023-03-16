package se.umu.cs.oi19aws.makrokoll.controllers

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

// Interface for providing consistent menu handling
interface MenuProvider {
    fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)
    fun onMenuItemSelected(menuItem: MenuItem): Boolean
}