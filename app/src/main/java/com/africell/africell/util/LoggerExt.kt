package com.africell.africell.util

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.africell.africell.data.repository.domain.SessionRepository

fun SessionRepository.identifyUser(firebaseAnalytics: FirebaseAnalytics, firebaseCrashlytics: FirebaseCrashlytics) {
    if (isLoggedIn()) {
        //firebaseAnalytics.setUserId(accessToken)
        firebaseAnalytics.setUserProperty("token", accessToken)
        //firebaseCrashlytics.setUserId(accessToken)
        firebaseCrashlytics.setCustomKey("token", accessToken)
    } else {
        removeUserIdentification(firebaseAnalytics, firebaseCrashlytics)
    }
}

fun removeUserIdentification(firebaseAnalytics: FirebaseAnalytics, firebaseCrashlytics: FirebaseCrashlytics) {
    //firebaseAnalytics.setUserId(null)
    firebaseAnalytics.setUserProperty("token", null)
    //firebaseCrashlytics.setUserId("")
    firebaseCrashlytics.setCustomKey("token", "")
}