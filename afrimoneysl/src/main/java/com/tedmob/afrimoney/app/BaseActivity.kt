package com.tedmob.afrimoney.app

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.tedmob.afrimoney.BuildConfig
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.ui.blocks.LoadingLayout
import com.tedmob.afrimoney.ui.blocks.LoadingView
import com.tedmob.afrimoney.ui.blocks.ToolbarLayout
import com.tedmob.afrimoney.util.alert.showMaterialAlert
import com.tedmob.afrimoney.util.dialog.DialogUtils
import com.tedmob.afrimoney.util.locale.LocaleHelper

abstract class BaseActivity : AppCompatActivity() {

    private var toolbarLayout: ToolbarLayout? = null
    protected var toolbar: Toolbar? = null
        get() = toolbarLayout?.toolbar
        private set
    private var loadingLayout: LoadingLayout? = null
    protected var loadingView: LoadingView? = null
        get() = loadingLayout?.loadingView
        private set
    private var progressDialog: ProgressDialog? = null


    override fun attachBaseContext(base: Context) {
        if (BuildConfig.MULTI_LANG) {
            val localeHelper = LocaleHelper(base)
            val context = localeHelper.createConfigurationContext()
            super.attachBaseContext(context)
            applyOverrideConfiguration(context.resources.configuration)//activity's version is never called by system for correct config for appcompat 1.2.0 and up
        } else {
            super.attachBaseContext(base)
            applyOverrideConfiguration(base.resources.configuration)//activity's version is never called by system for appcompat 1.2.0 and up
        }
    }


    inline fun setContentView(
        view: View,
        wrapLoading: Boolean,
        wrapToolbar: Boolean,
        toolbarLayoutId: Int
    ) {
        setContentView(wrap(view, wrapLoading, wrapToolbar, toolbarLayoutId, R.id.toolbar))
    }

    inline fun setContentView(
        view: View,
        wrapLoading: Boolean,
        toolbarLayout: View?
    ) {
        setContentView(wrap(view, wrapLoading, toolbarLayout, R.id.toolbar))
    }

    inline fun setContentView(
        layoutResId: Int,
        wrapLoading: Boolean,
        wrapToolbar: Boolean,
        toolbarLayoutId: Int
    ) {
        val view = layoutInflater.inflate(layoutResId, null)
        setContentView(wrap(view, wrapLoading, wrapToolbar, toolbarLayoutId, R.id.toolbar))
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(layoutResID, true, true, R.layout.toolbar_default)
    }

    fun wrap(
        view: View,
        wrapLoading: Boolean,
        wrapToolbar: Boolean,
        toolbarLayoutId: Int,
        toolbarId: Int
    ): View {
        var v = view
        if (wrapLoading) {
            loadingLayout = LoadingLayout(this)
            loadingLayout?.let {
                it.addView(v, 0)
                v = it
            }
        }

        if (wrapToolbar) {
            toolbarLayout = ToolbarLayout(this, toolbarLayoutId, toolbarId)
            toolbarLayout?.let {
                it.addView(
                    v,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                setSupportActionBar(toolbar)
                v = it
            }
        }

        return v
    }

    /**
     * Created for [BaseVBActivity].
     */
    fun wrap(
        view: View,
        wrapLoading: Boolean,
        toolbarLayoutView: View?,
        toolbarId: Int
    ): View {
        var v = view
        if (wrapLoading) {
            loadingLayout = LoadingLayout(this)
            loadingLayout?.let {
                it.addView(v, 0)
                v = it
            }
        }

        if (toolbarLayoutView != null) {
            toolbarLayout = ToolbarLayout(this, toolbarLayoutView, toolbarId)
            toolbarLayout?.let {
                it.addView(
                    v,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                setSupportActionBar(toolbar)
                v = it
            }
        }

        return v
    }

    override fun onSupportNavigateUp(): Boolean {
        finishAfterTransition()
        return true
    }

    fun showInlineLoading() {
        loadingLayout?.showLoadingView()
        loadingView?.loading(true)
    }

    fun showInlineMessageWithAction(message: CharSequence, actionName: String, action: (() -> Unit)?) {
        loadingLayout?.showLoadingView()
        loadingView?.loading(false)
            ?.message(message)
            ?.displayButton(true)
            ?.buttonText(actionName)
            ?.buttonClickListener(View.OnClickListener { action?.invoke() })
    }


    fun showContent() {
        loadingLayout?.showContent()
    }

    fun showProgressDialog(message: String) {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
        progressDialog = DialogUtils.showLoadingDialog(this, message)
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
}
