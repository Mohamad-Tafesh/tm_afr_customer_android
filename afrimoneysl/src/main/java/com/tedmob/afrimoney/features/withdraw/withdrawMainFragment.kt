package com.tedmob.afrimoney.features.withdraw


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R

import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class withdrawMainFragment : BaseVBFragment<FragmentWithdrawMainBinding>() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentWithdrawMainBinding::inflate)


    }


    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            wCode.setOnClickListener() {
                findNavController().navigate(R.id.action_withdrawMain_to_agentcode)
            }
            wNumber.setOnClickListener() {
                findNavController().navigate(R.id.action_withdrawMain_to_agentPhoneNumberFragment)
            }
        }


    }

}