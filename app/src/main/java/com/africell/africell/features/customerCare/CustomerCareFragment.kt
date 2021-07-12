package com.africell.africell.features.customerCare

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.api.ApiContract.Params.PHONE_NUMBER
import com.africell.africell.data.api.dto.SupportCategoryDTO
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.intents.dial
import kotlinx.android.synthetic.main.fragment_customer_care.*
import kotlinx.android.synthetic.main.toolbar_image.*
import javax.inject.Inject

class CustomerCareFragment : BaseFragment(), Liv.Action {
    @Inject
    lateinit var sessionRepository: SessionRepository

    private val liv by lazy { initLiv() }


    private val viewModel by provideViewModel<CustomerCareViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_customer_care, R.layout.toolbar_image, true)
    }


    override fun configureToolbar() {
        super.configureToolbar()
        setHasOptionsMenu(true)
        actionbar?.title = ""
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        toolbarImage.setActualImageResource(R.mipmap.img_report)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (sessionRepository.isLoggedIn()) {
            liv.start()
            setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.CUSTOMER_CARE)
            bindData()
            contactUsBtn.setOnClickListener {
                activity?.hideKeyboard()
                liv.submitWhenValid()
            }
        } else {
            showInlineMessageWithAction(getString(R.string.login_first),actionName = getString(R.string.login)) {
                redirectToLogin()
            }
        }
    }


    private fun bindData() {
        viewModel.getSupportCategory()
        observeResourceInline(viewModel.supportCategoryData) { supportCat ->
            supportCategoryLayout.adapter =
                ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, supportCat)

        }


        observeResource(viewModel.contactUsData) {
            showMessageDialog(it.resultText.orEmpty(), "Close") {
                findNavController().popBackStack()
            }
        }
    }


    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val emailRule = EmailRule(getString(R.string.invalid_email))

        val builder = Liv.Builder()
        builder.add(supportCategoryLayout, notEmptyRule)
            .add(messageLayout, notEmptyRule)


        return builder.submitAction(this).build()
    }

    override fun performAction() {
        val catId = (supportCategoryLayout.selectedItem as SupportCategoryDTO)
        viewModel.contactUs(
            catId,
            messageLayout.getText()
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
