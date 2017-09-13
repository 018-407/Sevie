-dontwarn org.apache.**
-dontwarn com.google.common.**
-dontwarn com.mixpanel.**
-dontwarn android.support.v4.**
-dontwarn com.google.android.gms.**
-keep class net.sqlcipher.** { *; }
-keep class android.support.v8.renderscript.** { *; }
-keep public class com.google.android.gms.* { public *; }
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-printmapping mapping.txt