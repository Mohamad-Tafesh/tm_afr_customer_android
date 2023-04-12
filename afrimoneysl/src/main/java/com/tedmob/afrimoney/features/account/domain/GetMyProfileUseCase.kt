package com.tedmob.afrimoney.features.account.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.UserDTO
import com.tedmob.afrimoney.data.entity.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetMyProfileUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<Profile, Unit>() {

    private val serverDateFormat by lazy { SimpleDateFormat(UserDTO.DATE_FORMAT, Locale.ENGLISH) }

    override suspend fun execute(params: Unit): Profile {
        return withContext(Dispatchers.IO) { //TODO assemble profile object
            val response = api.userInfo()

            Profile(

                null,
                response.fname,
                response.lname,
                response.gender,
/*                try {
                    response.dob?.let { serverDateFormat.parse(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                },*/
                response.dob,
                null,
                response.idNumber,
                null,
                null,
                null,
                null,
            )
        }
    }
}