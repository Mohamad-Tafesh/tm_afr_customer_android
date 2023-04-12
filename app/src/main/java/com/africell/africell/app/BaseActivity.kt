package com.africell.africell.app

import android.app.ProgressDialog
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.ui.blocks.LoadingLayout
import com.africell.africell.ui.blocks.LoadingView
import com.africell.africell.ui.blocks.ToolbarLayout
import com.africell.africell.util.DialogUtils
import com.africell.africell.util.locale.LocaleHelper
import com.google.android.material.snackbar.Snackbar

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
            super.attachBaseContext(localeHelper.createConfigurationContext())
        } else {
            super.attachBaseContext(base)
        }
    }

    //fixme appcompat 1.1.0 has a bug; it replaces configuration with a new one because of night mode
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    protected val activity: BaseActivity
        get() = this

    fun setContentView(
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

    fun setContentView(
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
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    fun showMessageWithAction(message: CharSequence, actionName: String, action: (() -> Unit)?) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionName) { action?.invoke() }
            .show()
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
