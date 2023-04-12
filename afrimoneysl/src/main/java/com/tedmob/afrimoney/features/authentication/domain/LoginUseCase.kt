package com.tedmob.afrimoney.features.authentication.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.GenerateOtpDTO
import com.tedmob.afrimoney.util.security.StringEncryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


class LoginUseCase
@Inject constructor(
    private val api: TedmobApis,
    @Named("local-string") private val encryptor: StringEncryptor,
) : SuspendableUseCase<GenerateOtpDTO, LoginUseCase.UserLoginInfo>() {

    override suspend fun execute(params: UserLoginInfo): GenerateOtpDTO {
        return withContext(Dispatchers.IO) {
            api.generateOtp(params.username)
        }
    }

    data class UserLoginInfo(val username: String)
}
