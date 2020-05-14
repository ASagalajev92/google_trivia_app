package com.example.android.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    CREATING VAR FOR DRAWER LAYOUT
    private lateinit var drawerLayout: DrawerLayout
//    CREATING PROJECT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//    TAKING CONTENT FROM DRAWER LAYOUT USING BINDING
        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)
//    ADDING DRAWERLAYOUT INTO NAVIFATION UI
        NavigationUI.setupActionBarWithNavController(this, navController,drawerLayout)
//    PREVENT DrawerLayout TO WORK WITH SWIPE IN ANOTHER FRAGMENTS OR ACTIVITY'S
    navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
        if (nd.id == nc.graph.startDestination) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
//    PREVENT END
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

//    Creating back button in actionBar with navigation
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }
}

