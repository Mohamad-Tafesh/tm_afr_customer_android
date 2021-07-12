package com.africell.africell.features.help

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.africell.africell.R
import com.africell.africell.app.BaseActivity
import com.africell.africell.data.repository.domain.SessionRepository
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.item_help.view.*
import javax.inject.Inject

class HelpActivity : BaseActivity() {
    @Inject
    lateinit var sessionRepository: SessionRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help, false, false, 0)
        setupUI()
        sessionRepository.showHelp = false
    }
    companion object {

        val helpList = listOf(
            Help(R.drawable.africell_bgd_login1, R.string.help_message_1),
            Help(R.drawable.africell_bgd_login2, R.string.help_message_2)
        )
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
                    "Let's Get Started"
                } else {
                    "Next"
                }
                super.onPageSelected(position)
            }
        })
        pageIndicator.setViewPager(viewPager)
    }

}
