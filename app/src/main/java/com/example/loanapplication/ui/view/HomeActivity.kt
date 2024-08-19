package com.example.loanapplication.ui.view

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.loanapplication.R
import com.example.loanapplication.databinding.ActivityHomeBinding
import com.example.loanapplication.util.InternetConnectivityCheck
import com.example.loanapplication.util.diAppComponents
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var networkConnectivityLiveData: InternetConnectivityCheck


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        networkConnectivityLiveData = diAppComponents.getInternetConnectivityCheck()
        observeNetworkConnectivity()
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.inflateMenu(R.menu.top_app_bar)
        binding.topAppBar.setNavigationIcon(R.drawable.ic_profile);
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.applyLoanFragment -> {
                    binding.topAppBar.navigationIcon =
                        ContextCompat.getDrawable(this, R.drawable.ic_profile)
                }

                R.id.loanHistoryFragment -> {
                    binding.topAppBar.navigationIcon = ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_back
                    )
                }

                R.id.profileFragment -> {
                    binding.topAppBar.navigationIcon = ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_back
                    )
                }

                else -> {
                    binding.topAppBar.navigationIcon = null
                }
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            val currentDestination = navController.currentDestination?.id
            when (currentDestination) {
                R.id.applyLoanFragment -> {
                    navController.navigate(R.id.action_applyLoanFragment_to_profileFragment)
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.action_profileFragment_to_applyLoanFragment)
                }

                R.id.loanHistoryFragment -> {
                    navController.navigate(R.id.action_loanHistoryFragment_to_applyLoanFragment)
                }

                else -> {

                }
            }
        }


        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            val currentDestination = navController.currentDestination?.id
            when (menuItem.itemId) {
                R.id.loanHistoryMenu -> {
                    if (currentDestination != R.id.loanHistoryFragment) {
                        navController.navigate(R.id.action_applyLoanFragment_to_loanHistoryFragment)
                    }
                    true
                }

                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(
        menu:
        Menu?
    ): Boolean {
        val currentDestination =
            findNavController(R.id.nav_host_fragment_home).currentDestination?.id
        menu?.findItem(R.id.loanHistoryMenu)?.isVisible = currentDestination == R.id.applyLoanFragment
        return super.onPrepareOptionsMenu(menu)
    }

    private fun observeNetworkConnectivity() {
        networkConnectivityLiveData.observe(this) { isConnected ->
            if (isConnected) {
                showSnackbar("Connected to the internet")
            } else {
                showSnackbar("Internet connection lost")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}