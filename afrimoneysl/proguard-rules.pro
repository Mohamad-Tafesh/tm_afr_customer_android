#-printconfiguration "build/outputs/r8/full-r8-config.txt"

# Kotlin
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# To keep the hamburger/back animation working (to retest with newer versions of androidx appcompat and navigation)
-keep class androidx.appcompat.graphics.** { *; }

# Classes accessed inside navigation component files (any parcelable, serializable, or enum)
-keepnames class com.tedmob.afrimoney.notification.NotificationData
-keepnames class com.tedmob.afrimoney.data.entity.Bank
-keepnames class com.tedmob.modules.mapcontainer.view.MapLatLng
-keepnames class com.tedmob.afrimoney.data.entity.BundlelistParent
-keepnames class com.tedmob.afrimoney.data.entity.Message
-keepnames class com.tedmob.afrimoney.data.api.dto.PendingTransactionsItemDTO
-keepnames class com.tedmob.afrimoney.notification.NotificationData

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Gson:
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}


# OkHttp:
-dontwarn okio.**

# Retrofit:
-dontwarn com.squareup.**

# Mobile Services:
-keep class com.tedmob.afrimoney.data.analytics.* {
    public static com.tedmob.afrimoney.data.analytics.AnalyticsHandler newInstanceIfAvailable(android.content.Context);
}

-keep class com.tedmob.afrimoney.data.crashlytics.* {
    public static com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler newInstanceIfAvailable(android.content.Context);
}

-keep class com.tedmob.afrimoney.util.location.gms.GMSAppLocationServices {
    public static com.tedmob.afrimoney.util.location.AppLocationServices newInstanceIfAvailable(android.content.Context);
}

-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keep class com.huawei.hianalytics.** { *; }
-keep class com.huawei.updatesdk.** { *; }
-keep class com.huawei.hms.** { *; }