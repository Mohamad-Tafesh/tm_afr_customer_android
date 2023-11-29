package com.tedmob.afrimoney.features.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.text.scale
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.DialogLogoutBinding
import com.tedmob.afrimoney.databinding.FragmentAccountBinding
import com.tedmob.afrimoney.databinding.ItemAccountOptionBinding
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.observeResourceProgress
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter
import com.tedmob.afrimoney.util.navigation.runIfFrom
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : BaseVBFragment<FragmentAccountBinding>() {

    private enum class Option {
        LastTransactions,
        ChangePIN,
        MyProfile,
    }


    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<AccountViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentAccountBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
        bindLoggedOut()

        viewModel.getUserInfo()
    }

    private fun bindData() {
        observeResourceInline(viewModel.info) {
            withVBAvailable {
                setupUserInfo(it)
                setupOptions(Option.values().toList())
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_account, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logoutAction -> {
                showLogoutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog(onDismiss: (() -> Unit)? = null) {
        this.let {
            val viewBinding = DialogLogoutBinding.inflate(
                it.layoutInflater,
                FrameLayout(requireContext()),
                false
            )

            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertBottom)
                .setView(viewBinding.root)
                .setOnDismissListener { onDismiss?.invoke() }
                .show()


            viewBinding.run {
                closeButton.setOnClickListener {
                    dialog.dismiss()
                    viewModel.logout()
                }
            }

            val window = dialog.window
            val wlp = window?.attributes
            wlp?.gravity = Gravity.BOTTOM;
            wlp?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window?.setAttributes(wlp)
        }
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun FragmentAccountBinding.setupUserInfo(info: UserAccountInfo) {
/*        userImage.load(info.userImage) {
            scale(Scale.FILL)
            lifecycle(viewLifecycleOwner)
        }*/





        balanceText.text = buildSpannedString {
            scale(1.6f) {
                color(ContextCompat.getColor(requireContext(), R.color.violetMain)) {


                    val newBalance = info.balance
                    if ((newBalance.split("."))[1] == "00") {
                        val bal = newBalance.toDoubleOrNull()?.toInt()
                        append(bal.toString() + "NLe")
                    } else append(newBalance + "NLe")

                }
                bold {

                }

            }

            append("\n" + getString(R.string.my_balance))
        }

        nameText.text = info.userName

        //addressText.text = info.userAddress
    }

    private fun FragmentAccountBinding.setupOptions(options: List<Option>) {
        optionsRV.adapter = adapter(options) {
            viewBinding(ItemAccountOptionBinding::inflate)
            onBindItemToViewBinding<ItemAccountOptionBinding> {
                when (it) {
                    Option.LastTransactions -> {
                        optionImage.setImageResource(R.drawable.account_last_5__transactions)
                        optionText.setText(R.string.last_5_transactions)
                    }
                    Option.ChangePIN -> {
                        optionImage.setImageResource(R.drawable.account_change_pin)
                        optionText.setText(R.string.change_pin)
                    }
                    /*Option.MyContractInformation -> {
                        optionImage.setImageResource(0)
                        optionText.setText(R.string.my_contract_information)
                    }*/
                    Option.MyProfile -> {
                        optionImage.setImageResource(R.drawable.account_my_profile)
                        optionText.setText(R.string.my_profile)
                    }
                    else -> {}
                }

                root.setOnClickListener { _ -> it.redirect() }
            }
        }
    }

    private fun Option.redirect() {
        when (this) {
            Option.LastTransactions -> {
                findNavController().runIfFrom(R.id.accountFragment) {
                    navigate(AccountFragmentDirections.actionAccountFragmentToEnterPinFragment())
                }
            }
            Option.ChangePIN -> {
                findNavController().runIfFrom(R.id.accountFragment) {
                    navigate(AccountFragmentDirections.actionAccountFragmentToNavChangePin())
                }
            }
            /*Option.MyContractInformation -> {
                findNavController().runIfFrom(R.id.accountFragment) {
                    //...
                }
            }*/
            Option.MyProfile -> {
                findNavController().runIfFrom(R.id.accountFragment) {
                    navigate(AccountFragmentDirections.actionAccountFragmentToMyProfileFragment())
                }
            }
            else -> {}
        }
    }


    private fun bindLoggedOut() {
        observeResourceProgress(viewModel.loggedOut) {
            appSessionNavigator.restart()
        }
    }
}