package com.tedmob.afrimoney.ui.form

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.textfield.TextInputLayout
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.fieldvalidators.TextInputLayoutValidatorField
import com.tedmob.libraries.validators.formValidator

class FormLayoutWithValidation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    companion object {
        const val SOURCE_ITEM = 25
        const val VALIDATION_FIELD = 49
    }


    private var validator: FormValidator? = null
        set(value) {
            field?.stop()
            field = value
        }


    var onSubmittedFormValid: (suspend () -> Unit)? = null


    fun addViewFrom(/*TODO custom item to convert to view*/) {
        //...
        /*View(context).apply {
            setTag(SOURCE_ITEM, Unit)
            setTag(VALIDATION_FIELD, Unit)
        }*/
    }

    fun attachValidator() {
        validator = formValidator {
            children.map {
                //TODO get actual field from view
                TextInputLayoutValidatorField(TextInputLayout(context), emptyList())
            }.forEach {
                validateField(it)
            }

            this@FormLayoutWithValidation.onSubmittedFormValid?.let {
                onValid = it
            }
        }
    }

    fun submitForValidation(lifecycleScope: LifecycleCoroutineScope) {
        validator?.submit(lifecycleScope)
    }


    inline fun runThenAttachValidator(block: FormLayoutWithValidation.() -> Unit) {
        this.block()
        attachValidator()
    }
}