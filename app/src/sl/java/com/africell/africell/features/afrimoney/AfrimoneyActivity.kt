package com.africell.africell.features.afrimoney

import android.os.Bundle
import androidx.activity.addCallback
import com.africell.africell.app.viewbinding.BaseVBActivity
import com.africell.africell.databinding.ActivityAfrimoneyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AfrimoneyActivity : BaseVBActivity<ActivityAfrimoneyBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(ActivityAfrimoneyBinding::inflate)

        onBackPressedDispatcher.addCallback(this, enabled = true) {
            finishAffinity()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        finishAffinity()
        return true
    }
}