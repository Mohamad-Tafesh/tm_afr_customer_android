package com.tedmob.afrimoney.features.launch

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.databinding.ActivityRootBinding
import com.tedmob.afrimoney.databinding.ToolbarDefaultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : BaseVBActivity<ActivityRootBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent(ActivityRootBinding::inflate, false, ToolbarDefaultBinding::inflate)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
}
