package com.tedmob.africell.features.launch

import androidx.lifecycle.LiveData
import com.tedmob.africell.R
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

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
                /* if(session.showHelp) {
                     activity?.startActivity(Intent(activity, HelpActivity::class.java))
                 }
 */
                activity?.finish()
            }
        )
    }


    override fun onCleared() {
        super.onCleared()
    }
}