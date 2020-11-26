package com.tedmob.africell.features.reportIncident

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.ApiContract.Params.PHONE_NUMBER
import com.tedmob.africell.data.api.dto.SupportCategoryDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.intents.dial
import com.tedmob.africell.util.intents.openWhatsAppWith
import com.tedmob.africell.util.setTextWhenNotBlank
import kotlinx.android.synthetic.main.fragment_report_incident.*
import kotlinx.android.synthetic.main.toolbar_image.*
import javax.inject.Inject

class ReportIncidentFragment : BaseFragment(), Liv.Action {
    @Inject
    lateinit var sessionRepository: SessionRepository


    private val liv by lazy { initLiv() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<ReportIncidentViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_report_incident, R.layout.toolbar_image, true)
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
        liv.start()
        setupUi()
        bindData()
        contactUsBtn.setOnClickListener {
            liv.submitWhenValid()
        }
    }

    private fun setupUi() {
        val user = sessionRepository.user
        emailLayout.setTextWhenNotBlank(user?.email)
      }

    private fun bindData() {
        viewModel.getSupportCategory()
        observeResourceInline(viewModel.supportCategoryData) { supportCat ->
            supportCategoryLayout.adapter =
                ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, supportCat)

        }


        observeResource(viewModel.contactUsData) {
            showMessageDialog(it.msg.orEmpty(), "Close") {
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

        builder.add(emailLayout, notEmptyRule, emailRule)

        return builder.submitAction(this).build()
    }

    override fun performAction() {
        val catId = (supportCategoryLayout.selectedItem as SupportCategoryDTO).id
        viewModel.contactUs(
            emailLayout.getText(),
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
