package com.africell.africell.features.launch

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.BidiFormatter
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.springAnimationOf
import androidx.dynamicanimation.animation.withSpringForceProperties
import androidx.navigation.fragment.findNavController
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.Resource
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentSplashBinding
import com.africell.africell.ui.viewmodel.provideViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseVBFragment<FragmentSplashBinding>() {

    companion object {
        private const val DELAY: Long = 1_500L
        private const val ANIMATION_START_DELAY: Long = 200L
    }


    private val handler: Handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var sessionRepo: SessionRepository


    private val viewModel by provideViewModel<SplashViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentSplashBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindRedirection()

        withVBAvailable {
            val bidi = BidiFormatter.getInstance()
            version.text = String.format(
                getString(R.string.version_name),
                bidi.unicodeWrap(BuildConfig.VERSION_NAME)
            )

            appLogo.translationY = -250f
        }
    }

    override fun onResume() {
        super.onResume()
        withVBAvailable {
            appLogo.translationY = -250f
            handler.postDelayed(animationRunnable, ANIMATION_START_DELAY)
            handler.postDelayed(navigateRunnable, DELAY)
        }
    }


    private val navigateRunnable: Runnable = Runnable {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            OneSignal.getDeviceState()?.areNotificationsEnabled() == false &&
            !sessionRepo.hasRefusedNotificationsPermission
        ) {
            promptForPushPermission(
                afterPrompt = {
                    viewModel.redirectToAppropriateSection()
                }
            )
        } else {
            viewModel.redirectToAppropriateSection()
        }
    }

    private val animationRunnable: Runnable = Runnable {
        withVBAvailable {
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


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun promptForPushPermission(
        afterPrompt: (accepted: Boolean) -> Unit,
    ) {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Notifications Permission")
                .setMessage("Enable notifications to get the latest news and updates instantly.\nTo enable it later, you can go to the Settings section and turn on Push Notifications.")
                .setPositiveButton("Allow") { _, _ ->
                    OneSignal.promptForPushNotifications(true) {
                        afterPrompt(it)
                    }
                }
                .setNegativeButton("Not now") { _, _ ->
                    sessionRepo.hasRefusedNotificationsPermission = true
                    afterPrompt(false)
                }
                .show()
        }
    }
}
