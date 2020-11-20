package com.tedmob.africell.features.launch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.BidiFormatter
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.springAnimationOf
import androidx.dynamicanimation.animation.withSpringForceProperties
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tedmob.africell.BuildConfig
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class SplashFragment : BaseFragment() {

    companion object {
        private const val DELAY: Long = 1_500L
        private const val ANIMATION_START_DELAY: Long = 200L
    }


    private val handler: Handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var sessionRepo: SessionRepository

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<SplashViewModel> { viewModelFactory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_splash, 0, false)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindRedirection()

        val bidi = BidiFormatter.getInstance()
        version.text = String.format(
            getString(R.string.version_name),
            bidi.unicodeWrap(BuildConfig.VERSION_NAME)
        )

        appLogo.translationY = -250f
    }

    override fun onResume() {
        super.onResume()
        appLogo.translationY = -250f
        handler.postDelayed(animationRunnable, ANIMATION_START_DELAY)
        handler.postDelayed(navigateRunnable, DELAY)
    }


    private val navigateRunnable: Runnable = Runnable {
        viewModel.redirectToAppropriateSection()
    }

    private val animationRunnable: Runnable = Runnable {
        val animation = springAnimationOf(
            setter = appLogo::setTranslationY,
            getter = appLogo::getTranslationY
        ).withSpringForceProperties {
            stiffness = SpringForce.STIFFNESS_LOW
            dampingRatio = 0.2f
            finalPosition = 0f
        }
        animation.start()
    }


    private fun bindRedirection() {
        viewModel.navigationAction.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    MaterialAlertDialogBuilder(requireActivity())
                        .setMessage(it.message)
                        .setPositiveButton(R.string.retry) { _, _ -> viewModel.redirectToAppropriateSection() }
                        .setNegativeButton(R.string.close) { _, _ -> activity?.finish() }
                        .setCancelable(false)
                        .show()
                }
                is Resource.Success -> findNavController().navigateWithAction(it.data, activity)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(animationRunnable)
        handler.removeCallbacks(navigateRunnable)
    }

    override fun onDestroyView() {//just in case onPause was skipped, which could happen in low power scenarios
        super.onDestroyView()
        handler.removeCallbacks(animationRunnable)
        handler.removeCallbacks(navigateRunnable)
    }
}
