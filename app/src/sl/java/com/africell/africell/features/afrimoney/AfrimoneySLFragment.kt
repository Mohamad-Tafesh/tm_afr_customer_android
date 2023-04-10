package com.africell.africell.features.afrimoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentAfriMoneySlBinding
import com.africell.africell.databinding.ToolbarHomeBinding
import javax.inject.Inject

class AfrimoneySLFragment : BaseVBFragment<FragmentAfriMoneySlBinding>() {

    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentAfriMoneySlBinding::inflate, true, ToolbarHomeBinding::inflate)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //...
    }
}