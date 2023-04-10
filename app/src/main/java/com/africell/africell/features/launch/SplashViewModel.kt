package com.africell.africell.features.launch

import android.content.Intent
import androidx.lifecycle.LiveData
import com.africell.africell.R
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.features.help.HelpActivity
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    private val session: SessionRepository
) : BaseViewModel() {

    val navigationAction: LiveData<Resource<NavigationAction>> get() = _navigationAction
    private val _navigationAction: SingleLiveEvent<Resource<NavigationAction>> = SingleLiveEvent()


    fun redirectToAppropriateSection() {
        _navigationAction.value = Resource.Success(
            NavigationAction { activity ->
                if (session.isLoggedIn()) {
                    navigate(R.id.action_splashFragment_to_mainActivity)
                } else {
                    navigate(R.id.action_splashFragment_to_authenticationActivity)

                }
                 if(session.showHelp) {
                     activity?.startActivity(Intent(activity, HelpActivity::class.java))
                 }

                activity?.finish()
            }
        )
    }


    override fun onCleared() {
        super.onCleared()
    }
}