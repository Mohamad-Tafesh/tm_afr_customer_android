package com.tedmob.afrimoney.app

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.data.analytics.AnalyticsHandler
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.databinding.DialogTransactionResultBinding
import com.tedmob.afrimoney.ui.blocks.LoadingLayout
import com.tedmob.afrimoney.ui.blocks.LoadingView
import com.tedmob.afrimoney.ui.blocks.ToolbarLayout
import com.tedmob.afrimoney.util.alert.showMaterialAlert
import com.tedmob.afrimoney.util.dialog.DialogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var analytics: AnalyticsHandler

    private var toolbarLayout: ToolbarLayout? = null
    protected var toolbar: Toolbar? = null
        get() = toolbarLayout?.toolbar
        private set
    private var loadingLayout: LoadingLayout? = null
    protected var loadingView: LoadingView? = null
        get() = loadingLayout?.loadingView

    protected var progressDialog: ProgressDialog? = null

    private val rxDisposables: CompositeDisposable by lazy { CompositeDisposable() }

    @Inject
    lateinit var session: SessionRepository


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            analytics.logScreenViewStart(this::class.java.simpleName, this::class.java.simpleName)
            //fixme Manifest: can make "google_analytics_automatic_screen_reporting_enabled" false
        } catch (e: Exception) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            analytics.logScreenViewEnd(this::class.java.simpleName)
        } catch (e: Exception) {
        }
    }

    fun wrap(context: Context, view: View): View {
        loadingLayout = LoadingLayout(context)
        loadingLayout?.let {
            it.addView(view, 0)
            return it
        }
        throw IllegalStateException("LoadingLayout was not created")
    }

    fun wrap(
        context: Context,
        view: View,
        wrapLoading: Boolean,
        wrapToolbar: Boolean,
        toolbarLayoutId: Int,
        toolbarId: Int
    ): View {
        var v = view
        if (wrapLoading) {
            loadingLayout = LoadingLayout(context)
            loadingLayout?.let {
                it.addView(v, 0)
                v = it
            }
        }

        if (wrapToolbar) {
            toolbarLayout = ToolbarLayout(context, toolbarLayoutId, toolbarId)
            toolbarLayout?.let {
                it.addView(
                    v,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                )
                v = it
            }
        }

        return v
    }

    /**
     * Created for [BaseVBFragment].
     */
    fun wrap(
        context: Context,
        view: View,
        wrapLoading: Boolean,
        toolbarLayoutView: View?,
        toolbarId: Int = R.id.toolbar
    ): View {
        var v = view
        if (wrapLoading) {
            loadingLayout = LoadingLayout(context)
            loadingLayout?.let {
                it.addView(v, 0)
                v = it
            }
        }

        if (toolbarLayoutView != null) {
            toolbarLayout = ToolbarLayout(context, toolbarLayoutView, toolbarId)
            toolbarLayout?.let {
                it.addView(
                    v,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                )
                v = it
            }
        }

        return v
    }

    fun wrap(context: Context, view: View, toolbarLayoutId: Int = 0, wrapLoading: Boolean = false): View {
        return wrap(
            context, view, wrapLoading, toolbarLayoutId != 0, toolbarLayoutId,
            R.id.toolbar
        )
    }

    fun wrap(context: Context, layoutResId: Int, toolbarLayoutId: Int = 0, wrapLoading: Boolean = false): View {
        val view = LayoutInflater.from(context).inflate(layoutResId, LinearLayout(context), false)
        return wrap(context, view, toolbarLayoutId, wrapLoading)
    }

    protected fun addDisposable(disposable: Disposable) {
        rxDisposables.add(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        rxDisposables.dispose()
    }


    fun showContent() {
        loadingLayout?.showContent()
    }

    fun showInlineLoading() {
        loadingLayout?.showLoadingView()
        loadingView?.loading(true)
    }

    fun showInlineMessage(message: CharSequence) {
        loadingLayout?.showLoadingView()
        loadingView?.loading(false)
            ?.message(message)
            ?.displayImage(false)
            ?.displayButton(false)
    }

    fun showInlineImage(@DrawableRes resId: Int) {
        loadingLayout?.showLoadingView()
        loadingView?.loading(false)
            ?.message("")
            ?.imageResource(resId)
            ?.displayImage(true)
            ?.displayButton(false)
    }

    fun showInlineMessageWithAction(message: CharSequence, actionName: String, action: (() -> Unit)?) {
        loadingLayout?.showLoadingView()
        loadingView?.loading(false)
            ?.message(message)
            ?.displayButton(true)
            ?.buttonText(actionName)
            ?.buttonClickListener(View.OnClickListener { action?.invoke() })
    }

    fun showProgressDialog(message: String) {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
        activity?.let { progressDialog = DialogUtils.showLoadingDialog(it, message) }
    }

    fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    fun showMessage(message: CharSequence) {

        showMaterialAlert {
            setMessage(message)
            setPositiveButton(R.string.close, null)
        }

    }

    fun showMessageWithAction(message: CharSequence, actionName: String, action: (() -> Unit)?) {

        showMaterialAlert {
            setMessage(message)
            setPositiveButton(actionName) { _, _ -> action?.invoke() }
            setNegativeButton(R.string.dp_s__cancel, null)
        }
    }


    private fun getActionBar(): ActionBar? {
        return activity?.let {
            (it as? AppCompatActivity)?.supportActionBar
        }
    }

    fun showMessageDialog(
        message: CharSequence,
        buttonText: String = getString(R.string.close),
        callback: (() -> Unit)? = null
    ) {
        activity?.let {
            AlertDialog.Builder(it)
                .setMessage(message)
                .setPositiveButton(buttonText) { _, _ -> callback?.invoke() }
                .show()
        }
    }

    fun showMaterialMessageDialog(
        message: CharSequence,
        buttonText: String = getString(R.string.close),
        callback: (() -> Unit)? = null
    ) {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(message)
                .setPositiveButton(buttonText) { _, _ -> callback?.invoke() }
                .show()
        }
    }

    fun showMaterialMessageDialog(
        message: CharSequence,
        positiveButtonText: String = getString(R.string.retry),
        negativeButtonText: String = getString(R.string.close),
        callback: (() -> Unit)? = null
    ) {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { _, _ -> callback?.invoke() }
                .setNegativeButton(negativeButtonText, null)
                .show()
        }
    }
}
