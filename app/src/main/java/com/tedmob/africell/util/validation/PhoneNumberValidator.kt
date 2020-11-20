package com.tedmob.africell.util.validation

import android.widget.Spinner
import com.benitobertoli.liv.validator.ValidationTime
import com.benitobertoli.liv.validator.Validator
import com.benitobertoli.liv.validator.ValidatorState
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.ui.spinner.MaterialSpinner
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * Created by lenovo on 12/29/2017.
 */
class PhoneNumberValidator(
        countryCodeSpinner: MaterialSpinner,
        phoneNumberTextLayout: TextInputLayout,
        private val notEmpty: Boolean = false,
        private val requiredMessage: String = "Required",
        private val invalidPhoneMessage: String = "Invalid phone number"
) : Validator() {

    private val DEBOUNCE_TIMEOUT_MILLIS = 500L

    private val countryCodeSpinnerRef: WeakReference<MaterialSpinner> = WeakReference(countryCodeSpinner)
    private val phoneNumberTextLayoutRef: WeakReference<TextInputLayout> = WeakReference(phoneNumberTextLayout)

    private var conditionDisposable: Disposable? = null
    private var textChangeDisposable: Disposable? = null

    private val time: ValidationTime = ValidationTime.LIVE

    init {
        textChangeDisposable =
                phoneNumberTextLayoutRef.get()?.editText?.let {
                    RxTextView.textChanges(it)
                            .skip(1)
                            .debounce(DEBOUNCE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                if (time == ValidationTime.LIVE) {
                                    validate()
                                } else {
                                    setState(ValidatorState.NOT_VALIDATED)
                                }
                            }) { throwable -> throwable.printStackTrace() }
                }
    }

    override fun validate() {
        val countryCodeSpinner = countryCodeSpinnerRef.get()
        val phoneNumberTextLayout = phoneNumberTextLayoutRef.get()

        if (countryCodeSpinner != null && phoneNumberTextLayout != null) {
            conditionDisposable = Observable.just(

                    //fixme Boolean should be replaced with enum to differentiate between required and invalid
                    //TODO add option to change type, or to pass custom PhoneNumberHelper

                    //1st check: if phoneNumber is empty and is allowed to be empty.
                    //2nd check: if phoneNumber is valid according to country code.
                    (phoneNumberTextLayout.getText() == "" && !notEmpty) || PhoneNumberHelper.isValid(
                            (countryCodeSpinner.selectedItem as? Country)?.phonecode,
                            phoneNumberTextLayout.getText(),
                            PhoneNumberHelper.Type.MOBILE
                    )
            )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ condition ->
                        if (condition) {
                            //Condition satisfied.
                            phoneNumberTextLayout.error = null
                            state = ValidatorState.VALID
                        } else {
                            //Condition not satisfied.
                            phoneNumberTextLayout.error =
                                    if (phoneNumberTextLayout.getText() == "") requiredMessage else invalidPhoneMessage
                            state = ValidatorState.INVALID
                        }
                    }, { it.printStackTrace() })
        }
    }

    private fun TextInputLayout.getText() = editText?.text?.toString() ?: ""

    override fun dispose() {
        conditionDisposable?.dispose()
        textChangeDisposable?.dispose()
    }
}