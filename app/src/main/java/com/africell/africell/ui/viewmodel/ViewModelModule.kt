package com.africell.africell.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.africell.africell.features.aboutus.AboutViewModel
import com.africell.africell.features.accountInfo.AccountViewModel
import com.africell.africell.features.activateBundle.ActivateBundleViewModel
import com.africell.africell.features.addNewAccount.AddAccountViewModel
import com.africell.africell.features.authentication.LoginViewModel
import com.africell.africell.features.authentication.RegisterViewModel
import com.africell.africell.features.authentication.ResetPasswordViewModel
import com.africell.africell.features.authentication.VerifyPinViewModel
import com.africell.africell.features.bookNumber.BookNumberViewModel
import com.africell.africell.features.bundles.BundleDetailsViewModel
import com.africell.africell.features.bundles.BundlesViewModel
import com.africell.africell.features.creditTransfer.CreditTransferViewModel
import com.africell.africell.features.customerCare.CustomerCareViewModel
import com.africell.africell.features.dataCalculator.DataCalculatorViewModel
import com.africell.africell.features.faq.FaqViewModel
import com.africell.africell.features.home.HomeViewModel
import com.africell.africell.features.home.ImageViewModel
import com.africell.africell.features.launch.SplashViewModel
import com.africell.africell.features.lineRecharge.LineRechargeViewModel
import com.africell.africell.features.location.LocationViewModel
import com.africell.africell.features.myBundlesAndServices.MyBundlesAndServicesViewModel
import com.africell.africell.features.profile.ChangePasswordViewModel
import com.africell.africell.features.profile.EditProfileViewModel
import com.africell.africell.features.reportIncident.ReportIncidentViewModel
import com.africell.africell.features.services.ServicesViewModel
import com.africell.africell.features.settings.SettingsViewModel
import com.africell.africell.features.sms.SMSViewModel
import com.africell.africell.features.terms.TermsViewModel
import com.africell.africell.features.usefulNumber.UsefulNumberViewModel
import com.africell.africell.features.vasServices.VasServicesViewModel
import com.africell.africell.ui.BaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BaseViewModel::class)
    internal abstract fun baseViewModel(viewModel: BaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FaqViewModel::class)
    internal abstract fun faqViewModel(viewModel: FaqViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerifyPinViewModel::class)
    internal abstract fun verifyPinViewModel(viewModel: VerifyPinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun registerViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    internal abstract fun resetPasswordViewModel(viewModel: ResetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    internal abstract fun locationViewModel(viewModel: LocationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun homeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ServicesViewModel::class)
    internal abstract fun servicesViewModel(viewModel: ServicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UsefulNumberViewModel::class)
    internal abstract fun usefulNumberViewModel(viewModel: UsefulNumberViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookNumberViewModel::class)
    internal abstract fun bookNumberViewModel(viewModel: BookNumberViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TermsViewModel::class)
    internal abstract fun termsViewModel(viewModel: TermsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AboutViewModel::class)
    internal abstract fun aboutViewModel(viewModel: AboutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportIncidentViewModel::class)
    internal abstract fun reportIncidentViewModel(viewModel: ReportIncidentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CustomerCareViewModel::class)
    internal abstract fun customerCareViewModel(viewModel: CustomerCareViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(BundlesViewModel::class)
    internal abstract fun bundlesViewModel(viewModel: BundlesViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SMSViewModel::class)
    internal abstract fun smsViewModel(viewModel: SMSViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LineRechargeViewModel::class)
    internal abstract fun lineRechargeViewModel(viewModel: LineRechargeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    internal abstract fun accountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    internal abstract fun editProfileViewModel(viewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    internal abstract fun changePasswordViewModel(viewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreditTransferViewModel::class)
    internal abstract fun creditTransferViewModel(viewModel: CreditTransferViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivateBundleViewModel::class)
    internal abstract fun activateBundleViewModel(viewModel: ActivateBundleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DataCalculatorViewModel::class)
    internal abstract fun dataCalculatorViewModel(viewModel: DataCalculatorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyBundlesAndServicesViewModel::class)
    internal abstract fun myBundlesAndServicesViewModel(viewModel: MyBundlesAndServicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VasServicesViewModel::class)
    internal abstract fun vasServicesViewModel(viewModel: VasServicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddAccountViewModel::class)
    internal abstract fun addAccountViewModel(viewModel: AddAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel::class)
    internal abstract fun imageViewModel(viewModel: ImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun settingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BundleDetailsViewModel::class)
    internal abstract fun bundleDetailsViewModel(viewModel: BundleDetailsViewModel): ViewModel
}