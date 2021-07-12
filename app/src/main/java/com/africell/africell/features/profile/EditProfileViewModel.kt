package com.africell.africell.features.profile


import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.UserDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.profile.domain.EditProfileUseCase
import com.africell.africell.features.profile.domain.GetProfileUseCase
import com.africell.africell.ui.BaseViewModel
import com.africell.africell.util.ISO_DATE_FORMAT
import com.africell.africell.util.toFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class EditProfileViewModel
@Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {
    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }


    val updatedProfileData = SingleLiveEvent<Resource<Unit>>()
    val profileData = SingleLiveEvent<Resource<UserDTO>>()

    val dobData = MutableLiveData<Long>()


    fun setProfile(
        firstName: String?, lastName: String?, email: String?
    ) {
        val params = EditProfileUseCase.Params(firstName, lastName, email, dobData.value?.toFormat(ISO_DATE_FORMAT))
        ResourceUseCaseExecutor(editProfileUseCase, params, updatedProfileData,appExceptionFactory, appSessionNavigator, null).execute()
    }

    fun getProfile() {
        ResourceUseCaseExecutor(getProfileUseCase, Unit, profileData,appExceptionFactory, appSessionNavigator){
            getProfileUseCase
        }.execute()
    }

    override fun onCleared() {
        getProfileUseCase.dispose()
        editProfileUseCase.dispose()
        super.onCleared()
    }
}