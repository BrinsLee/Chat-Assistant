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

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keep,allowobfuscation,allowshrinking class retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

## Stream Chat Android Client Proguard Rules

# Classes that are using with QuerySort can't be minified, because QuerySort uses reflection. If the
# name of the fields of the classes being used by QuerySort, change, the sort won't work as expected.
-keep class io.getstream.chat.android.models.** { *; }
-keep class io.getstream.chat.android.client.api2.model.** { *; }

# ExtraDataDto can't be minified because we check for extraData using reflection in
# io.getstream.chat.android.client.parser2.adapters.CustomObjectDtoAdapter. If the name of extraData
# is changed, we will have problem with serialization.
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
## Stream Chat Android UI Common Proguard Rules

# Classes that are used by reflection.
-keep class io.getstream.chat.android.ui.common.notifications.StreamCoilUserIconBuilder { *; }
-keep class io.getstream.android.push.permissions.snackbar.SnackbarNotificationPermissionHandler { *; }


# When editing this file, update the following files as well:
# - META-INF/com.android.tools/proguard/coroutines.pro
# - META-INF/com.android.tools/r8/coroutines.pro

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

-keep class com.caverock.androidsvg.SVG {}
-keep class com.caverock.androidsvg.SVGParseException {}
-keep class com.github.chrisbanes.photoview.PhotoView {}
-keeppackagenames com.permissionx.guolindev.**
-keeppackagenames pl.droidsonroids.**
-keeppackagenames com.stfalcon.imageviewer.**

-dontwarn com.caverock.androidsvg.SVG
-dontwarn com.caverock.androidsvg.SVGParseException
-dontwarn com.github.chrisbanes.photoview.PhotoView
-dontwarn com.permissionx.guolindev.PermissionMediator
-dontwarn com.permissionx.guolindev.PermissionX
-dontwarn com.permissionx.guolindev.callback.ExplainReasonCallback
-dontwarn com.permissionx.guolindev.callback.ForwardToSettingsCallback
-dontwarn com.permissionx.guolindev.callback.RequestCallback
-dontwarn com.permissionx.guolindev.request.PermissionBuilder
-dontwarn com.stfalcon.imageviewer.StfalconImageViewer$Builder
-dontwarn com.stfalcon.imageviewer.StfalconImageViewer
-dontwarn com.stfalcon.imageviewer.loader.ImageLoader
-dontwarn pl.droidsonroids.gif.GifDrawable

