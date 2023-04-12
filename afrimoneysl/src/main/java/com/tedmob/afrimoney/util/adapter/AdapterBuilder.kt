package com.tedmob.afrimoney.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


open class AdapterHolder(view: View) : RecyclerView.ViewHolder(view)
open class AdapterHolderVB<VB : ViewBinding>(val viewBinding: VB) : AdapterHolder(viewBinding.root)

abstract class FlexibleAdapter<T>(val adapterItems: MutableList<T> = mutableListOf()) :
    RecyclerView.Adapter<AdapterHolder>() {

    override fun getItemCount(): Int = adapterItems.size
}

@DslMarker
annotation class AdapterBuilderMarker

@AdapterBuilderMarker
class AdapterBuilder<T> {
    var onCreateHolder: FlexibleAdapter<T>.(parent: ViewGroup, viewType: Int) -> AdapterHolder =
        { _, _ -> throw NotImplementedError("AdapterBuilder.onCreateHolder was not set (either directly or using: layout(), view(), generateHolders(), ...).") }

    var onBindHolder: FlexibleAdapter<T>.(holder: AdapterHolder, position: Int) -> Unit = { _, _ -> }

    var startingItems: MutableList<T> = mutableListOf()

    var getItemCount: (FlexibleAdapter<T>.() -> Int)? = null

    var getItemViewType: (FlexibleAdapter<T>.(position: Int) -> Int)? = null


    //onCreateHolder extensions:
    inline fun view(view: View) {
        onCreateHolder = { _, _ -> AdapterHolder(view) }
    }

    inline fun view(crossinline onCreateView: (parent: ViewGroup) -> View) {
        onCreateHolder = { parent, _ -> AdapterHolder(onCreateView(parent)) }
    }

    inline fun layout(@LayoutRes res: Int, crossinline setup: View.() -> Unit = {}) {
        onCreateHolder = { parent, _ ->
            AdapterHolder(
                LayoutInflater.from(parent.context).inflate(res, parent, false)
                    .apply(setup)
            )
        }
    }

    inline fun <reified VB : ViewBinding> viewBinding(
        crossinline inflate: (inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean) -> VB,
        crossinline setup: VB.() -> Unit = {}
    ) {
        onCreateHolder = { parent, _ ->
            AdapterHolderVB(
                inflate(LayoutInflater.from(parent.context), parent, false)
                    .apply(setup)
            )
        }
    }

    inline fun provideHolders(createHolders: FlexibleHolderGenerator<T>.() -> Unit) {
        FlexibleHolderGenerator<T>().apply(createHolders).integrateInto(this)
    }
    //---

    //onBindHolder extensions:
    inline fun <reified V : AdapterHolder> onBindHolder(crossinline onBind: V.(position: Int) -> Unit) {
        onBindHolder = { holder, position -> (holder as V).onBind(position) }
    }

    @JvmName("onBindItem_Holder")
    inline fun <reified V : AdapterHolder> onBindItem(crossinline onBind: V.(item: T) -> Unit) {
        onBindHolder = { holder, position -> (holder as V).onBind(adapterItems[position]) }
    }

    inline fun onBindItem(crossinline onBind: AdapterHolder.(item: T) -> Unit) {
        onBindHolder = { holder, position -> holder.onBind(adapterItems[position]) }
    }

    inline fun onBindItemToView(crossinline onBind: View.(item: T) -> Unit) {
        onBindHolder = { holder, position -> holder.itemView.onBind(adapterItems[position]) }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified VB : ViewBinding> onBindItemToViewBinding(crossinline onBind: VB.(item: T) -> Unit) {
        onBindHolder =
            { holder, position -> (holder as AdapterHolderVB<VB>).viewBinding.onBind(adapterItems[position]) }
    }
    //---


    fun build(): FlexibleAdapter<T> = object : FlexibleAdapter<T>(startingItems) {

        override fun getItemCount(): Int = this@AdapterBuilder.getItemCount?.invoke(this) ?: super.getItemCount()

        override fun getItemViewType(position: Int): Int =
            this@AdapterBuilder.getItemViewType?.invoke(this, position) ?: super.getItemViewType(position)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder =
            onCreateHolder(parent, viewType)

        override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
            onBindHolder(holder, position)
        }
    }
}

