package com.tedmob.africell.features.bundles

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseActivity
import com.tedmob.africell.util.navigation.goBack


class BundleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bundle, false, false, 0)
        findNavController(R.id.nav_host_bundle).addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id != R.id.bundleVPFragment && destination.id!=R.id.bundleDetailsFragment) {
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
