package com.africell.africell.features.help

import android.os.Bundle
import com.africell.africell.app.viewbinding.BaseVBActivity
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.Resource
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.ActivityHelpBinding
import com.africell.africell.features.home.ImageViewModel
import com.africell.africell.ui.viewmodel.observeNotNull
import com.africell.africell.ui.viewmodel.provideViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HelpActivity : BaseVBActivity<ActivityHelpBinding>() {

    private val viewModel by provideViewModel<ImageViewModel>()

    @Inject
    lateinit var sessionRepository: SessionRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(ActivityHelpBinding::inflate, false)
        getImage()
        sessionRepository.showHelp = false
    }


/*
    companion object {
        val helpList = listOf(Help(R.drawable.africell_bgd_login1, R.string.help_message_1))
    }


    fun setupUI() {
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val adapter = HelpAdapter(helpList)
        viewPager.adapter = adapter
        nextBtn.visibility = if (sessionRepository.showHelp) View.VISIBLE else View.GONE
        nextBtn.setOnClickListener {
            val position = viewPager.currentItem
            if (position == adapter.itemCount - 1) {
                activity?.finish()
            } else {
                viewPager.currentItem = position + 1
            }
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val item= helpList[position]
                layout.background = ContextCompat.getDrawable(this@HelpActivity, item.image)

                nextBtn.text = if (position == adapter.itemCount - 1) {
                    getString(R.string.get_started)
                } else {
                    getString(R.string.next)
                }
                super.onPageSelected(position)
            }
        })
        pageIndicator.setViewPager(viewPager)
    }
*/

    fun getImage() {
        withVBAvailable {
            nextBtn.setOnClickListener {
                activity.finish()
            }
        }
        viewModel.getImages(ApiContract.Params.BACKGROUND, ApiContract.ImagePageName.LAUNCH)
        observeNotNull(viewModel.imagesData) { resource ->
            withVBAvailable {
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        val data = resource.data
                        imageView.setImageURI(data.getOrNull(0))
                    }
                    is Resource.Error -> {
                        hideProgressDialog()
                        showMessage(resource.message)
                    }
                }
            }
        }

    }

}
