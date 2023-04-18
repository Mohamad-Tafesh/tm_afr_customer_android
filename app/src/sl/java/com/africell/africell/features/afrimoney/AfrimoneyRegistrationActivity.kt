package com.africell.africell.features.afrimoney

import android.os.Bundle
import androidx.navigation.findNavController
import com.africell.africell.databinding.ActivityAfrimoneyRegistrationBinding
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.databinding.ToolbarDefaultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AfrimoneyRegistrationActivity : BaseVBActivity<ActivityAfrimoneyRegistrationBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(ActivityAfrimoneyRegistrationBinding::inflate, false, ToolbarDefaultBinding::inflate)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
}
