package com.tedmob.africell.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tedmob.africell.features.aboutus.AboutViewModel
import com.tedmob.africell.features.accountInfo.AccountViewModel
import com.tedmob.africell.features.authentication.LoginViewModel
import com.tedmob.africell.features.authentication.RegisterViewModel
import com.tedmob.africell.features.authentication.ResetPasswordViewModel
import com.tedmob.africell.features.authentication.VerifyPinViewModel
import com.tedmob.africell.features.bookNumber.BookNumberViewModel
import com.tedmob.africell.features.bundles.BundlesViewModel
import com.tedmob.africell.features.creditTransfer.CreditTransferViewModel
import com.tedmob.africell.features.customerCare.CustomerCareViewModel
import com.tedmob.africell.features.faq.FaqViewModel
import com.tedmob.africell.features.home.HomeViewModel
import com.tedmob.africell.features.launch.SplashViewModel
import com.tedmob.africell.features.lineRecharge.LineRechargeViewModel
import com.tedmob.africell.features.location.LocationViewModel
import com.tedmob.africell.features.profile.ChangePasswordViewModel
import com.tedmob.africell.features.profile.EditProfileViewModel
import com.tedmob.africell.features.reportIncident.ReportIncidentViewModel
import com.tedmob.africell.features.services.ServicesViewModel
import com.tedmob.africell.features.sms.SMSViewModel
import com.tedmob.africell.features.terms.TermsViewModel
import com.tedmob.africell.features.usefulNumber.UsefulNumberViewModel
import com.tedmob.africell.ui.BaseViewModel
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


}