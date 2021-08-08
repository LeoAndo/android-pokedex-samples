package com.example.pokedexkotlinsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pokedexkotlinsample.databinding.ActivityMainBinding
import com.example.pokedexkotlinsample.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val binding by viewBindings(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initNavController()
        observeDestination()
    }

    private fun observeDestination() {
        navController.addOnDestinationChangedListener { n, destination, _ ->
            updateContent(destination.id)
        }
    }

    private fun initNavController() {
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = host.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.pokemonListFragment),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        this.setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun updateContent(destinationId: Int) {
        when (destinationId) {
            R.id.pokemonListFragment -> {
                supportActionBar?.hide()
            }
            R.id.pokemonStatsFragment -> {
                supportActionBar?.show()
            }
        }
    }
}