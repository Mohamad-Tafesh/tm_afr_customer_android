package com.africell.africell.features.customerCare

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.api.ApiContract.Params.PHONE_NUMBER
import com.africell.africell.data.api.dto.SupportCategoryDTO
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentCustomerCareBinding
import com.africell.africell.databinding.ToolbarImageBinding
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.intents.dial
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import javax.inject.Inject

class CustomerCareFragment : BaseVBFragment<FragmentCustomerCareBinding>(), Liv.Action {

    @Inject
    lateinit var sessionRepository: SessionRepository

    private val liv by lazy { initLiv() }


    private val viewModel by provideViewModel<CustomerCareViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentCustomerCareBinding::inflate, true, ToolbarImageBinding::inflate)
    }


    override fun configureToolbar() {
        super.configureToolbar()
        setHasOptionsMenu(true)
        getToolbarBindingAs<ToolbarImageBinding>()?.run {
            actionbar?.title = ""
            actionbar?.setDisplayHomeAsUpEnabled(true)
            actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
            toolbarImage.setActualImageResource(R.mipmap.img_report)
            toolbarTitle.setText(R.string.customer_care)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (sessionRepository.isLoggedIn()) {
            liv.start()
            getToolbarBindingAs<ToolbarImageBinding>()?.run {
                setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.CUSTOMER_CARE)
            }
            bindData()
            withVBAvailable {
                contactUsBtn.setOnClickListener {
                    activity?.hideKeyboard()
                    liv.submitWhenValid()
                }
            }
        } else {
            showInlineMessageWithAction(getString(R.string.login_first), actionName = getString(R.string.login)) {
                redirectToLogin()
            }
        }
    }


    private fun bindData() {
        viewModel.getSupportCategory()
        observeResourceInline(viewModel.supportCategoryData) { supportCat ->
            withVBAvailable {
                supportCategoryLayout.adapter =
                    ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, supportCat)
            }

        }


        observeResource(viewModel.contactUsData) {
            showMaterialMessageDialog(
                getString(R.string.successful),
                it.resultText.orEmpty(),
                getString(R.string.close)
            ) {
                findNavController().popBackStack()
            }
        }
    }


    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val emailRule = EmailRule(getString(R.string.invalid_email))

        val builder = Liv.Builder()
        builder.add(requireBinding().supportCategoryLayout, notEmptyRule)
            .add(requireBinding().messageLayout, notEmptyRule)


        return builder.submitAction(this).build()
    }

    override fun performAction() {
        val catId = (requireBinding().supportCategoryLayout.selectedItem as SupportCategoryDTO)
        viewModel.contactUs(
            catId,
            requireBinding().messageLayout.getText()
        )
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_call, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.item_call) {
            dial(PHONE_NUMBER)
            true
        } else super.onOptionsItemSelected(item)
    }
}
