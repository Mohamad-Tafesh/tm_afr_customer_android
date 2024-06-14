package com.tedmob.afrimoney.features.newhome

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.AfricellDestination
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.databinding.ActivityAfrimoneyNewBinding
import com.tedmob.afrimoney.util.navigation.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AfrimoneyActivity : BaseVBActivity<ActivityAfrimoneyNewBinding>() {


    @Inject
    lateinit var session: SessionRepository


    private val number: String? by lazy { intent.getStringExtra("number") }
    private val token: String? by lazy { intent.getStringExtra("token") }
    private val restart: Int? by lazy { intent.getIntExtra("restart", -1) }


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
        setContent(ActivityAfrimoneyNewBinding::inflate, false)
        setSupportActionBar(requireBinding().drawerToolbar)
        withVBAvailable {
            appBarConfiguration = drawerLayout.getAppBarConfigWithRoot(topLevelDestinations)
            setupNavigation()
            setupBottomNavigationStyle()
        }
        if (restart == 401) {
            this.finish()
        }
    }


    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(
        findNavController(R.id.nav_host_main),
        appBarConfiguration
    ) || findNavController(R.id.nav_host_main).navigateUp()

    private fun ActivityAfrimoneyNewBinding.setupNavigation() {
        navigationView.itemIconTintList = null
        findNavController(R.id.nav_host_main).let {
            setupActionBarWithNavController(it, drawerLayout)
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


            val backPressedCallback = onBackPressedDispatcher.addCallback(this@AfrimoneyActivity, enabled = false) {
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

            afrimoneyImg.setImageResource(
                R.mipmap.tab_afrimoney_selected
            )

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

            navigationView.setNavigationItemSelectedListener {
                AfricellDestination.side_menu_destination.value = it
                finish()
                true
            }

            it.addOnDestinationChangedListener { _, destination, _ ->
                Timber.d("Navigate to %s", destination.label)
                bottomNavigationView.visibility =
                    if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                afrimoneyImg.visibility =
                    if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                afrimoneyText.visibility =
                    if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
            }

            session.msisdn = number.orEmpty()


            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {

                    R.id.homeFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }

                   /* R.id.customerCareFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }

                    R.id.SMSFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }*/

                    R.id.locationListFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }

                    else -> {}

                }

                true
            }

            supportActionBar?.setHomeAsUpIndicator(R.drawable.sidemenunav)
            it.addOnDestinationChangedListener { _, destination, _ ->
                withVBAvailable {
                    supportActionBar?.setHomeAsUpIndicator(
                        when (destination.id) {
                            R.id.homeFragment,
                            R.id.aboutUsFragment,
                            R.id.referFriendFragment,
                            R.id.faqFragment,
                            R.id.helpFragment,
                            R.id.locateUsFragment,
                            R.id.africellServicesFragment2
                            -> R.mipmap.nav_side_menu

                            //fixme black menu

                            R.id.chooseBankingServiceTypeFragment,
                            R.id.termsOfUseFragment,
                            R.id.settingsFragment,
                            R.id.contactUsFragment

                            -> R.drawable.sidemenunavblack


                            R.id.transferMoneyConfirmationFragment,
                            R.id.merchantPaymentConfirmationFragment,
                            R.id.pending_transactionsMainFragment,
                            R.id.africellServicesFragment,
                            R.id.withdrawMain,
                            R.id.payMyBillsFragment,
                            R.id.payMyBillsConfirmRenewDSTVFragment,
                            R.id.bankingServicesFragment,
                            R.id.agentCodeConfirmationFragment,
                            R.id.mercuryConfirmationFragment,
                            R.id.agentPhoneNumberConfirmationFragment,
                            R.id.bankToWalletConfirmationFragment,
                            R.id.walletToBankConfirmationFragment,
                            R.id.powergenConfirmationFragment,
                            R.id.payMyBillsOptionsFragment,
                            R.id.changePinFragment,
                            R.id.enterPin_from_home,
                            R.id.enterPin_from_account,
                            R.id.enterPin,
                            R.id.yaRemixConfirmationFragment,
                            R.id.yaSpecialConfirmationFragment,
                            R.id.postPaidConfirmationFragment,
                            R.id.risingAcademyConfirmationFragment,
                            R.id.edsaConfirmationFragment,
                            R.id.fccConfirmationFragment,
                            R.id.waecConfirmationFragment,
                            R.id.bundleConfirmationFragment,
                            R.id.checkDSTVConfirmationFragment,
                            R.id.balanceEnquiryFragment,
                            R.id.confirmPendingTransactionsFragment,
                            R.id.airtimeConfirmationfragment,
                            R.id.accountFragment,
                            -> R.drawable.ic_nav_back_black


                            else -> R.drawable.ic_nav_back_black
                        }
                    )

                    /*                    bottomNavigationView.visibility =
                                            if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                                        fabQR.visibility =
                                            if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE*/


                }
            }

        }
    }


    private fun ActivityAfrimoneyNewBinding.setupBottomNavigationStyle() {
        bottomNavigationView.itemIconTintList = null
    }

    private fun DrawerLayout.getAppBarConfigWithRoot(topLevelDestinations: Set<Int>): AppBarConfiguration {
        return AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(this).build()
    }
}