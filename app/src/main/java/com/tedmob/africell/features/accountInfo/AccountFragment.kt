package com.tedmob.africell.features.accountInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.features.home.BalanceAdapter
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.observeResourceWithoutProgress
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_account_info.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class AccountFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_account_info, R.layout.toolbar_home, true)
    }

    val balanceAdapter by lazy {
        BalanceAdapter(mutableListOf())
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by provideViewModel<AccountViewModel> { viewModelFactory }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        myProfile.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_editProfileFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //setupRecyclerView()
        bindData()

    }

    private fun bindData() {
        viewModel.getSubAccounts()
        observeResourceWithoutProgress(viewModel.subAccountData) {
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.textview_spinner, it)
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            accountSpinner.adapter = arrayAdapter
            accountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.getAccountInfo(it[position].account.orEmpty())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            accountSpinner.setSelection(0)

        }

        observeResourceInline(viewModel.accountInfoData) {

        }

    }
}