package com.tedmob.afrimoney.features.authentication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.IdTypeItem
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.databinding.FragmentRegisterStep2Binding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.media.BitmapDecoder
import com.tedmob.afrimoney.util.media.ImageDecoder
import com.tedmob.afrimoney.util.media.MediaPicker
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.fieldvalidators.FormValidatorField
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.state.ValidationState
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class RegisterStep2Fragment : BaseVBFragment<FragmentRegisterStep2Binding>() {

    companion object {
        const val FRONT = 0
        const val BACK = 1
        const val PASSPORT = 2
    }


    private var validator: FormValidator? = null

    private val viewModel by provideNavGraphViewModel<RegisterViewModel>(R.id.nav_register)

    private val args by navArgs<RegisterStep2FragmentArgs>()

    private val mediaPicker: MediaPicker by lazy {
        MediaPicker.Builder(requireActivity())
            .fragment(this)
            .directoryName("Afrimoney")
            .type(MediaPicker.Type.IMAGE_BOTH)
            .callback(
                object : MediaPicker.Callback {
                    override fun onImagePicked(imageUri: Uri) {
                        //setImage(imageUri)
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
    private var currentImageSelected: Int = -1
    private var frontImage: Bitmap? = null
    private var backImage: Bitmap? = null
    private var passportImage: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentRegisterStep2Binding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindEvents()
        // Toast.makeText(context,viewModel.test(),Toast.LENGTH_LONG).show()
        //  Toast.makeText(context,args.mobilenb,Toast.LENGTH_LONG).show()

        withVBAvailable {
            backButton.setDebouncedOnClickListener { findNavController().popBackStack() }

            setupValidator()
            confirmingButton.setDebouncedOnClickListener {
                validator?.submit(viewLifecycleOwner.lifecycleScope)
            }
/*
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
*/

            idType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                resources.let {
                    it.getStringArray(R.array.idtype)
                        .zip(it.getStringArray(R.array.idtype_values)) { t1, t2 ->
                            IdTypeItem(t1, t2)
                        }
                }
            )
        }
    }

    private fun bindEvents() {
        observeResourceFromButton(viewModel.proceedToPin, R.id.confirmingButton) {
            proceedWith(it)
        }
    }

    private fun FragmentRegisterStep2Binding.setupValidator() {
        validator = formValidator {
            val notEmptyTextRule = NotEmptyRule(getString(R.string.mandatory_field))

            idNumber.validate(notEmptyTextRule)
            idType.validate(notEmptyTextRule)
            /*  validateField(
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
              )*/

            onValid = {
                viewModel.registerStep2(
                    args.mobilenb,
                    idNumber.getText(),
                    idType.getText(),
                    //  frontImage!!,
                    // backImage!!,
                    // passportImage!!
                )
                //findNavController().navigate(R.id.action_registerStep2Fragment_to_changePinRegisterFragment)
            }
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


    /* private inline fun setImage(uri: Uri) {
         withVBAvailable {
             when (currentImageSelected) {
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
     }*/

    private fun saveImage(bitmap: Bitmap?) {
        when (currentImageSelected) {
            FRONT -> frontImage = bitmap
            BACK -> backImage = bitmap
            PASSPORT -> passportImage = bitmap
        }
    }


    private fun proceedWith(state: UserState) {
        when (state) {
            is UserState.NotRegistered -> {
                /*  findNavController().navigate(
                     LoginVerificationFragmentDirections.actionLoginVerificationFragmentToNavRegister(
                          state.token
                      )
                      //  LoginVerificationFragmentDirections.actionLoginVerificationFragmentToSetPinFragment()
                  )*/
            }
            is UserState.Registered -> {
                findNavController().navigate(R.id.action_registerStep2Fragment_to_changePinRegisterFragment)
            }
        }
    }


}