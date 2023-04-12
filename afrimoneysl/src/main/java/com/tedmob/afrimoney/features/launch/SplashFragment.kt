package com.tedmob.afrimoney.features.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.BidiFormatter
import androidx.lifecycle.lifecycleScope
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : BaseVBFragment<FragmentSplashBinding>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentSplashBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //bindAvailableUpdate()
        //bindRedirection()

        withVBAvailable {
            val bidi = BidiFormatter.getInstance()
            version.text = String.format(
                getString(R.string.version_name),
                bidi.unicodeWrap(getString(R.string.app_version_name))
            )

            prepareViewsForAnimation()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.prepareViewsForAnimation()

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            animate()
            start()
        }
    }

    private inline fun FragmentSplashBinding.prepareViewsForAnimation() {
        topLogo.alpha = 0f
        appLogo.alpha = 0f
        version.alpha = 0f
        copyright.alpha = 0f
    }


    private suspend fun animate() {
        withVBAvailable {
            delay(200L)

            topLogo.animate()
                .alpha(1f)
                .setDuration(500L)
                .start()

            delay(200L)

            appLogo.animate()
                .alpha(1f)
                .setDuration(500L)
                .start()

            delay(1_000L)

            version.animate()
                .alpha(1f)
                .setDuration(200L)
                .start()
            copyright.animate()
                .alpha(1f)
                .setDuration(200L)
                .start()

            delay(500L)
        }
    }

    private suspend inline fun start() {
        //viewModel.redirectToAppropriateSection()
    }


    /*private fun bindRedirection() {
        viewModel.navigationAction.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding?.progressBar?.isVisible = true
                }
                is Resource.Error -> {
                    binding?.progressBar?.isVisible = false
                    MaterialAlertDialogBuilder(requireActivity())
                        .setMessage(it.message)
                        .setPositiveButton(R.string.retry) { _, _ -> it.action?.invoke() }
                        .setNeutralButton(R.string.login) { _, _ -> viewModel.redirectToLogin() }
                        .setNegativeButton(R.string.close) { _, _ -> activity?.finish() }
                        .setCancelable(false)
                        .show()
                }
                is Resource.Success -> {
                    binding?.progressBar?.isVisible = false
                    // findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSetPinFragment())
                    findNavController().navigateWithAction(it.data, activity)
                }
            }
        }
    }*/


    /*private fun bindAvailableUpdate() {
        viewModel.updateAvailable.observe(viewLifecycleOwner) {
            requireBinding().progressBar.isVisible = false
            if (it.isForced) {
                showUpdateNeeded(it)
            } else {
                //showUpdateOptional(it)
                viewModel.redirectToAppropriateSection()
            }
        }
    }*/

    /*private inline fun showUpdateNeeded(data: AppUpdate) {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(data.title)
                .setMessage(data.message)
                .setPositiveButton(R.string.update) { _, _ ->
                    redirectToAppOnStore(BuildConfig.APPLICATION_ID)
                    activity?.finish()
                }
                .setNeutralButton(R.string.close) { _, _ ->
                    activity?.finish()
                }
                .setCancelable(false)
                .show()
        }
    }*/

    /*private inline fun showUpdateOptional(data: AppUpdate) {
        //fixme proper title and message
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(data.title)
                .setMessage(data.message)
                .setPositiveButton(R.string.update) { _, _ ->
                    redirectToAppOnStore(BuildConfig.APPLICATION_ID)
                    activity?.finish()
                }
                .setNegativeButton(R.string.proceed) { _, _ ->
                    viewModel.redirectToAppropriateSection()
                }
                .setNeutralButton(R.string.close) { _, _ ->
                    activity?.finish()
                }
                .setCancelable(false)
                .show()
        }
    }*/

    /*private inline fun redirectToAppOnStore(appPackageName: String) {
        //TODO detect if Google/Huawei? Use handler?
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (ex: ActivityNotFoundException) {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setMessage("Your device cannot access the Google Play Store.")
                        .setPositiveButton(R.string.close) { _, _ ->
                            activity?.finish()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
    }*/
}
