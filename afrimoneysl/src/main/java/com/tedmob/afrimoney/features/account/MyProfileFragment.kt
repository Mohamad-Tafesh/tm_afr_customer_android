package com.tedmob.afrimoney.features.account

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
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.GenderItem
import com.tedmob.afrimoney.data.entity.Profile
import com.tedmob.afrimoney.databinding.FragmentMyProfileBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.dpToPx
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.image.BorderedCircleCropTransformation
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.media.BitmapDecoder
import com.tedmob.afrimoney.util.media.ImageDecoder
import com.tedmob.afrimoney.util.media.MediaPicker
import com.tedmob.afrimoney.util.setText
import com.tedmob.afrimoney.util.validation.BitmapExistsRule
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.fieldvalidators.FormValidatorField
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.state.ValidationState
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import java.util.*

@AndroidEntryPoint
class MyProfileFragment : BaseVBFragment<FragmentMyProfileBinding>() {

    companion object {
        const val USER = 0
        const val FRONT = 1
        const val BACK = 2
        const val PASSPORT = 3
    }


    private val viewModel by provideViewModel<MyProfileViewModel>()
    //...

    private var validator: FormValidator? = null

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

    private var currentImageSelected: Int = -1
    private var userImage: Bitmap? = null
    private var frontImage: Bitmap? = null
    private var backImage: Bitmap? = null
    private var passportImage: Bitmap? = null

    private val mediaPicker: MediaPicker by lazy {
        MediaPicker.Builder(requireActivity())
            .fragment(this)
            .directoryName("Afrimoney")
            .type(MediaPicker.Type.IMAGE_BOTH)
            .callback(
                object : MediaPicker.Callback {
                    override fun onImagePicked(imageUri: Uri) {
                        setImage(imageUri)
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
                                saveImage(result?.get())
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentMyProfileBinding::inflate, true)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            backButton.setDebouncedOnClickListener { findNavController().popBackStack() }

            userImageView.load(R.drawable.ic_camera) {
                size(ViewSizeResolver(userImageView))
                lifecycle(viewLifecycleOwner)
                transformations(
                    BorderedCircleCropTransformation(
                        resources.dpToPx(2),
                        primaryColor,
                        Scale.FIT,
                        backgroundColor,
                    ),
                )
            }

            //dob.fragmentManagerProvider = { childFragmentManager }

            addressOfResidenceEdit.setDebouncedOnClickListener {
                hideKeyboard()
                findNavController().navigate(MyProfileFragmentDirections.actionMyProfileFragmentToNavAddress())
            }

            /*userImageView.setDebouncedOnClickListener {
                currentImageSelected = USER
                mediaPicker.launch()
            }*/
            frontImageView.setDebouncedOnClickListener {
                currentImageSelected = FRONT
                mediaPicker.launch()
            }
            backImageView.setDebouncedOnClickListener {
                currentImageSelected = BACK
                mediaPicker.launch()
            }
            passportImageView.setDebouncedOnClickListener {
                currentImageSelected = PASSPORT
                mediaPicker.launch()
            }

            gender.adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                resources.let {
                    it.getStringArray(R.array.genders).zip(it.getStringArray(R.array.gender_values)) { t1, t2 ->
                        GenderItem(t1, t2)
                    }
                }
            )


            setupFormValidation()
        }

        bindData()
        bindEvents()
        bindAddress()

        viewModel.getProfile()
    }

    private fun FragmentMyProfileBinding.setupFormValidation() {
        validator = formValidator {
            val notEmptyTextRule = NotEmptyRule(getString(R.string.mandatory_field))

            validateField(
                object : FormValidatorField<Bitmap?>(
                    listOf(BitmapExistsRule(getString(R.string.image_is_required)))
                ) {
                    override suspend fun startValidation() {
                        validateImage(userImage)
                    }
                }
            )
            firstName.validate(notEmptyTextRule)
            lastName.validate(notEmptyTextRule)
            dob.validate(
                ifNotEmpty(englishDateRule("dd/MM/yyyy", getString(R.string.invalid_date_of_birth)))
            )

            idNumber.validate(notEmptyTextRule)
            idType.validate(notEmptyTextRule)
            validateField(
                object : FormValidatorField<Bitmap?>(
                    listOf(BitmapExistsRule(getString(R.string.front_image_is_required)))
                ) {
                    override suspend fun startValidation() {
                        validateImage(frontImage)
                    }
                }
            )
            validateField(
                object : FormValidatorField<Bitmap?>(
                    listOf(BitmapExistsRule(getString(R.string.back_image_is_required)))
                ) {
                    override suspend fun startValidation() {
                        validateImage(backImage)
                    }
                }
            )
            validateField(
                object : FormValidatorField<Bitmap?>(
                    listOf(BitmapExistsRule(getString(R.string.passport_image_is_required)))
                ) {
                    override suspend fun startValidation() {
                        validateImage(passportImage)
                    }
                }
            )

            onValid = { binding?.validateAddress() }
        }
    }

    private suspend inline fun FormValidatorField<Bitmap?>.validateImage(image: Bitmap?) {
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

    private fun FragmentMyProfileBinding.validateAddress() {
        if (!viewModel.hasAddress()) {
            showMaterialMessageDialog(
                getString(R.string.please_fill_the_address_of_residence)
            )
            return
        }

        //TODO save profile
    }


    private fun bindData() {
        observeResourceInline(viewModel.profile) {
            withVBAvailable {
                setupProfile(it)
            }
        }
    }

    private fun bindEvents() {
        //...
    }


    private fun FragmentMyProfileBinding.setupProfile(profile: Profile) {
        profile.userImage?.let {
            currentImageSelected = USER
            setImage(it.toUri())
        }
        firstName.setText(profile.firstName.orEmpty())
        lastName.setText(profile.lastName.orEmpty())
        gender.selection = resources.getStringArray(R.array.gender_values)
            .let { it.indexOfFirst { it == profile.genderValue } }
       profile.dob?.let { date -> dob.setText(date) }

        idNumber.setText(profile.idNumber.orEmpty())
        idType.setText(profile.idType.orEmpty())

        profile.frontImage?.let {
            currentImageSelected = FRONT
            setImage(it.toUri())
        }
        profile.backImage?.let {
            currentImageSelected = BACK
            setImage(it.toUri())
        }
        profile.passportImage?.let {
            currentImageSelected = PASSPORT
            setImage(it.toUri())
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (!mediaPicker.handleRequestPermissionsResult(requestCode, permissions, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!mediaPicker.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private inline fun setImage(uri: Uri) {
        withVBAvailable {
            when (currentImageSelected) {
                USER -> {
                    userImageView.load(uri) {
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
                FRONT -> {
                    frontImageView.load(uri) {
                        lifecycle(viewLifecycleOwner)
                    }
                    frontImageText.isVisible = false
                }
                BACK -> {
                    backImageView.load(uri) {
                        lifecycle(viewLifecycleOwner)
                    }
                    backImageText.isVisible = false
                }
                PASSPORT -> {
                    passportImageView.load(uri) {
                        lifecycle(viewLifecycleOwner)
                    }
                    passportImageText.isVisible = false
                }
            }
        }
    }

    private inline fun saveImage(bitmap: Bitmap?) {
        when (currentImageSelected) {
            USER -> userImage = bitmap
            FRONT -> frontImage = bitmap
            BACK -> backImage = bitmap
            PASSPORT -> passportImage = bitmap
        }
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