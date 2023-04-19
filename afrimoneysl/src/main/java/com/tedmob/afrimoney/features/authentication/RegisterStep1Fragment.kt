package com.tedmob.afrimoney.features.authentication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.ColorInt
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.GenderItem
import com.tedmob.afrimoney.databinding.FragmentRegisterStep1Binding
import com.tedmob.afrimoney.features.account.MyAddressFragment
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.dpToPx
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.image.BorderedCircleCropTransformation
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.media.BitmapDecoder
import com.tedmob.afrimoney.util.media.ImageDecoder
import com.tedmob.afrimoney.util.media.MediaPicker
import com.tedmob.afrimoney.util.validation.BitmapExistsRule
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.fieldvalidators.FormValidatorField
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.state.ValidationState
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import java.time.LocalDateTime
import java.time.Year
import java.util.*

@AndroidEntryPoint
class RegisterStep1Fragment : BaseVBFragment<FragmentRegisterStep1Binding>() {

    private var validator: FormValidator? = null

    private val onDestinationChangedListener: NavController.OnDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                hideKeyboard()
            }
        }

    private val viewModel by provideNavGraphViewModel<RegisterViewModel>(R.id.nav_register)


    private val mediaPicker: MediaPicker by lazy {
        MediaPicker.Builder(requireActivity())
            .fragment(this)
            .directoryName("Afrimoney")
            .type(MediaPicker.Type.IMAGE_BOTH)
            .callback(
                object : MediaPicker.Callback {
                    override fun onImagePicked(imageUri: Uri) {
                        binding?.userImage?.load(imageUri) {
                            lifecycle(viewLifecycleOwner)
                            transformations(
                                BorderedCircleCropTransformation(
                                    resources.dpToPx(2),
                                    primaryColor,
                                    Scale.FILL,
                                ),
                            )
                        }
                    }

                    override fun onVideoPicked(videoUri: Uri) {
                    }
                }
            )
            .imageDecoder(
                BitmapDecoder.Builder(requireActivity())
                    .resize(BitmapDecoder.ResizeType.MAX, 700, 700)
                    .callback(
                        object : ImageDecoder.Callback<WeakReference<Bitmap?>> {
                            override fun onStarted() {
                                showProgressDialog(getString(R.string.loading_))
                            }

                            override fun onFinished(result: WeakReference<Bitmap?>?) {
                                hideProgressDialog()
                                image = result?.get()
                            }

                            override fun onError(t: Throwable) {
                                hideProgressDialog()
                                showMaterialMessageDialog(t.localizedMessage.orEmpty())
                            }
                        }
                    )
                    .build()
            )
            .build()
    }
    private var image: Bitmap? = null

    @get:ColorInt
    private val primaryColor: Int
        get() =
            TypedValue().run {
                requireContext().theme.resolveAttribute(R.attr.colorPrimary, this, true)
                data
            }

    @get:ColorInt
    private val backgroundColor: Int
        get() =
            TypedValue().run {
                requireContext().theme.resolveAttribute(android.R.attr.colorBackground, this, true)
                data
            }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentRegisterStep1Binding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  viewModel.code = args.token
        bindEvents()
        bindAddress()

        binding?.let {
            it.backButton.setDebouncedOnClickListener { activity?.finish() }

            /*    it.userImage.load(R.drawable.ic_camera) {
                    size(ViewSizeResolver(it.userImage))
                    lifecycle(viewLifecycleOwner)
                    transformations(
                        BorderedCircleCropTransformation(
                            resources.dpToPx(2),
                            primaryColor,
                            Scale.FIT,
                            backgroundColor,
                        ),
                    )
                }*/

            it.dob.fragmentManagerProvider = { childFragmentManager }
            it.dob.onPickerShown = {
                //(LocalDateTime.now().year).toLong()
                this.maxDate = Calendar.getInstance().timeInMillis
            }

            it.setupFormValidation()
            it.gender.adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                resources.let {
                    it.getStringArray(R.array.genders)
                        .zip(it.getStringArray(R.array.gender_values)) { t1, t2 ->
                            GenderItem(t1, t2)
                        }
                }
            )
        }

        findNavController().addOnDestinationChangedListener(onDestinationChangedListener)

        withVBAvailable {
            nextButton.setDebouncedOnClickListener {

                hideKeyboard()
                validator?.submit(viewLifecycleOwner.lifecycleScope)
            }
            userImage.setDebouncedOnClickListener {
                hideKeyboard()
                //  mediaPicker.launch()
            }
            addressOfResidenceEdit.setDebouncedOnClickListener {
                hideKeyboard()
                findNavController().navigate(RegisterStep1FragmentDirections.actionRegisterFragmentToNavAddress())
            }
        }
    }

    private fun bindEvents() {
        observeResourceFromButton(viewModel.proceedToStep2, R.id.nextButton) {
            proceed()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!mediaPicker.handleRequestPermissionsResult(requestCode, permissions, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!mediaPicker.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun FragmentRegisterStep1Binding.setupFormValidation() {
        validator = formValidator {
            val notEmptyTextRule = NotEmptyRule(getString(R.string.mandatory_field))

            /*validateField(
                object : FormValidatorField<Bitmap?>(
                    listOf(BitmapExistsRule(getString(R.string.image_is_required)))
                ) {
                    override suspend fun startValidation() {
                        stateFlow.value = ValidationState.Validating

                        val isValid = rules.all { it.isValid(image) }

                        stateFlow.value = if (isValid) {
                            ValidationState.Valid
                        } else {
                            val error = rules.first { !it.isValid(image) }.errorMessage
                            showMaterialMessageDialog(error)
                            ValidationState.NotValid
                        }
                    }
                }
            )*/
            firstName.validate(notEmptyTextRule)
            lastName.validate(notEmptyTextRule)
            dob.validate(
                notEmptyTextRule,
                englishDateRule("dd/MM/yyyy", getString(R.string.invalid_date_of_birth))
            )
            gender.validate(
                notEmptyTextRule
            )

            onValid = { binding?.validateAddress() }
        }
    }


    private fun FragmentRegisterStep1Binding.validateAddress() {
        /*if (!viewModel.hasAddress()) {
            showMaterialMessageDialog(
                getString(R.string.please_fill_the_address_of_residence)
            )
            return
        }*/

        viewModel.registerStep1(
            //image!!,
            firstName.getText(),
            lastName.getText(),
            (gender.selectedItem as? GenderItem?)?.value,
            dob.currentCalendar,
            //referredInput.getText().takeUnless { it.isBlank() },
        )
    }

    private fun proceed() {
        findNavController().navigate(
            RegisterStep1FragmentDirections.actionRegisterFragmentToRegisterStep2Fragment(
             session.msisdn
            )
        )

    }


    override fun onDestroy() {
        super.onDestroy()
        findNavController().removeOnDestinationChangedListener(onDestinationChangedListener)
    }


    private fun bindAddress() {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<MyAddressFragment.Result>(MyAddressFragment.KEY__RESULT)
            ?.observe(viewLifecycleOwner) {
                viewModel.setAddress(
                    it.coordinates,
                    it.selectedProvidence,
                    it.selectedCity,
                    it.selectedStreet,
                )

                withVBAvailable {
                    addressOfResidenceEdit.setText("${it.selectedProvidence}, ${it.selectedCity}, ${it.selectedStreet}")
                }
            }
    }
}
