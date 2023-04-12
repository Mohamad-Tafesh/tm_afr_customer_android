package com.tedmob.afrimoney.features.help

import android.os.Bundle
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.databinding.ActivityWalkthroughBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalkthroughActivity : BaseVBActivity<ActivityWalkthroughBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(ActivityWalkthroughBinding::inflate, false)

        //...
    }
}