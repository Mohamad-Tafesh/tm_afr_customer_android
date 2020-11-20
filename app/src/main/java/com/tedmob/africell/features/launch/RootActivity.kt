package com.tedmob.africell.features.launch

import android.os.Bundle
import androidx.navigation.findNavController
import com.tedmob.africell.app.BaseActivity
import com.tedmob.africell.R

class RootActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root, false, true, R.layout.toolbar_default)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp()
}
