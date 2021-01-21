package com.tedmob.africell.app

import com.tedmob.africell.features.aboutus.AboutUsFragment
import com.tedmob.africell.features.accountInfo.AccountInfoActivity
import com.tedmob.africell.features.accountInfo.AccountInfoFragment
import com.tedmob.africell.features.authentication.*
import com.tedmob.africell.features.bookNumber.BookNumberFragment
import com.tedmob.africell.features.bundles.BundleActivity
import com.tedmob.africell.features.bundles.BundleCategoriesFragment
import com.tedmob.africell.features.bundles.BundleDetailsFragment
import com.tedmob.africell.features.bundles.BundlesFragment
import com.tedmob.africell.features.creditTransfer.CreditTransferFragment
import com.tedmob.africell.features.customerCare.CustomerCareFragment
import com.tedmob.africell.features.faq.FaqFragment
import com.tedmob.africell.features.help.HelpFragment
import com.tedmob.africell.features.home.HomeFragment
import com.tedmob.africell.features.home.MainActivity
import com.tedmob.africell.features.launch.PushFragment
import com.tedmob.africell.features.launch.RootActivity
import com.tedmob.africell.features.launch.SplashFragment
import com.tedmob.africell.features.lineRecharge.LineRechargeFragment
import com.tedmob.africell.features.location.LocationDetailsFragment
import com.tedmob.africell.features.location.LocationFragment
import com.tedmob.africell.features.location.LocationMapFragment
import com.tedmob.africell.features.profile.ChangePasswordFragment
import com.tedmob.africell.features.profile.EditProfileFragment
import com.tedmob.africell.features.reportIncident.ReportIncidentFragment
import com.tedmob.africell.features.services.ServiceDetailsFragment
import com.tedmob.africell.features.services.ServicesFragment
import com.tedmob.africell.features.settings.SettingsFragment
import com.tedmob.africell.features.sms.SMSFragment
import com.tedmob.africell.features.terms.TermsFragment
import com.tedmob.africell.features.usefulNumber.UsefulNumberFragment
import com.tedmob.africelll.features.bundles.BundleVPFragment
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
}