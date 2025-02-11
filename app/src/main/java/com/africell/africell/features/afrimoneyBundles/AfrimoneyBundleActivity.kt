package com.africell.africell.features.afrimoneyBundles

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.africell.africell.R
import com.africell.africell.app.BaseActivity
import com.africell.africell.util.navigation.goBack
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AfrimoneyBundleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afrimoney_bundle, false, false, 0)
        findNavController(R.id.nav_host_bundle).addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id != R.id.afrimoneyBundleVPFragment && destination.id!=R.id.afrimoneyBundleDetailsFragment) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window?.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
                }
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_bundle).goBack(this)
    override fun onDestroy() {
        super.onDestroy()

    }

}