private typealias HolderPredicate<T> = FlexibleAdapter<T>.(position: Int) -> Boolean
private typealias HolderCreator = (parent: ViewGroup) -> AdapterHolder

@AdapterBuilderMarker
class FlexibleHolderGenerator<T> {

    private val constructors: MutableMap<HolderPredicate<T>, HolderCreator> = mutableMapOf()


    fun holderIf(
        predicate: FlexibleAdapter<T>.(position: Int) -> Boolean = { true },
        createHolder: (parent: ViewGroup) -> AdapterHolder
    ) {
        constructors[predicate] = createHolder
    }

    //onCreateHolder extensions:
    inline fun holder(noinline createHolder: (parent: ViewGroup) -> AdapterHolder) {
        holderIf({ true }, createHolder)
    }

    //onCreateHolder extensions from AdapterBuilder:
    inline fun viewIf(
        noinline shouldBeUsed: FlexibleAdapter<T>.(position: Int) -> Boolean,
        view: View
    ) {
        holderIf(shouldBeUsed) { AdapterHolder(view) }
    }

    inline fun view(view: View) {
        viewIf({ true }, view)
    }

    inline fun viewIf(
        noinline shouldBeUsed: FlexibleAdapter<T>.(position: Int) -> Boolean,
        crossinline onCreateView: (parent: ViewGroup) -> View
    ) {
        holderIf(shouldBeUsed) { AdapterHolder(onCreateView(it)) }
    }

    inline fun view(
        crossinline onCreateView: (parent: ViewGroup) -> View
    ) {
        viewIf({ true }, onCreateView)
    }

    inline fun layoutIf(
        noinline shouldBeUsed: FlexibleAdapter<T>.(position: Int) -> Boolean,
        @LayoutRes res: Int,
        crossinline setup: View.() -> Unit = {}
    ) {
        holderIf(shouldBeUsed) {
            AdapterHolder(
                LayoutInflater.from(it.context).inflate(res, it, false)
                    .apply(setup)
            )
        }
    }

    inline fun layout(@LayoutRes res: Int, crossinline setup: View.() -> Unit = {}) {
        layoutIf({ true }, res, setup)
    }

    inline fun <reified VB : ViewBinding> viewBindingIf(
        noinline shouldBeUsed: FlexibleAdapter<T>.(position: Int) -> Boolean,
        crossinline inflate: (inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean) -> VB,
        crossinline setup: VB.() -> Unit = {}
    ) {
        holderIf(shouldBeUsed) {
            AdapterHolderVB(
                inflate(LayoutInflater.from(it.context), it, false)
                    .apply(setup)
            )
        }
    }

    inline fun <reified VB : ViewBinding> viewBinding(
        crossinline inflate: (inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean) -> VB,
        crossinline setup: VB.() -> Unit = {}
    ) {
        viewBindingIf({ true }, inflate, setup)
    }
    //---


    fun integrateInto(builder: AdapterBuilder<T>) {
        builder.getItemViewType = { position ->
            val indexOfValidKey = constructors.keys.indexOfFirst { it(this, position) }
            if (indexOfValidKey >= 0) {
                indexOfValidKey + 1 //avoid zero-indexing since default value for viewType in RecyclerView is 0.
            } else {
                throw IllegalArgumentException("FlexibleHolderGenerator: no valid generator found for itemViewType implementation.")
            }
        }
        builder.onCreateHolder = { parent, viewType ->
            val constructor = constructors.values.toList()[viewType - 1] //since using one-based indexing
            constructor(parent)
        }
    }
}

inline fun <reified T> adapter(setup: AdapterBuilder<T>.() -> Unit): FlexibleAdapter<T> =
    AdapterBuilder<T>().apply(setup).build()

inline fun <reified T> adapter(items: MutableList<T>, setup: AdapterBuilder<T>.() -> Unit): FlexibleAdapter<T> =
    adapter { startingItems = items; setup() }

@JvmName("staticAdapter")
inline fun <reified T> adapter(items: List<T>, setup: AdapterBuilder<T>.() -> Unit): FlexibleAdapter<T> =
    adapter { startingItems = items.toMutableList(); setup() }