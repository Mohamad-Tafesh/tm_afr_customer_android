package com.africell.africell.features.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBActivity
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.ActivityMainBinding
import com.africell.africell.features.authentication.AuthenticationActivity
import com.africell.africell.ui.viewmodel.observe
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.navigation.setupWithNavController
import com.tedmob.afrimoney.data.entity.AfricellDestination
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.features.newhome.AfrimoneyActivity
import com.tedmob.afrimoney.features.newhome.AfrimoneyRegistrationActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseVBActivity<ActivityMainBinding>() {

    private val viewModel by provideViewModel<ActivityViewModel>()

    private val bottomNavFragmentIds: List<Int> by lazy {
        val list = mutableListOf<Int>()
        requireBinding().bottomNavigationView.menu.let { menu ->
            val defaultList = (0 until menu.size()).map { menu.getItem(it).itemId }
            list.addAll(defaultList)
        }
        list.add(R.id.locationListFragment)
        list
    }


    private lateinit var appBarConfiguration: AppBarConfiguration

    private val sideMenuIds: List<Int> by lazy {
        requireBinding().navigationView.menu.let { menu ->
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
        setContent(ActivityMainBinding::inflate, false)
        withVBAvailable {
            appBarConfiguration = drawerLayout.getAppBarConfigWithRoot(topLevelDestinations)
            setupNavigation()
            setupBottomNavigationStyle()
        }

        observe(AfricellDestination.destination) {
            it?.let {
                onNavDestinationSelected(it, findNavController(R.id.nav_host_main))
                withVBAvailable {
                    bottomNavigationView.selectedItemId = it.itemId
                }

            }
        }
        AfricellDestination.destination.value = null

        observe(AfricellDestination.side_menu_destination) {
            it?.let {
                onNavDestinationSelected(it, findNavController(R.id.nav_host_main))
            }

        }
        AfricellDestination.side_menu_destination.value = null
    }


    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(
        findNavController(R.id.nav_host_main),
        appBarConfiguration
    ) || findNavController(R.id.nav_host_main).navigateUp()

    private fun ActivityMainBinding.setupNavigation() {
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


            val backPressedCallback = onBackPressedDispatcher.addCallback(this@MainActivity, enabled = false) {
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

            observeResource(viewModel.verified) {
                proceedWith(it)
            }



            bottomNavigationView.setupWithNavController(it)
            //preventing reselection by passing empty listener; a null listener will result in onItemSelected listener to be called on reselection.
            bottomNavigationView.setOnNavigationItemReselectedListener {

            }

            bottomNavigationView.setOnItemSelectedListener {
                if (it.itemId == R.id.afrimoneyFragment) {
                    if (session.isLoggedIn()) {
                        viewModel.verify(session.msisdnAfrimoney)
                        //viewModel.verify("2507775")//todo remove
                        //viewModel.verify("7750036")//todo remove
                        //viewModel.verify("2434373")//todo remove
                    } else showLoginMessage()

                } else {
                    onNavDestinationSelected(it, findNavController(R.id.nav_host_main))
                }
                true
            }



            it.addOnDestinationChangedListener { _, destination, _ ->
                Timber.d("Navigate to %s", destination.label)
                bottomNavigationView.visibility =
                    if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                customerCareTxt.visibility =
                    if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE

                afrimoneyImg.visibility =
                    if ((BuildConfig.FLAVOR == "sl" || BuildConfig.FLAVOR == "gambia" ) && bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                customerCareTxt.visibility =
                    if ((/*BuildConfig.FLAVOR != "sl" &&*/ BuildConfig.FLAVOR != "gambia" ) && bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                if (BuildConfig.FLAVOR == "drc") {
                    afrimoneyImg.visibility = GONE
                    customerCareTxt.visibility = GONE

                }

                if (bottomNavFragmentIds.contains(destination.id)) {
                    if (BuildConfig.FLAVOR == "sl" || BuildConfig.FLAVOR == "gambia") {
                        if (destination.id == R.id.afrimoneyFragment) {
                            afrimoneyImg.setImageResource(
                                 R.mipmap.tab_afrimoney_selected
                            )
                        } else {
                            afrimoneyImg.setImageResource(
                                R.mipmap.tab_afrimoney_not_selected
                            )

                        }
                        bottomNavigationView.menu.getItem(bottomNavigationView.menu.size() - 1).title =
                            getString(R.string.post_stores)
                    } else {
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
    }

    fun showLoginMessage() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.login_first))
            .setCancelable(false)
            .setPositiveButton(R.string.login) { dialog, which ->
                redirectToLogin()
            }
            .setNegativeButton(R.string.close) { dialog, which ->
                AfricellDestination.destination.value = binding?.bottomNavigationView?.menu?.getItem(0)
                dialog.dismiss()
            }
            .show()
    }

    fun redirectToLogin() {
        this.startActivity(Intent(activity, AuthenticationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    fun proceedWith(state: UserState) {
        when (state) {
            is UserState.NotRegistered -> {
                startActivity(Intent(this, AfrimoneyRegistrationActivity::class.java).apply {
                    putExtra("number", session.msisdnAfrimoney)
                    //putExtra("number", "2507775") //todo remove
                })
                AfricellDestination.destination.value = binding?.bottomNavigationView?.menu?.getItem(0)
            }

            is UserState.Registered -> {

                activity.startActivity(Intent(activity, AfrimoneyActivity::class.java).apply {
                    //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("number", session.msisdnAfrimoney)
                    //putExtra("number", "2434373")//todo remove
                    //putExtra("number", "2507775") //todo remove
                    //putExtra("number", "7750036") //todo remove
                    putExtra("token", session.accessToken)

                })

                AfricellDestination.destination.value = binding?.bottomNavigationView?.menu?.getItem(0)
            }

            else -> {}
        }
    }


    private fun ActivityMainBinding.setupBottomNavigationStyle() {
        bottomNavigationView.itemIconTintList = null
    }

    private fun DrawerLayout.getAppBarConfigWithRoot(topLevelDestinations: Set<Int>): AppBarConfiguration {
        return AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(this).build()
    }
}