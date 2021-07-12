package com.africell.africell.ui.spinner.searchable

import com.africell.africell.ui.spinner.MaterialSpinnerItem
import java.io.Serializable

/**
 * The object should implement Parcelable for retaining dialog instance when pausing the fragment.
 */
open class MaterialSearchableSpinnerItem : MaterialSpinnerItem, Serializable {
    override fun toDisplayString(): String = toString()
    open fun matchesQuery(query: String) = toDisplayString().startsWith(query, true)
}