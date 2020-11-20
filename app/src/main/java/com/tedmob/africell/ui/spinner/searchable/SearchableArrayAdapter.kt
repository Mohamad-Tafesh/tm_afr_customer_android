package com.tedmob.africell.ui.spinner.searchable

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.ThemedSpinnerAdapter
import java.util.*

internal class SearchableArrayAdapter<T : MaterialSearchableSpinnerItem> : BaseAdapter, Filterable, ThemedSpinnerAdapter {
    /**
     * Lock used to modify the content of [.mObjects]. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see [.getFilter] to make a synchronized copy of
     * the original array of data.
     */
    private val mLock = Any()

    private val mInflater: LayoutInflater

    val context: Context

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private val mResource: Int

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter in a drop down widget.
     */
    private var mDropDownResource: Int

    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private var mObjects: MutableList<T>

    /**
     * Indicates whether the contents of [.mObjects] came from static resources.
     */
    private var mObjectsFromResources: Boolean

    /**
     * If the inflated resource is not a TextView, `mFieldId` is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    private val mFieldId: Int

    /**
     * Indicates whether or not [.notifyDataSetChanged] must be called whenever
     * [.mObjects] is modified.
     */
    private var mNotifyOnChange = true

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private var mOriginalValues: ArrayList<T>? = null
    private val mFilter: ArrayFilter by lazy { ArrayFilter() }

    /** Layout inflater used for [.getDropDownView].  */
    private val mDropDownInflater: LayoutInflater? = null

    private constructor(context: Context,
                        @LayoutRes resource: Int,
                        @IdRes textViewResourceId: Int,
                        objects: MutableList<T>,
                        objsFromResources: Boolean) {

        this.context = context
        mInflater = LayoutInflater.from(context)
        mResource = resource
        mDropDownResource = resource

        mObjects = objects

        mObjectsFromResources = objsFromResources
        mFieldId = textViewResourceId
    }

    constructor(
            context: Context,
            @LayoutRes resource: Int,
            @IdRes textViewResourceId: Int = 0,
            objects: MutableList<T>
    ) : this(context, resource, textViewResourceId, objects, false)

    fun add(item: T) {
        synchronized(mLock) {
            mOriginalValues?.add(item) ?: mObjects.add(item)
        }
        mObjectsFromResources = false
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun addAll(collection: Collection<T>) {
        synchronized(mLock) {
            mOriginalValues?.addAll(collection) ?: mObjects.addAll(collection)
        }
        mObjectsFromResources = false
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun addAll(vararg items: T) {
        synchronized(mLock) {
            if (items.isNotEmpty()) {
                mOriginalValues?.addAll(items) ?: mObjects.addAll(items)
            }
        }
        mObjectsFromResources = false
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun insert(item: T, index: Int) {
        synchronized(mLock) {
            mOriginalValues?.add(index, item) ?: mObjects.add(index, item)
        }
        mObjectsFromResources = false
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun remove(item: T) {
        synchronized(mLock) {
            mOriginalValues?.remove(item) ?: mObjects.remove(item)
        }
        mObjectsFromResources = false
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun clear() {
        synchronized(mLock) {
            mOriginalValues?.clear() ?: mObjects.clear()
        }
        mObjectsFromResources = false
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    fun sort(comparator: Comparator<in T>?) {
        synchronized(mLock) {
            comparator?.let { mOriginalValues?.sortWith(it) }
        }
        mObjectsFromResources = false
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        mNotifyOnChange = true
    }

    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }

    override fun getCount() = mObjects.size

    override fun getItem(p0: Int) = mObjects.get(p0)

    fun getPosition(item: T) = mObjects.indexOf(item)

    override fun getItemId(p0: Int) = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup) = createViewFromResource(mInflater, p0, p1, p2, mResource)

    private fun createViewFromResource(inflater: LayoutInflater,
                                       position: Int,
                                       convertView: View?,
                                       parent: ViewGroup,
                                       resource: Int): View {

        val tempView = convertView ?: inflater.inflate(resource, parent, false)

        val textView = if (mFieldId == 0) tempView as? TextView else tempView.findViewById(mFieldId)
        textView?.text = getItem(position).toDisplayString()

        return tempView
    }

    fun setDropDownViewResource(@LayoutRes resource: Int) {
        mDropDownResource = resource
    }

    override fun setDropDownViewTheme(theme: Resources.Theme?) {
        val helper = ThemedSpinnerAdapter.Helper(context)
        helper.dropDownViewTheme = theme
    }

    override fun getDropDownViewTheme(): Resources.Theme? {
        val helper = ThemedSpinnerAdapter.Helper(context)
        return helper.dropDownViewTheme
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val inflater = mDropDownInflater ?: mInflater
        return createViewFromResource(inflater, position, convertView, parent, mDropDownResource)
    }

    override fun getFilter(): Filter = mFilter

    /**
     *
     * An array filter constrains the content of the array adapter with
     * a withStart. Each item that does not start with the supplied withStart
     * is removed from the list.
     */
    private inner class ArrayFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()

            if (mOriginalValues == null) {
                synchronized(mLock) {
                    mOriginalValues = ArrayList(mObjects)
                }
            }

            if (prefix == null || prefix.isEmpty()) {
                //Synchronized flow changed because of the compiler nagging about list not being initialized
                synchronized(mLock) {
                    val list = ArrayList(mOriginalValues.orEmpty())
                    results.values = list
                    results.count = list.size
                }
            } else {
                val prefixString = prefix.toString()

                var values: ArrayList<T> = ArrayList()
                synchronized(mLock) {
                    values = ArrayList(mOriginalValues.orEmpty())
                }

                val newValues = values.filter { it.matchesQuery(prefixString) }
                results.values = newValues
                results.count = newValues.size
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            mObjects = results?.values as? MutableList<T> ?: ArrayList()
            if (results?.count ?: 0 > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }

    companion object {
        fun defaultFilterMatch(value: Any, prefix: CharSequence?): Boolean {
            val prefixString = prefix.toString()
            val valueText = value.toString().toLowerCase()

            return if (valueText.startsWith(prefixString)) {
                true
            } else {
                valueText.split(" ").any { it.startsWith(prefixString) }
            }
        }
    }
}