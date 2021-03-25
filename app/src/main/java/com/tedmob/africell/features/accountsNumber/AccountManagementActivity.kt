package com.tedmob.africell.features.accountsNumber

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseActivity
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.accountInfo.AccountViewModel
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_account_numbers.*
import javax.inject.Inject


class AccountManagementActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
@Inject lateinit var sessionRepository: SessionRepository
    private val viewModel by provideViewModel<AccountViewModel> { viewModelFactory }
    val adapter by lazy {
        AccountAdapter(mutableListOf(),sessionRepository.msisdn,true, object : AccountAdapter.CallBack {
            override fun onItemClick(item: SubAccount) {
            }

            override fun deleteAccount(item: SubAccount) {
                item.account?.let { viewModel.deleteSubAccount(it) }
            }


        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management, true, true, R.layout.toolbar_default)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.account_management)
        setupRecyclerView()
        bindData()
    }

    private fun bindData() {
        viewModel.getSubAccounts()
        observeResourceInline(viewModel.subAccountData, {accounts->
            adapter.setItems(accounts.toMutableList())
        })

        observeResource(viewModel.deleteAccountData){accounts->
            adapter.setItems(accounts.toMutableList())
        }

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(this, R.drawable.separator)
        drawable?.let {
            dividerItemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)

    }


}