package com.tedmob.africell.features.home

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseActivity
import com.tedmob.africell.util.navigation.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BaseActivity() {

    private val bottomNavFragmentIds: List<Int> by lazy {
        val list = mutableListOf<Int>()
        bottomNavigationView.menu.let { menu ->
            val defaultList = (0 until menu.size()).map { menu.getItem(it).itemId }
            list.addAll(defaultList)
        }
        list.add(R.id.locationListFragment)
        list
    }


    private lateinit var appBarConfiguration: AppBarConfiguration

    private val sideMenuIds: List<Int> by lazy {
        navigationView.menu.let { menu ->
            (0 until menu.size()).map { menu.getItem(it).itemId }
        }
    }

    val topLevelDestinations: Set<Int> by lazy {
        val list = mutableListOf<Int>()
        list.addAll(bottomNavFragmentIds)
        list.addAll(sideMenuIds)
        HashSet<Int>(list)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main, false, false, 0)
        /*   setSupportActionBar(drawerToolbar)
        */
        appBarConfiguration = drawerLayout.getAppBarConfigWithRoot(topLevelDestinations)
        setupNavigation()
        setupBottomNavigationStyle()
    }


    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(
        findNavController(R.id.nav_host_main),
        appBarConfiguration
    ) || findNavController(R.id.nav_host_main).navigateUp()

    private fun setupNavigation() {
        //    navigationView.itemIconTintList = null

        findNavController(R.id.nav_host_main).let {
//            setupActionBarWithNavController(it, drawerLayout)
            navigationView.setupWithNavController(
                it,
                customHasPriority = true,
                customNavigationListener = { item ->
                    when (item.itemId) {
                        //...

                        else -> false
                    }
                }
            )


            val backPressedCallback = onBackPressedDispatcher.addCallback(this, enabled = false) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                }

                override fun onDrawerClosed(drawerView: View) {
                    if (drawerView.id == R.id.navigationView) {
                        backPressedCallback.isEnabled = false
                    }
                }

                override fun onDrawerOpened(drawerView: View) {
                    if (drawerView.id == R.id.navigationView) {
                        backPressedCallback.isEnabled = true
                    }
                }
            })

            navigationView.setupWithNavController(
                it,
                customHasPriority = true,
                customNavigationListener = { item ->
                    when (item.itemId) {
                        //...
                        else -> false
                    }
                }
            )

            bottomNavigationView.setupWithNavController(it)
            //preventing reselection by passing empty listener; a null listener will result in onItemSelected listener to be called on reselection.
            bottomNavigationView.setOnNavigationItemReselectedListener { }
            it.addOnDestinationChangedListener { _, destination, _ ->
                Timber.d("Navigate to %s", destination.label)
                bottomNavigationView.visibility =
                    if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                if (bottomNavFragmentIds.contains(destination.id)) {
                    if (destination.id == R.id.customerCareFragment) {
                        customerCareTxt.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            R.mipmap.tab_customer_care_selected, 0, 0
                        )
                    } else {
                        customerCareTxt.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            R.mipmap.tab_customer_care,
                            0,
                            0
                        )

                    }
                }
            }
        }
    }

    private fun setupBottomNavigationStyle() {
        bottomNavigationView.itemIconTintList = null
     /*   val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        // make second item bigger
        val iconView =
            menuView.getChildAt(2)?.findViewById<View>(com.google.android.material.R.id.icon)
        val size = resources.getDimensionPixelSize(R.dimen.spacing_large)
        val padding = resources.getDimensionPixelSize(R.dimen.spacing_medium)
        iconView?.setPadding(0, 0, 0, padding)

        val layoutParams = iconView?.layoutParams
        val displayMetrics = resources.displayMetrics
        // set your height here
        // set your height here
        layoutParams?.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45f, displayMetrics).toInt()
        // set your width here
        // set your width here
        layoutParams?.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45f, displayMetrics).toInt()
        iconView?.layoutParams = layoutParams*/
    }

    private fun DrawerLayout.getAppBarConfigWithRoot(topLevelDestinations: Set<Int>): AppBarConfiguration {
        return AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(this).build()
    }

}