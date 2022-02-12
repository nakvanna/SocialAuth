package com.example.socialauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.socialauth.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)

        /*
         * Back button without menu icon
         */
        // NavigationUI.setupActionBarWithNavController(this, navController)

        /*
         * Back button with menu icon
         */
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        /*
         * AppBarConfiguration for Drawer menu
         */
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        /*
         * prevent nav gesture if not on start destination
         */
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            if (nd.id == nc.graph.startDestinationId) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    /*
     * Back button click to previous fragment
     */
    override fun onSupportNavigateUp(): Boolean {
        /*
         * Back button only   
         */
        //return navController.navigateUp() || super.onSupportNavigateUp()

        /*
         * Menu icon or Back button
         */
        return NavigationUI.navigateUp(
            this.findNavController(R.id.myNavHostFragment),
            appBarConfiguration
        )
    }
}