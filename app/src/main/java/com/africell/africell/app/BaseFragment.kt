package com.africell.africell.app

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.africell.africell.R
import com.africell.africell.features.authentication.AuthenticationActivity
import com.africell.africell.features.home.ImageViewModel
import com.africell.africell.ui.blocks.LoadingLayout
import com.africell.africell.ui.blocks.LoadingView
import com.africell.africell.ui.blocks.ToolbarLayout
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.ViewModelFactory
import com.africell.africell.ui.viewmodel.observeResourceWithoutProgress
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.DialogUtils
import com.africell.africell.util.intents.dial
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
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

    fun showInlineImage(@DrawableRes resId: Int,message: String) {
        loadingLayout?.showLoadingView()
        loadingView?.loading(false)
            ?.message(message)
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
            val dialogView = View.inflate(activity, R.layout.dialog_custom, null)

            val titleTxt = dialogView.findViewById<TextView>(R.id.titleTxt)
            val messageTxt = dialogView.findViewById<TextView>(R.id.messageTxt)
            val dismissButton = dialogView.findViewById<TextView>(R.id.dismiss)
            val actionBtn = dialogView.findViewById<TextView>(R.id.actionBtn)
            val image = dialogView.findViewById<ImageView>(R.id.iconDialog)

            actionBtn.visibility = View.GONE
            val builder = MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
            messageTxt.text = message
            image.setImageResource(R.drawable.failed_icon)
            val alertDialog = builder.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dismissButton.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    fun showInfoMessage(message: String) {
        view?.let { view ->
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }
    fun showMessageWithAction(message: String, actionName: String, action: (() -> Unit)?) {
        view?.let { view ->
            val dialogView = View.inflate(activity, R.layout.dialog_custom, null)

            val titleTxt = dialogView.findViewById<TextView>(R.id.titleTxt)
            val messageTxt = dialogView.findViewById<TextView>(R.id.messageTxt)
            val dismissButton = dialogView.findViewById<TextView>(R.id.dismiss)
            val actionBtn = dialogView.findViewById<TextView>(R.id.actionBtn)
            val image = dialogView.findViewById<ImageView>(R.id.iconDialog)

            actionBtn.visibility = View.VISIBLE
            actionBtn.text = actionName
            val builder = MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
            messageTxt.text = message
            titleTxt.text = getString(R.string.failed)

            image.setImageResource(R.drawable.failed_icon)
            val alertDialog = builder.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dismissButton.setOnClickListener {
                alertDialog.dismiss()
            }
            actionBtn.setOnClickListener {
                action?.invoke()
                alertDialog.dismiss()
            }
        }
    }

    private fun getActionBar(): ActionBar? {
        return activity?.let {
            (it as? AppCompatActivity)?.supportActionBar
        }
    }


    fun showMaterialMessageDialog(
        title: String,
        message: String,
        buttonText: String = getString(R.string.close),
        callback: (() -> Unit)? = null
    ) {
        val dialogView = View.inflate(activity, R.layout.dialog_custom, null)

        val titleTxt = dialogView.findViewById<TextView>(R.id.titleTxt)
        val image = dialogView.findViewById<ImageView>(R.id.iconDialog)
        val messageTxt = dialogView.findViewById<TextView>(R.id.messageTxt)
        val dismissButton = dialogView.findViewById<TextView>(R.id.dismiss)
        val actionBtn = dialogView.findViewById<TextView>(R.id.actionBtn)
        actionBtn.visibility = View.GONE
        actionBtn.text = buttonText
        titleTxt.text = title
        image.setImageResource(R.drawable.success_icon)
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
        messageTxt.text = message

        val alertDialog = builder.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dismissButton.setOnClickListener {
            callback?.invoke()
            alertDialog.dismiss()
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
        val phones = allPhones.distinct()
        if (phones.size <= 1) {
            action.invoke(phones[0])
        } else {
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
