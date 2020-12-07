package com.tedmob.africell.features.profile


import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.profile.domain.EditProfileUseCase
import com.tedmob.africell.features.profile.domain.GetProfileUseCase
import com.tedmob.africell.ui.BaseViewModel
import com.tedmob.africell.util.ISO_DATE_FORMAT
import com.tedmob.africell.util.toFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class EditProfileViewModel
@Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val appExceptionFactory: AppExceptionFactory
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
        ResourceUseCaseExecutor(editProfileUseCase, params, updatedProfileData, appExceptionFactory, null).execute()
    }

    fun getProfile() {
        ResourceUseCaseExecutor(getProfileUseCase, Unit, profileData, appExceptionFactory, null){
            getProfileUseCase
        }.execute()
    }

    override fun onCleared() {
        getProfileUseCase.dispose()
        editProfileUseCase.dispose()
        super.onCleared()
    }
}