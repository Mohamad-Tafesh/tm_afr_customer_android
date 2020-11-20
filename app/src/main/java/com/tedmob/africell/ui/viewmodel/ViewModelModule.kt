package com.tedmob.africell.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tedmob.africell.features.authentication.LoginViewModel
import com.tedmob.africell.features.authentication.RegisterViewModel
import com.tedmob.africell.features.authentication.VerifyPinViewModel
import com.tedmob.africell.features.bookNumber.BookNumberViewModel
import com.tedmob.africell.features.faq.FaqViewModel
import com.tedmob.africell.features.home.HomeViewModel
import com.tedmob.africell.features.launch.SplashViewModel
import com.tedmob.africell.features.location.LocationViewModel
import com.tedmob.africell.features.services.ServicesViewModel
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

}