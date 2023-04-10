package com.africell.africell.features.transferMoney


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentMoneyTransferBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TransferMoneyFragment : BottomSheetDialogFragment() {

    private var viewBinding: FragmentMoneyTransferBinding? = null

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
        viewBinding = FragmentMoneyTransferBinding.inflate(LayoutInflater.from(context), LinearLayout(context), false)
        return viewBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)

        viewBinding?.run {
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


    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}