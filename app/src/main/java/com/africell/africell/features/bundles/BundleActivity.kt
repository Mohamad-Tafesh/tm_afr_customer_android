package com.africell.africell.features.bundles

import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.africell.africell.R
import com.africell.africell.app.BaseActivity
import com.africell.africell.util.navigation.goBack
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BundleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bundle, false, wrapToolbar = false, toolbarLayoutId = 0)
        findNavController(R.id.nav_host_bundle).addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.bundleVPFragment && destination.id != R.id.bundleDetailsFragment) {
                window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window?.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_bundle).goBack(this)
    override fun onDestroy() {
        super.onDestroy()

    }

}
