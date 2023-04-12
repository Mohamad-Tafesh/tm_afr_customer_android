package com.tedmob.afrimoney.features.authentication

import android.os.Bundle
import androidx.activity.addCallback
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.databinding.ActivityEnterPinBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterPinActivity : BaseVBActivity<ActivityEnterPinBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(ActivityEnterPinBinding::inflate)

        onBackPressedDispatcher.addCallback(this, enabled = true) {
            finishAffinity()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        finishAffinity()
        return true
    }
}