package com.maslan.capstoneupschool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

        private val binding by viewBinding (ActivityMainBinding::inflate)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)

            with(binding){

                val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                val navController = navHostFragment.navController
                NavigationUI.setupWithNavController(bottomNavigationView, navController)

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.homeFragment -> bottomNavigationView.visibility = View.VISIBLE
                        R.id.searchFragment2-> bottomNavigationView.visibility=View.VISIBLE
                        R.id.favoriteFragment->bottomNavigationView.visibility=View.VISIBLE
                        R.id.cartFragment2->bottomNavigationView.visibility=View.VISIBLE
                        else -> bottomNavigationView.visibility = View.GONE
                    }
                }

                }
            }

        }