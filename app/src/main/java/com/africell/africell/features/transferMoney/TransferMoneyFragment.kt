package com.africell.africell.features.transferMoney


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.africell.africell.R
import com.africell.africell.data.repository.domain.SessionRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_money_transfer.*
import javax.inject.Inject


class TransferMoneyFragment : BottomSheetDialogFragment() {

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    interface Callback {
        fun p2pTransfer()
        fun merchantTransfer()
    }


    var callback: Callback? = null
    fun setCallBack(callback: Callback) {
        this.callback = callback
    }

    @Inject
    lateinit var sessionRepository: SessionRepository

    companion object {
        fun newInstance(): TransferMoneyFragment {
            return TransferMoneyFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_money_transfer, LinearLayout(context), false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)

        p2pTransfer.setOnClickListener {
            callback?.p2pTransfer()
            dismiss()
        }

        cancel.setOnClickListener {
            dismiss()
        }

        merchantPayment.setOnClickListener {
            callback?.merchantTransfer()
            dismiss()
        }
    }


}