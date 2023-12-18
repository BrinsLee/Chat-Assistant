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
-keep class * extends io.getstream.chat.android.client.api2.model.dto.ExtraDataDto {
    public kotlin.collections.Map extraData;
 }

# Rules necessary for R8 full mode
-keep class io.getstream.chat.android.client.api2.endpoint.** { *; }
-keep class io.getstream.chat.android.client.call.RetrofitCall { *; }
-keep class com.squareup.moshi.JsonReader
-keep class com.squareup.moshi.JsonAdapter
-keep class kotlin.reflect.jvm.internal.* { *; }

# Rules to improve the logs by keeping the names of the classes
-keep class * extends io.getstream.chat.android.client.clientstate.UserState

# Classes that are used by reflection.
-keep class io.getstream.chat.android.client.notifications.ChatPushDelegate { *; }

-keep class io.getstream.chat.android.ui.common.notifications.StreamCoilUserIconBuilder { *; }
-keep class io.getstream.android.push.permissions.snackbar.SnackbarNotificationPermissionHandler { *; }

# 保持 Hilt 注解的类不被混淆
-keep class dagger.hilt.** { *; }
-keepclasseswithmembernames class * {
    @dagger.hilt.* <fields>;
}
-keepclasseswithmembernames class * {
    @dagger.hilt.* <methods>;
}

# 保持注入的类不被混淆
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keep class * extends dagger.hilt.android.internal.managers.ApplicationComponentManager
-keep class * extends dagger.hilt.android.internal.builders.ApplicationComponentBuilder
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
-keep class * extends dagger.hilt.android.internal.testing.TestApplicationComponentManager
-keep class * extends dagger.hilt.android.internal.testing.TestApplicationComponentBuilder
-keep class * extends dagger.hilt.android.internal.testing.MarkThatRulesAreGeneratedP
-keep class * extends dagger.hilt.android.internal.testing.MarkThatRulesAreNotGeneratedP
-keep class * extends dagger.hilt.GeneratesRootInput
-keep class * extends dagger.hilt.android.components.ApplicationComponent
-keep class * extends dagger.hilt.android.components.ActivityComponent
-keep class * extends dagger.hilt.android.components.FragmentComponent
-keep class * extends dagger.hilt.android.components.ViewComponent
-keep class * extends dagger.hilt.android.components.ServiceComponent

# 保持通过 Hilt 注入的 ViewModel 不被混淆
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# 保持 Dagger/Hilt 生成的类不被混淆
-keep class dagger.** { *; }
-keepclasseswithmembernames class * {
    @javax.inject.* <fields>;
}
-keepclasseswithmembernames class * {
    @javax.inject.* <methods>;
}
-keep class javax.inject.** { *; }
-keep class dagger.hilt.internal.aggregatedroot.codegen._root_ide_package_.** { *; }
-keep class dagger.hilt.internal.processedrootsentinel.codegen._root_ide_package_.** { *; }

# 保持 @Inject 构造函数不被混淆
-keepclasseswithmembers class * {
    @javax.inject.Inject <init>(...);
}

-keep class androidx.navigation.** { *; }

# 保持所有与 navigation 相关的视图和碎片
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.view.View
-keep public class * extends androidx.activity.ComponentActivity

# 保持自定义参数类，如果你在 navigation 中使用了
-keepclassmembers class * {
    @androidx.navigation.NavArgs public *;
}

# 保持 viewModels，如果你在 navigation 中使用了
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

-keep,allowobfuscation,allowshrinking class retrofit2.Call
