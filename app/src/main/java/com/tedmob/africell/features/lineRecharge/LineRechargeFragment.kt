package com.tedmob.africell.features.lineRecharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.RechargeCardDTO
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_line_recharge.*
import javax.inject.Inject


class LineRechargeFragment : BaseFragment(), Liv.Action {
    private var liv: Liv? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<LineRechargeViewModel> { viewModelFactory }
    val adapter by lazy {
        LineRechargeAdapter(mutableListOf(), object : LineRechargeAdapter.Callback {
            override fun onItemClickListener(item: RechargeCardDTO) {
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_line_recharge, R.layout.toolbar_default, true)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.line_recharge)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liv = initLiv()
        liv?.start()
        viewModel.getRechargeCards()
        sendBtn.setOnClickListener { liv?.submitWhenValid() }
        bindData()
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .add(rechargeCardLayout, notEmptyRule)
            .submitAction(this)
            .build()

    }

    private fun bindData() {
        observeResourceInline(viewModel.cardsData) {
            recyclerView.adapter = adapter
            adapter.setItems(it)
        }

        observeResource(viewModel.rechargeVoucherData) {
            showMessageDialog(it.resultText.orEmpty(), getString(R.string.close)) {
                liv?.dispose()
                rechargeCardLayout.setText("")
                mobileNumberLayout.setText("")
                liv?.start()
            }
        }
    }


    override fun performAction() {
        val formatted = PhoneNumberHelper.getFormattedIfValid("", mobileNumberLayout.getText())?.replace("+", "")
        formatted?.let {
            viewModel.rechargeVoucher(it, rechargeCardLayout.getText())
        } ?: showMessage(getString(R.string.phone_number_not_valid))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}

