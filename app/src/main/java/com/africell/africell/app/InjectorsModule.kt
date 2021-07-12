package com.africell.africell.app

import com.africell.africell.features.aboutus.AboutUsFragment
import com.africell.africell.features.accountInfo.AccountInfoActivity
import com.africell.africell.features.accountInfo.AccountInfoFragment
import com.africell.africell.features.accountsNumber.AccountManagementActivity
import com.africell.africell.features.accountsNumber.AccountsNumbersFragment
import com.africell.africell.features.activateBundle.ActivateBundleFragment
import com.africell.africell.features.addNewAccount.AddAccountActivity
import com.africell.africell.features.addNewAccount.AddAccountFragment
import com.africell.africell.features.addNewAccount.VerifyAccountFragment
import com.africell.africell.features.authentication.*
import com.africell.africell.features.bookNumber.BookNumberFragment
import com.africell.africell.features.bundles.BundleActivity
import com.africell.africell.features.bundles.BundleCategoriesFragment
import com.africell.africell.features.bundles.BundleDetailsFragment
import com.africell.africell.features.bundles.BundlesFragment
import com.africell.africell.features.creditTransfer.CreditTransferFragment
import com.africell.africell.features.customerCare.CustomerCareFragment
import com.africell.africell.features.faq.FaqFragment
import com.africell.africell.features.help.HelpFragment
import com.africell.africell.features.home.HomeFragment
import com.africell.africell.features.home.MainActivity
import com.africell.africell.features.launch.PushFragment
import com.africell.africell.features.launch.RootActivity
import com.africell.africell.features.launch.SplashFragment
import com.africell.africell.features.lineRecharge.LineRechargeFragment
import com.africell.africell.features.location.LocationDetailsFragment
import com.africell.africell.features.location.LocationFragment
import com.africell.africell.features.location.LocationMapFragment
import com.africell.africell.features.profile.ChangePasswordFragment
import com.africell.africell.features.profile.EditProfileFragment
import com.africell.africell.features.reportIncident.ReportIncidentFragment
import com.africell.africell.features.services.ServiceDetailsFragment
import com.africell.africell.features.services.ServicesFragment
import com.africell.africell.features.settings.SettingsFragment
import com.africell.africell.features.sms.SMSFragment
import com.africell.africell.features.terms.TermsFragment
import com.africell.africell.features.usefulNumber.UsefulNumberFragment
import com.africell.africell.features.bundles.BundleVPFragment
import com.africell.africell.features.dataCalculator.DataCalculatorFragment
import com.africell.africell.features.help.HelpActivity
import com.africell.africell.features.myBundlesAndServices.MyBundleDetailsFragment
import com.africell.africell.features.myBundlesAndServices.MyBundleServicesVPFragment
import com.africell.africell.features.myBundlesAndServices.MyBundlesAndServicesFragment
import com.africell.africell.features.vasServices.VasServicesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InjectorsModule {
    @ContributesAndroidInjector
    internal abstract fun rootActivity(): RootActivity

    @ContributesAndroidInjector
    internal abstract fun pushActivity(): PushFragment

    @ContributesAndroidInjector
    internal abstract fun splashFragment(): SplashFragment

    @ContributesAndroidInjector
    internal abstract fun loginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun registerFragment(): RegisterFragment

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun homeFragment(): HomeFragment

    @ContributesAndroidInjector
    internal abstract fun helpFragment(): HelpFragment

    @ContributesAndroidInjector
    internal abstract fun faqFragment(): FaqFragment

    @ContributesAndroidInjector
    internal abstract fun aboutUsFragment(): AboutUsFragment

    @ContributesAndroidInjector
    internal abstract fun reportIncidentFragment(): ReportIncidentFragment

    @ContributesAndroidInjector
    internal abstract fun settingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    internal abstract fun verifyPinFragment(): VerifyPinFragment

    @ContributesAndroidInjector
    internal abstract fun authenticationActivity(): AuthenticationActivity

    @ContributesAndroidInjector
    internal abstract fun mobileNumberFragment(): MobileNumberFragment

    @ContributesAndroidInjector
    internal abstract fun setPasswordFragment(): ResetPasswordFragment

    @ContributesAndroidInjector
    internal abstract fun locationMapFragment(): LocationMapFragment

    @ContributesAndroidInjector
    internal abstract fun locationListFragment(): LocationFragment

    @ContributesAndroidInjector
    internal abstract fun locationDetailsFragment(): LocationDetailsFragment

    @ContributesAndroidInjector
    internal abstract fun servicesFragment(): ServicesFragment

    @ContributesAndroidInjector
    internal abstract fun usefulNumberFragment(): UsefulNumberFragment

    @ContributesAndroidInjector
    internal abstract fun bookNumberFragment(): BookNumberFragment

    @ContributesAndroidInjector
    internal abstract fun termsFragment(): TermsFragment

    @ContributesAndroidInjector
    internal abstract fun customerCareFragment(): CustomerCareFragment

    @ContributesAndroidInjector
    internal abstract fun serviceDetailsFragment(): ServiceDetailsFragment

    @ContributesAndroidInjector
    internal abstract fun bundleCategoriesFragment(): BundleCategoriesFragment

    @ContributesAndroidInjector
    internal abstract fun bundleActivity(): BundleActivity

    @ContributesAndroidInjector
    internal abstract fun smsFragment(): SMSFragment

    @ContributesAndroidInjector
    internal abstract fun lineRechargeFragment(): LineRechargeFragment


    @ContributesAndroidInjector
    internal abstract fun accountInfoActivity(): AccountInfoActivity

    @ContributesAndroidInjector
    internal abstract fun accountFragment(): AccountInfoFragment

    @ContributesAndroidInjector
    internal abstract fun editProfileFragment(): EditProfileFragment

    @ContributesAndroidInjector
    internal abstract fun changePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector
    internal abstract fun creditTransferFragment(): CreditTransferFragment

    @ContributesAndroidInjector
    internal abstract fun bundleVPFragment(): BundleVPFragment

    @ContributesAndroidInjector
    internal abstract fun bundleFragment(): BundlesFragment

    @ContributesAndroidInjector
    internal abstract fun bundleDetailsFragment(): BundleDetailsFragment

    @ContributesAndroidInjector
    internal abstract fun activateBundleFragment(): ActivateBundleFragment

    @ContributesAndroidInjector
    internal abstract fun dataCalculatorFragment(): DataCalculatorFragment

    @ContributesAndroidInjector
    internal abstract fun myBundlesAndServicesFragment(): MyBundlesAndServicesFragment

    @ContributesAndroidInjector
    internal abstract fun myBundleServicesVPFragment(): MyBundleServicesVPFragment

    @ContributesAndroidInjector
    internal abstract fun vasServicesFragment(): VasServicesFragment

    @ContributesAndroidInjector
    internal abstract fun accountsNumbersFragment(): AccountsNumbersFragment

    @ContributesAndroidInjector
    internal abstract fun accountManagementActivity(): AccountManagementActivity

    @ContributesAndroidInjector
    internal abstract fun addAccountActivity(): AddAccountActivity

    @ContributesAndroidInjector
    internal abstract fun addAccountFragment(): AddAccountFragment

    @ContributesAndroidInjector
    internal abstract fun verifyAccountFragment(): VerifyAccountFragment

    @ContributesAndroidInjector
    internal abstract fun myBundleDetailsFragment(): MyBundleDetailsFragment

    @ContributesAndroidInjector
    internal abstract fun helpActivity(): HelpActivity
}