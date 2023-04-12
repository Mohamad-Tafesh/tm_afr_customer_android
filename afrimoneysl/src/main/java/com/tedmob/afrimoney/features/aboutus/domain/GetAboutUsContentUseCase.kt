package com.tedmob.afrimoney.features.aboutus.domain

import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.AboutData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAboutUsContentUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<AboutData, Unit>() {

    override suspend fun execute(params: Unit): AboutData {
        return withContext(Dispatchers.IO) { //TODO api call
            AboutData(
                listOf(
                    AboutData.SocialLink(
                        R.drawable.aboutfb,
                        "https://www.facebook.com/AfricellSierraLeone"
                    ),
                    AboutData.SocialLink(
                        R.drawable.aboutinsta,
                        "https://www.instagram.com/africellsl/"
                    ),
                    AboutData.SocialLink(
                        R.drawable.abouttwitter,
                        "https://twitter.com/AfricellSalone?s=20&t=5MSJoEaVnRPNuYumndxhsQ"
                    ),
                    AboutData.SocialLink(
                        R.drawable.socialtik,
                        " https://www.tiktok.com/@africellsl"
                    ),
                ),
                "Afrimoney is a mobile money solution offered to all Africell Subscribers.\n\nAfrimoney is a safe, secure and convenient way to store, manage and send money, and pay bills with an account linked to your Africell SIM card.",
                "support@afrimoney.sl"
            )
        }
    }
}