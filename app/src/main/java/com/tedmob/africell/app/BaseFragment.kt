package com.tedmob.africell.app

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.tedmob.africell.R
import com.tedmob.africell.features.authentication.AuthenticationActivity
import com.tedmob.africell.features.home.ImageViewModel
import com.tedmob.africell.ui.blocks.LoadingLayout
import com.tedmob.africell.ui.blocks.LoadingView
import com.tedmob.africell.ui.blocks.ToolbarLayout
import com.tedmob.africell.ui.hideKeyboard
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceWithoutProgress
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.DialogUtils
import com.tedmob.africell.util.intents.dial
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    protected val actionbar: ActionBar? get() = getActionBar()
    private var toolbarLayout: ToolbarLayout? = null
    protected var toolbar: Toolbar? = null
        get() = toolbarLayout?.toolbar
        private set
    private var loadingLayout: LoadingLayout? = null
    protected var loadingView: LoadingView? = null
        get() = loadingLayout?.loadingView

    protected var progressDialog: ProgressDialog? = null

    private var rxDisposables: CompositeDisposable? = null
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    /**
     * Toolbar might be shared between multiple fragments. Configure it here.
     */
    open fun configureToolbar() {}
    private val imageViewModel by provideViewModel<ImageViewModel> { viewModelFactory }
    fun setupImageBanner(toolbar: SimpleDraweeView, imageType: String?, pageName: String?) {
        imageViewModel.getImages(imageType, pageName)
        observeResourceWithoutProgress(imageViewModel.imagesData, {
            toolbar.setImageURI(it.getOrNull(0))
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(
                FirebaseAnalytics.Param.SCREEN_NAME to this::class.java.simpleName,
                FirebaseAnalytics.Param.SCREEN_CLASS to this::class.java.simpleName
            )
        )
        //fixme Manifest: can make "google_analytics_automatic_screen_reporting_enabled" false

        configureToolbar()
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
                (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
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
        if (rxDisposables == null) {
            rxDisposables = CompositeDisposable()
        }
        rxDisposables?.add(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        rxDisposables?.dispose()
    }


    fun showContent() {
        loadingLayout?.showContent()
    }

    fun showInlineLoading() {
        loadingLayout?.showLoadingView()
        loadingView?.loading(true)
    }

    fun showInlineMessage(message: String) {
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

    fun showInlineMessageWithAction(message: String, actionName: String, action: (() -> Unit)?) {
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

    fun showMessage(message: String) {
        view?.let { view ->
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }

    fun showMessageWithAction(message: String, actionName: String, action: (() -> Unit)?) {
        view?.let { view ->
            Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionName) { action?.invoke() }
                .show()
        }
    }

    private fun getActionBar(): ActionBar? {
        return activity?.let {
            (it as? AppCompatActivity)?.supportActionBar
        }
    }

    fun showMessageDialog(
        message: String,
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
        message: String,
        buttonText: String = getString(R.string.close),
        callback: (() -> Unit)? = null
    ) {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(buttonText) { _, _ -> callback?.invoke() }
                .show()
        }
    }

    fun showLoginMessage() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.login_first))
            .setCancelable(true)
            .setPositiveButton(R.string.login) { dialog, which ->
                redirectToLogin()
            }
            .setNegativeButton(R.string.close) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    fun redirectToLogin() {
        activity?.startActivity(Intent(activity, AuthenticationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    fun promptCallUs(phones: List<String>) {

        val dialog = AlertDialog.Builder(requireContext())
            .setItems(
                phones.toTypedArray()
            ) { dialog, which -> dial(phones[which]) }
            .setNegativeButton(R.string.close, null)
            .create()
        dialog.show()
    }

    fun selectPhoneNumber(allPhones: List<String>, action: ((String) -> Unit)) {
        val phones= allPhones.distinct()
        if(phones.size<=1){
            action.invoke(phones[0])
        }else {
            val dialog = AlertDialog.Builder(requireContext())
                .setItems(
                    phones.toTypedArray()
                ) { dialog, which ->
                    action.invoke(phones[which])
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.close, null)
                .create()
            dialog.show()
        }
    }

    override fun onDestroyView() {
        activity?.hideKeyboard()
        super.onDestroyView()
    }
}
