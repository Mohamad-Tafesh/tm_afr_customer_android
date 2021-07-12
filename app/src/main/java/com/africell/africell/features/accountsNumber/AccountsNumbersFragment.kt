package com.africell.africell.features.accountsNumber


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.africell.africell.R
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.data.repository.domain.SessionRepository
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_account_numbers.*
import javax.inject.Inject


class AccountsNumbersFragment : BottomSheetDialogFragment() {
    val accounts by lazy {
        arguments?.getParcelableArrayList<SubAccount>(ACCOUNTS) ?: throw IllegalArgumentException("Missing Accounts")
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    interface Callback {
        fun addNewAccount()
        fun manageAccount()
        fun setDefault(subAccount: SubAccount)
    }

    val adapter by lazy {
        AccountAdapter(mutableListOf(), null,false, object : AccountAdapter.CallBack {
            override fun onItemClick(item: SubAccount) {
                this@AccountsNumbersFragment.dismiss()
                sessionRepository.selectedMsisdn=item.account.orEmpty()
                callback?.setDefault(item)
            }

            override fun deleteAccount(item: SubAccount) {

            }

        })
    }
    var callback: Callback? = null
    fun setCallBack(callback: Callback) {
        this.callback = callback
    }

    @Inject
    lateinit var sessionRepository: SessionRepository

    companion object {
        const val ACCOUNTS = "accounts"
        fun newInstance(subAccounts: ArrayList<SubAccount>): AccountsNumbersFragment {
            return AccountsNumbersFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ACCOUNTS, subAccounts)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_account_numbers, LinearLayout(context), false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)

        addNewAccount.setOnClickListener {
            callback?.addNewAccount()
            dismiss()
        }

        accountManagement.setOnClickListener {
            callback?.manageAccount()
            dismiss()
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
        drawable?.let {
            dividerItemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter.setItems(accounts.toMutableList())
    }

}