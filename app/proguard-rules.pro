# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# DbFlow
-keep class com.foody.pos.database.** { *; }
-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }
-keep class * extends com.raizlabs.android.dbflow.structure.BaseModel { *; }
-keep class com.raizlabs.android.dbflow.config.GeneratedDatabaseHolder
-keep class * extends com.raizlabs.android.dbflow.config.BaseDatabaseDefinition { *; }


# For OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


# For using GSON @Expose annotation
-keep class * implements foody.com.poswaiter.base.data.entity.GsonModel
-keepattributes *Annotation*
-keepattributes EnclosingMethod
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream { *; }
-keepclassmembers enum * { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# For retrofit 2
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


# Crashlytics 1.+
-keep class com.crashlytics { *; }
-keepattributes SourceFile,LineNumberTable
# Crashlytics 2.+
-keep class com.crashlytics { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile, LineNumberTable, Annotation
# If you are using custom exceptions, add this line so that custom exception types are skipped during obfuscation:
-keep public class * extends java.lang.Exception

# SignalR
-keep class microsoft.aspnet.signalr.** { *; }

#Pager
-keep class foody.com.poswaiter.base.util.config.data.** { *; }

-keep class * implements java.io.Serializable { *; }

-dontwarn sun.misc.Unsafe

#-keep class your.app.data.model.** { *; }