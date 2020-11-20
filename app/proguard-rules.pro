#-printconfiguration "build/outputs/r8/full-r8-config.txt"

# Kotlin
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# To keep the hamburger/back animation working (to retest with newer versions of androidx appcompat and navigation)
-keep class androidx.appcompat.graphics.** { *; }

# Classes accessed inside navigation component files (any parcelable, serializable, or enum)
-keepnames class com.tedmob.africell.notification.NotificationData

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Gson:
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Crashlytics:
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

# OkHttp:
-dontwarn okio.**

# Retrofit:
-dontwarn com.squareup.**


# Dexguard:

# e.g: Encrypt all strings in a class:
#-encryptstrings class com.tedmob...Class

# e.g 2: Encrypt all hardcoded strings in the app with value "Some String":
#-encryptstrings class "Some String"

# e.g 3: Encrypt specific string members inside a class:
#-encryptstrings class com.example.HelloWorldActivity {
#    private static final java.lang.String MESSAGE;
# }

# -keepresourcefiles res/navigation/**.xml
# -keepresources drawable/ic_stat_onesignal_default