package com.tedmob.afrimoney.features.newhome

import android.os.Bundle
import androidx.navigation.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.databinding.ActivityAfrimoneyRegistrationBinding
import com.tedmob.afrimoney.databinding.ToolbarDefaultBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AfrimoneyRegistrationActivity : BaseVBActivity<ActivityAfrimoneyRegistrationBinding>() {

    @Inject
    lateinit var session: SessionRepository


    private val number: String? by lazy { intent.getStringExtra("number") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(ActivityAfrimoneyRegistrationBinding::inflate, false)

        session.msisdn = number.orEmpty()

    }


    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
}
