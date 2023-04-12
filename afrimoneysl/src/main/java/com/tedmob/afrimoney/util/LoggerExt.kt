package com.tedmob.afrimoney.util

import com.tedmob.afrimoney.data.analytics.AnalyticsHandler
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import com.tedmob.afrimoney.data.repository.domain.SessionRepository

fun SessionRepository.identifyUser(analytics: AnalyticsHandler, crashlytics: CrashlyticsHandler) {
    if (isLoggedIn()) {
        analytics.setUserProperty("token", accessToken)

        crashlytics.setCustomKey("token", accessToken)
    } else {
        removeUserIdentification(analytics, crashlytics)
    }
}

fun removeUserIdentification(analytics: AnalyticsHandler, crashlytics: CrashlyticsHandler) {
    analytics.setUserProperty("token", null)

    crashlytics.setCustomKey("token", "")
}