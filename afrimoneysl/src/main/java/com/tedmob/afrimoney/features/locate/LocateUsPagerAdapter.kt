package com.tedmob.afrimoney.features.locate

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tedmob.afrimoney.R
import java.util.*

class LocateUsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var mode: LocateUsViewModel.Mode = LocateUsViewModel.Mode.Map


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> when (mode) {
            LocateUsViewModel.Mode.Map -> LocateMapFragment.newInstance(LocateUsViewModel.Section.Agents)
            LocateUsViewModel.Mode.List -> LocateListFragment.newInstance(LocateUsViewModel.Section.Agents)
        }
        1 -> when (mode) {
            LocateUsViewModel.Mode.Map -> LocateMapFragment.newInstance(LocateUsViewModel.Section.Stores)
            LocateUsViewModel.Mode.List -> LocateListFragment.newInstance(LocateUsViewModel.Section.Stores)
        }
        else -> Fragment()
    }

    fun getTitle(context: Context, position: Int) = when (position) {
        0 -> context.getString(R.string.our_agents).uppercase(Locale.getDefault())
        1 -> context.getString(R.string.our_stores).uppercase(Locale.getDefault())
        else -> ""
    }

    fun changeMode(newMode: LocateUsViewModel.Mode) {
        if (mode != newMode) {
            mode = newMode
            notifyDataSetChanged()
        }
    }
}