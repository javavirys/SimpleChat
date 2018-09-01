# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html


# Add this global rule
-keepattributes Signature

-keepclassmembers class com.example.javavirys.socketiochat.list.models.** {
  *;
}
