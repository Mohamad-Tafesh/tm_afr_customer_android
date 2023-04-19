package com.tedmob.afrimoney.features.newhome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.AfricellDestination
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.databinding.ActivityAfrimoneyNewBinding
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.navigation.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AfrimoneyActivity : BaseVBActivity<ActivityAfrimoneyNewBinding>() {


    @Inject
    lateinit var session: SessionRepository

    private val viewModel by provideViewModel<ActivityViewModel>()

    private val number: String? by lazy { intent.getStringExtra("number") }
    private val token: String? by lazy { intent.getStringExtra("token") }


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
    }


    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(
        findNavController(R.id.nav_host_main),
        appBarConfiguration
    ) || findNavController(R.id.nav_host_main).navigateUp()

    private fun ActivityAfrimoneyNewBinding.setupNavigation() {
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

            observeResource(viewModel.verified) {
                proceedWith(it)
            }
            viewModel.verify(number.orEmpty(), token.orEmpty())
            session.msisdn = number.orEmpty()


            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {

                    R.id.homeFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }
                    R.id.customerCareFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }
                    R.id.SMSFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }
                    R.id.locationListFragment -> {
                        AfricellDestination.destination.value = it
                        finish()
                    }
                    else -> {}

                }

                true
            }

            supportActionBar?.setHomeAsUpIndicator(com.tedmob.afrimoney.R.drawable.sidemenunav)
            it.addOnDestinationChangedListener { _, destination, _ ->
                withVBAvailable {
                    supportActionBar?.setHomeAsUpIndicator(
                        when (destination.id) {
                            com.tedmob.afrimoney.R.id.homeFragment,
                            com.tedmob.afrimoney.R.id.aboutUsFragment,
                            com.tedmob.afrimoney.R.id.referFriendFragment,
                            com.tedmob.afrimoney.R.id.faqFragment,
                            com.tedmob.afrimoney.R.id.helpFragment,
                            com.tedmob.afrimoney.R.id.accountFragment,
                            com.tedmob.afrimoney.R.id.locateUsFragment,
                            com.tedmob.afrimoney.R.id.africellServicesFragment2
                            -> R.mipmap.nav_side_menu

                            //fixme black menu

                            com.tedmob.afrimoney.R.id.chooseBankingServiceTypeFragment,
                            com.tedmob.afrimoney.R.id.termsOfUseFragment,
                            com.tedmob.afrimoney.R.id.settingsFragment,
                            com.tedmob.afrimoney.R.id.contactUsFragment

                            -> com.tedmob.afrimoney.R.drawable.sidemenunavblack


                            com.tedmob.afrimoney.R.id.transferMoneyConfirmationFragment,
                            com.tedmob.afrimoney.R.id.merchantPaymentConfirmationFragment,
                            com.tedmob.afrimoney.R.id.pending_transactionsMainFragment,
                            com.tedmob.afrimoney.R.id.africellServicesFragment,
                            com.tedmob.afrimoney.R.id.withdrawMain,
                            com.tedmob.afrimoney.R.id.payMyBillsFragment,
                            com.tedmob.afrimoney.R.id.payMyBillsConfirmRenewDSTVFragment,
                            com.tedmob.afrimoney.R.id.bankingServicesFragment,
                            com.tedmob.afrimoney.R.id.agentCodeConfirmationFragment,
                            com.tedmob.afrimoney.R.id.mercuryConfirmationFragment,
                            com.tedmob.afrimoney.R.id.agentPhoneNumberConfirmationFragment,
                            com.tedmob.afrimoney.R.id.bankToWalletConfirmationFragment,
                            com.tedmob.afrimoney.R.id.walletToBankConfirmationFragment,
                            com.tedmob.afrimoney.R.id.powergenConfirmationFragment,
                            com.tedmob.afrimoney.R.id.payMyBillsOptionsFragment,
                            com.tedmob.afrimoney.R.id.changePinFragment,
                            com.tedmob.afrimoney.R.id.enterPin_from_home,
                            com.tedmob.afrimoney.R.id.enterPin_from_account,
                            com.tedmob.afrimoney.R.id.enterPin,
                            com.tedmob.afrimoney.R.id.yaRemixConfirmationFragment,
                            com.tedmob.afrimoney.R.id.yaSpecialConfirmationFragment,
                            com.tedmob.afrimoney.R.id.postPaidConfirmationFragment,
                            com.tedmob.afrimoney.R.id.risingAcademyConfirmationFragment,
                            com.tedmob.afrimoney.R.id.edsaConfirmationFragment,
                            com.tedmob.afrimoney.R.id.fccConfirmationFragment,
                            com.tedmob.afrimoney.R.id.waecConfirmationFragment,
                            com.tedmob.afrimoney.R.id.bundleConfirmationFragment,
                            com.tedmob.afrimoney.R.id.checkDSTVConfirmationFragment,
                            com.tedmob.afrimoney.R.id.balanceEnquiryFragment,
                            com.tedmob.afrimoney.R.id.confirmPendingTransactionsFragment,
                            com.tedmob.afrimoney.R.id.airtimeConfirmationfragment
                            -> com.tedmob.afrimoney.R.drawable.ic_nav_back_white


                            else -> com.tedmob.afrimoney.R.drawable.ic_nav_back_black
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


    fun proceedWith(state: UserState) {
        when (state) {
            is UserState.NotRegistered -> {
                startActivity(Intent(this, AfrimoneyRegistrationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
            is UserState.Registered -> {
                //findNavController(R.id.nav_host_main).navigate(R.id.afrimoneyFragment)
                val bundle = Bundle()
                /*    bundle.putString("mobilenb", session.msisdnAfrimoney)
                    findNavController(R.id.nav_host_main).navigate(R.id.setPinFragment2, bundle)*/
                startActivity(Intent(this, AfrimoneyRegistrationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
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