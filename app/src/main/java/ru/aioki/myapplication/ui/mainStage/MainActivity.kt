package ru.aioki.myapplication.ui.mainStage

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseActivity
import ru.aioki.myapplication.databinding.ActivityMainBinding
import ru.aioki.myapplication.utils.disableItemsReselected

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun getFragmentContainer(): Int = R.id.nav_host_fragment


    override fun setUpView() {
        super.setUpView()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.decorView.windowInsetsController?.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS)
//        } else {
//
//        }
        setupNavigation()

    }


    private fun setupNavigation() {
        binding.navView.itemIconTintList = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        binding.navView.disableItemsReselected()
    }

}