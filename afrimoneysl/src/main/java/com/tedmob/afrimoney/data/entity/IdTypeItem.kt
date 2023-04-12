package com.tedmob.afrimoney.data.entity

import com.tedmob.afrimoney.ui.spinner.MaterialSpinnerItem

class IdTypeItem(
    val label: String,
    val value: String,
) : MaterialSpinnerItem {
    override fun toDisplayString(): String = label

    override fun toString(): String {
        return label
    }

}