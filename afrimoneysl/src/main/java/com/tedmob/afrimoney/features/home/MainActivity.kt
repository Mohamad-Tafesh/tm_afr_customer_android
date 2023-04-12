package com.tedmob.afrimoney.features.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBActivity
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.util.adapter.adapter
import com.tedmob.afrimoney.util.intents.email
import com.tedmob.afrimoney.util.navigation.getStartDestinationsFor
import com.tedmob.afrimoney.util.navigation.onNavDestinationIdSelected
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainActivity : BaseVBActivity<ActivityMainBinding>() {

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    @Inject
    lateinit var session: SessionRepository

    private val bottomNavFragmentIds: List<Int> by lazy {
        requireBinding().bottomNavigationView.menu.getStartDestinationsFor(findNavController(R.id.nav_host_main).graph)
    }
    private val args by navArgs<MainActivityArgs>()
    private val topLevelDestinations = setOf(
        R.id.homeFragment,
        R.id.aboutUsFragment,
        R.id.termsOfUseFragment,
        R.id.contactUsFragment,
        R.id.helpFragment,
        R.id.referFriendFragment,
        R.id.faqFragment,
        R.id.settingsFragment,
        R.id.accountFragment,
        R.id.africellServicesFragment2
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(ActivityMainBinding::inflate, false)
        setSupportActionBar(requireBinding().drawerToolbar)
        setupNavigation()
        setupBottomNavigationStyle()
        setupScanButton()

        if (args.isNew) { //TODO how to check new user
            findNavController(R.id.nav_host_main).navigate(R.id.walkthroughActivity)
        }
    }


    private fun setupScanButton() {
        withVBAvailable {
            fabQR.setDebouncedOnClickListener {
                showDialog()
            }
        }
    }

    fun showDialog(
        onDismiss: (() -> Unit)? = null
    ) {
        val viewBinding = QrcodeAlertBinding.inflate(layoutInflater, FrameLayout(this), false)

        viewBinding.run {
            val code = session.msisdn
            try {
                val requiredWidth =
                    (resources.displayMetrics.widthPixels * 0.75).roundToInt()

                val bitmap = BarcodeEncoder().encodeBitmap(
                    code, BarcodeFormat.QR_CODE,
                    requiredWidth, requiredWidth
                )
                barcodeImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        val dialog = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setView(viewBinding.root)

            .setOnDismissListener { onDismiss?.invoke() }
            .show()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /*android.R.id.home -> NavigationUI.navigateUp(
                findNavController(R.id.nav_host_main),
                requireBinding().drawerLayout
            )*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(
        findNavController(R.id.nav_host_main),
        AppBarConfiguration(
            topLevelDestinations,
            requireBinding().drawerLayout
        )
    )

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_main) as NavHostFragment

        navHostFragment.navController
    }
    private fun setupNavigation() {
        requireBinding().navigationView.itemIconTintList = null

    //  /*  findNavController(R.id.nav_host_main).*/ navController.let {
        findNavController(R.id.nav_host_main).let {
            setupActionBarWithNavController(
                it,
                AppBarConfiguration(topLevelDestinations, requireBinding().drawerLayout),
            )
            val backPressedCallback = onBackPressedDispatcher.addCallback(this, enabled = false) {
                requireBinding().drawerLayout.closeDrawer(GravityCompat.START)
            }
            requireBinding().drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
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

            requireBinding().setupSideMenuWithNavController(it)

            requireBinding().bottomNavigationView.setupWithNavController(it)
            //preventing reselection by passing empty listener; a null listener will result in onItemSelected listener to be called on reselection.
            requireBinding().bottomNavigationView.setOnItemReselectedListener { }
            it.addOnDestinationChangedListener { _, destination, _ ->
                withVBAvailable {
                    supportActionBar?.setHomeAsUpIndicator(
                        when (destination.id) {
                            R.id.homeFragment,
                            R.id.aboutUsFragment,
                            R.id.referFriendFragment,
                            R.id.faqFragment,
                            R.id.helpFragment,
                            R.id.accountFragment,
                            R.id.locateUsFragment,
                            R.id.africellServicesFragment2
                            -> R.drawable.sidemenunav

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
                            R.id.airtimeConfirmationfragment
                            -> R.drawable.ic_nav_back_white


                            else -> R.drawable.ic_nav_back_black
                        }
                    )

                    bottomNavigationView.visibility =
                        if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE
                    fabQR.visibility =
                        if (bottomNavFragmentIds.contains(destination.id)) View.VISIBLE else View.GONE


                }
            }
        }
    }

    private sealed class SideMenuOption(
        @DrawableRes val iconRes: Int,
        @StringRes val labelRes: Int
    ) {
        object About : SideMenuOption(R.drawable.ic_africell_services, R.string.about_afrimoney)
        object Terms : SideMenuOption(R.drawable.ic_africredit, R.string.terms_of_use)
        object Contact : SideMenuOption(R.drawable.ic_merchant_payment, R.string.contact_support)

        /*   object Help : SideMenuOption(R.drawable.ic_banking_services, R.string.help)
           object Refer : SideMenuOption(R.drawable.sidemenureferafriend, R.string.refer_a_friend)*/
        object FAQs : SideMenuOption(R.drawable.sidemenufaqs, R.string.faq)
        object Settings : SideMenuOption(R.drawable.sidemenusettings, R.string.app_settings)
        object Account : SideMenuOption(0, R.string.app_settings)

        companion object {
            fun values() = listOf(
                About,
                Terms,
                Contact,
                /*  Help,
                  Refer,*/
                FAQs,
                Settings,
            )
        }
    }

    private fun ActivityMainBinding.setupSideMenuWithNavController(navController: NavController) {
        sideMenuLayout.sideMenuRV.adapter = adapter(SideMenuOption.values()) {
            viewBinding(ItemSideMenuBinding::inflate)
            onBindItemToViewBinding<ItemSideMenuBinding> {
                image.load(it.iconRes)
                text.setText(it.labelRes)

                root.setOnClickListener { _ ->
                    if (navController.redirectToSideMenuItem(it)) {
                        withVBAvailable {
                            drawerLayout.closeDrawer(navigationView)
                        }
                    }
                }
            }
        }
        sideMenuLayout.sideMenuTopSection.setDebouncedOnClickListener {
            if (navController.onNavDestinationIdSelected(R.id.homeFragment)) {
                withVBAvailable {
                    drawerLayout.closeDrawer(navigationView)
                }
            }
        }
    }

    private fun NavController.redirectToSideMenuItem(item: SideMenuOption): Boolean {
        return when (item) {
            SideMenuOption.About -> {
                onNavDestinationIdSelected(R.id.aboutUsFragment)
            }
            SideMenuOption.Contact -> {
                email("support@afrimoney.sl", "", "Afrimoney Contact Support", "")
                true
                // onNavDestinationIdSelected(R.id.contactUsFragment)
            }
            SideMenuOption.FAQs -> {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.africell.sl/afrimoney/#FAQs"))
                startActivity(browserIntent)
                true
                //onNavDestinationIdSelected(R.id.faqFragment)
            }
/*            SideMenuOption.Help -> {
                onNavDestinationIdSelected(R.id.helpFragment)
            }
            SideMenuOption.Refer -> {
                onNavDestinationIdSelected(R.id.referFriendFragment)
            }*/
            SideMenuOption.Settings -> {
                onNavDestinationIdSelected(R.id.settingsFragment)
            }
            SideMenuOption.Terms -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.africell.com/en/terms-conditions")
                )
                startActivity(browserIntent)
                true
                //onNavDestinationIdSelected(R.id.termsOfUseFragment)
            }
            SideMenuOption.Account -> {
                onNavDestinationIdSelected(R.id.accountFragment)
            }
        }
    }

    private fun setupBottomNavigationStyle() {
        requireBinding().bottomNavigationView.itemIconTintList = null
    }
}