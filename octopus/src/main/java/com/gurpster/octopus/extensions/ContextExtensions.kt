package com.gurpster.octopus.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Discouraged
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.annotation.PluralsRes
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.gurpster.octopus.helpers.IntentHelper
import com.gurpster.octopus.helpers.UriHelper
import com.gurpster.octopus.helpers.activityManager
import com.gurpster.octopus.helpers.clipboardManager
import com.gurpster.octopus.helpers.connectivityManager
import com.gurpster.octopus.helpers.locationManager
import com.gurpster.octopus.helpers.notificationManager
import com.gurpster.octopus.helpers.telephonyManager
import java.io.File
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.random.Random.Default.nextInt

/**
 * isOnline{ // Do you work when connected with internet }
+ if you want to execute some code when there is no internet you can pass it as first lambda
isOnline({
// Ofline
}) {
// Online
}
 */
fun Context?.hasInternetConnection(failBlock: () -> Unit = { }, successBlock: () -> Unit = { }) {
    this?.apply {
//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val netInfo = cm.activeNetworkInfo
//        if (netInfo != null && netInfo.isConnected){
//            successBlock()
//        }else{
//            failBlock()
//        }

        /*val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
            if (actNw != null) {
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> successBlock()
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> successBlock()
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> successBlock()
                    else -> failBlock()
                }
            } else failBlock()
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> successBlock()
                        ConnectivityManager.TYPE_MOBILE -> successBlock()
                        ConnectivityManager.TYPE_ETHERNET -> successBlock()
                        else -> failBlock()
                    }

                }
            }
        }*/

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true


    } ?: failBlock()
}

enum class NetworkConnectivityType {
    TYPE_MOBILE, TYPE_WIFI, TYPE_UNKNOWN
}

fun Context.internetType(): NetworkConnectivityType {
    val connectivityMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nc = connectivityMgr.getNetworkCapabilities(connectivityMgr.activeNetwork)
        when {
            nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                NetworkConnectivityType.TYPE_MOBILE
            }

            nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                NetworkConnectivityType.TYPE_WIFI
            }

            else -> NetworkConnectivityType.TYPE_UNKNOWN
        }
    } else {
        val activeNetwork: NetworkInfo? = connectivityMgr.activeNetworkInfo
        when {
            activeNetwork!!.type == ConnectivityManager.TYPE_WIFI -> {
                NetworkConnectivityType.TYPE_WIFI
            }

            activeNetwork.type == ConnectivityManager.TYPE_MOBILE -> {
                NetworkConnectivityType.TYPE_MOBILE
            }

            else -> NetworkConnectivityType.TYPE_UNKNOWN
        }
    }
}

fun Context?.globalInternetFailBock() {
    // show alter to user or implement custom code here
}

/**
 * Is app installed check if some app is installed
 * search by package name
 * @param packageName
 * @return true|false
 */
fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getApplicationInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

@SuppressLint("ObsoleteSdkInt")
fun Context.isDevMode(): Boolean {
    return when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN -> {
            Settings.Secure.getInt(
                contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) != 0
        }

        Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN -> {
            @Suppress("DEPRECATION")
            Settings.Secure.getInt(
                contentResolver,
                Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) != 0
        }

        else -> false
    }
}

/**
 * Screen size
 *
 * <pre>
 * {@code
 * Log.d(TAG, "User's screen size: ${screenSize.x}x${screenSize.y}")
 * }
 * </pre>
 */
val Context.screenSize: Point
    get() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

fun Context.directionsTo(location: Location) {
    val lat = location.latitude
    val lng = location.longitude
    val uri = String.format(Locale.US, "http://maps.google.com/maps?daddr=%f,%f", lat, lng)
    try {
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()

        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        startActivity(intent)
    }
}

/**
 * Vibrate
 * </br>
 * <b>Need to use
 * <pre><uses-permission android:name="android.permission.VIBRATE" /></pre></b> permission</pre>
 * <pre>
 * {@code
 * context.vibrate(500) // 500 ms
 * }
 * </pre>
 * @param duration
 */
fun Context.vibrate(duration: Long) {
    val vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vib.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vib.vibrate(duration)
    }
}

fun Context.showNotificationOngoing(
    id: Int = 0,
    channelName: String,
    title: String,
    contentText: String,
    smallIcon: Int,
) {

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

//    val contentIntent = PendingIntent.getActivity(
//        context, 0,
//        Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
//    )

    val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
        this,
        channelName
    )
        .setContentTitle(title)
        .setContentText(contentText)
        .setSmallIcon(smallIcon)
//        .setContentIntent(contentIntent)
        .setOngoing(true)
//        .setStyle(BigTextStyle().bigText(addressFragments.toString()))
        .setAutoCancel(true)
    notificationManager.notify(id, notificationBuilder.build())
}

/**
 * The application label of this context's package.
 */
@get:CheckResult
inline val Context.applicationLabel: String
    get() = applicationInfo.loadLabel(packageManager).toString()

/**
 * The [PackageInfo] of this context's package.
 * @throws IllegalStateException In case the [PackageInfo] is not found.
 * This should never happen, but for the sake of completeness it is documented.
 */
@get:CheckResult
@get:Throws(IllegalStateException::class)
inline val Context.packageInfo: PackageInfo
    get() {
        return try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            throw IllegalStateException("Failed to find own package '$packageName'.", e)
        }
    }

/**
 * The content of the clipboard's primary clip as text.
 * `null` if the application is not the default input method editor or does not have input focus.
 * @see ClipData.Item.coerceToText
 * @see ClipboardManager.getPrimaryClipDataItem
 */
@get:CheckResult
inline var Context.clipboardTextContent: String?
    get() = clipboardManager.getPrimaryClipDataItem()?.coerceToText(this)?.toString()
    set(value) = clipboardManager.setPrimaryClip(text = value ?: "")

/**
 * `true` if the app is currently at the top of the screen that the user is interacting with.
 * @see RunningAppProcessInfo.IMPORTANCE_FOREGROUND
 */
@get:CheckResult
inline val Context.isAppInForeground: Boolean
    get() {
        return activityManager.runningAppProcesses.orEmpty().any { process ->
            process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.uid == Process.myUid()
        }
    }

/**
 * The current configuration for the application's package resources instance.
 */
@get:CheckResult
inline val Context.configuration: Configuration get() = resources.configuration

/**
 * A [SharedPreferences] instance that points to the default file that is used by
 * the preference framework in the given context.
 * The returned instance points to the same preferences file as the
 * PreferenceManager.getDefaultSharedPreferences(receiver) method from the framework and the
 * AndroidX preferences library.
 * @see Context.getSharedPreferences
 */
@get:CheckResult
inline val Context.defaultSharedPreferences: SharedPreferences
    get() = getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)

/**
 * Opens various settings activities.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param action An action to open a settings screen. Some examples are listed below.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 * @see Settings.ACTION_SETTINGS
 * @see Settings.ACTION_WIRELESS_SETTINGS
 * @see Settings.ACTION_AIRPLANE_MODE_SETTINGS
 * @see Settings.ACTION_ACCESSIBILITY_SETTINGS
 * @see Settings.ACTION_USAGE_ACCESS_SETTINGS
 * @see Settings.ACTION_SECURITY_SETTINGS
 * @see Settings.ACTION_PRIVACY_SETTINGS
 * @see Settings.ACTION_VPN_SETTINGS
 * @see Settings.ACTION_WIFI_SETTINGS
 * @see Settings.ACTION_BLUETOOTH_SETTINGS
 * @see Settings.ACTION_DATE_SETTINGS
 * @see Settings.ACTION_SOUND_SETTINGS
 * @see Settings.ACTION_DISPLAY_SETTINGS
 * @see Settings.ACTION_AUTO_ROTATE_SETTINGS
 * @see Settings.ACTION_APPLICATION_SETTINGS
 * @see Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
 * @see Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
 * @see Settings.ACTION_INTERNAL_STORAGE_SETTINGS
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startSettings(
    action: String = Settings.ACTION_SETTINGS,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startActivityCatchingWithIntentEditor(
        intent = Intent(action),
        intentEditor = intentEditor
    )
}

/**
 * Opens various settings activities for an application.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param action One of the actions below to get to a specific settings screen.
 * @param packageName The package name for which the settings page should be displayed.
 * By default, the package name is determined from the calling context.
 * @param setPackageUri For some actions like [Settings.ACTION_APPLICATION_DETAILS_SETTINGS]
 * it is not enough to specify the package name in the Intent Extras.
 * It must be supplied as a data Uri. Set this parameter to true to do this.
 * See the documentation for each action to find out if the Uri is required or not.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 * @see Settings.ACTION_APP_LOCALE_SETTINGS
 * @see Settings.ACTION_APP_NOTIFICATION_SETTINGS
 * @see Settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS
 * @see Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS
 * @see Settings.ACTION_APP_USAGE_SETTINGS
 * @see Settings.ACTION_APPLICATION_DETAILS_SETTINGS
 * @see Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS
 * @see Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
 * @see Settings.ACTION_MANAGE_OVERLAY_PERMISSION
 * @see Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
 * @see Settings.ACTION_MANAGE_WRITE_SETTINGS
 * @see Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
 * @see Settings.ACTION_REQUEST_MANAGE_MEDIA
 * @see Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
 * @see Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE
 */
@OptIn(ExperimentalContracts::class)
@SuppressLint("InlinedApi")
inline fun Context.startAppSettings(
    action: String = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    packageName: String = getPackageName(),
    setPackageUri: Boolean = action == Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startActivityCatchingWithIntentEditor(
        intent = Intent(action)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            .putExtra(Intent.EXTRA_PACKAGE_NAME, packageName)
            .apply {
                if (setPackageUri) setPackageUri(packageName)
            },
        intentEditor = intentEditor
    )
}

/**
 * Open the app notification channel settings for a specific channel.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param channelId The ID of the channel for which the settings are to be displayed.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
@RequiresApi(Build.VERSION_CODES.O)
inline fun Context.startAppNotificationChannelSettings(
    channelId: String,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startActivityCatchingWithIntentEditor(
        intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            .putExtra(Settings.EXTRA_CHANNEL_ID, channelId),
        intentEditor = intentEditor
    )
}

/**
 * Launch a new activity.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param T The Activity to start.
 * @param options Additional options for how the Activity should be started.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * @see Context.startActivity
 */
@OptIn(ExperimentalContracts::class)
@Throws(ActivityNotFoundException::class)
inline fun <reified T : Activity> Context.startActivity(
    options: Bundle? = null,
    intentEditor: Intent.() -> Unit = {}
) {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    val intent = Intent(this, T::class.java)
    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent.also(intentEditor), options)
}

/**
 * Similar to [Context.startActivity] but instead of throwing an exception,
 * the exception is encapsulated in the returned result.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param intent The description of the activity to start.
 * @param options Additional options for how the Activity should be started.
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 * @see Context.startActivity
 */
fun Context.startActivityCatching(
    intent: Intent,
    options: Bundle? = null
): Result<Unit> {
    if (this !is Activity && (this !is ContextWrapper || this.baseContext !is Activity)) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return kotlin.runCatching {
        startActivity(intent, options)
    }
}

/**
 * Internal version of [startActivityCatching] that takes an [intentEditor] lambda as last parameter.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param intent The description of the activity to start.
 * @param options Additional options for how the Activity should be started.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 * @see Context.startActivity
 * @see Context.startActivityCatching
 */
@OptIn(ExperimentalContracts::class)
@PublishedApi
internal inline fun Context.startActivityCatchingWithIntentEditor(
    intent: Intent,
    options: Bundle? = null,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    if (this !is Activity && (this !is ContextWrapper || this.baseContext !is Activity)) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    intent.also(intentEditor)
    return kotlin.runCatching {
        startActivity(intent, options)
    }
}

/**
 * Opens the Play Store overview page for the given app.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param packageName The unique application id for the desired application.
 * @param referrer Value for the referrer parameter in the URI. The value is encoded by the function.
 * If null, no referrer is added.
 * @param browserAsFallback `true` to try opening the Play Store in the browser (or any other app
 * that can handle the intent) if opening the Play Store app was unsuccessful.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startPlayStoreForApp(
    packageName: String = this.packageName,
    referrer: String? = null,
    browserAsFallback: Boolean = true,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.AT_LEAST_ONCE)
    }
    return startPlayStoreInternal(
        uri = UriHelper.createPlayStoreUriForApp(packageName = packageName, referrer = referrer),
        browserAsFallback = browserAsFallback,
        intentEditor = intentEditor
    )
}

/**
 * Opens the Play Store overview page for the given developer.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param developerName The unique developer id.
 * @param browserAsFallback `true` to try opening the Play Store in the browser (or any other app
 * that can handle the intent) if opening the Play Store app was unsuccessful.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startPlayStoreForDeveloper(
    developerName: String,
    browserAsFallback: Boolean = true,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.AT_LEAST_ONCE)
    }
    return startPlayStoreInternal(
        uri = UriHelper.createPlayStoreUriForDeveloper(developerName = developerName),
        browserAsFallback = browserAsFallback,
        intentEditor = intentEditor
    )
}

/**
 * Internal method to start the Google Play Store.
 * If this fails, the exception is encapsulated in the returned [Result].
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param uri The [Uri] for the launch intent.
 * @param browserAsFallback `true` to start the intent without the specified Play Store package
 * if the start with the specified package fails.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
@PublishedApi
internal inline fun Context.startPlayStoreInternal(
    uri: Uri,
    browserAsFallback: Boolean = true,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.AT_LEAST_ONCE)
    }
    val intent = Intent(Intent.ACTION_VIEW, uri).setPackage("com.android.vending")
    return startActivityCatchingWithIntentEditor(
        intent = intent,
        intentEditor = intentEditor
    ).onFailure {
        if (browserAsFallback) {
            return startActivityCatchingWithIntentEditor(
                intent = intent.setPackage(null),
                intentEditor = intentEditor
            )
        }
    }
}

/**
 * Displays the test track for an app in Google Play.
 * Note: For opening the Play Store app, this is currently not different from displaying the app
 * overview page.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param packageName The unique application id for the desired application.
 * @param startBrowser `true` to open the browser instead of the app. Open Play Store app (`false`)
 * is default.
 * @param useFallback `true` to also try the other method (browser or app) if the primary method
 * was not successful. If startBrowser is `true`, the app is opened as fallback and vice versa.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 * @see UriCompanion.createPlayStoreTestTrackUriForApp
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startPlayStoreForTestTrack(
    packageName: String = this.packageName,
    startBrowser: Boolean = false,
    useFallback: Boolean = true,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.AT_LEAST_ONCE)
    }
    if (startBrowser) {
        return startBrowser(
            url = UriHelper.createPlayStoreTestTrackUriForApp(packageName = packageName),
            intentEditor = intentEditor
        ).onFailure {
            if (useFallback) {
                return startPlayStoreForApp(
                    packageName = packageName,
                    browserAsFallback = false,
                    intentEditor = intentEditor
                )
            }
        }
    } else {
        return startPlayStoreForApp(
            packageName = packageName,
            browserAsFallback = false,
            intentEditor = intentEditor
        ).onFailure {
            if (useFallback) {
                return startBrowser(
                    url = UriHelper.createPlayStoreTestTrackUriForApp(packageName = packageName),
                    intentEditor = intentEditor
                )
            }
        }
    }
}

/**
 * Open a website in the user's default browser.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param url The url of the website to open.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startBrowser(url: Uri, intentEditor: Intent.() -> Unit = {}): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startActivityCatchingWithIntentEditor(
        intent = Intent(Intent.ACTION_VIEW, url),
        intentEditor = intentEditor
    )
}

/**
 * Open a website in the user's default browser.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param url The url of the website to open.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startBrowser(url: String, intentEditor: Intent.() -> Unit = {}): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startActivityCatchingWithIntentEditor(
        intent = Intent(Intent.ACTION_VIEW, url.toUri()),
        intentEditor = intentEditor
    )
}

/**
 * Share plain text and/or an attachment.
 * How the individual parameters are interpreted depends on the activity that the user
 * selects to handle the intent.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param subject The subject to send.
 * @param text The text to send.
 * @param attachment A content URI holding a stream of data to send.
 * @param intentEditor Edit the created [Intent] before it is used to create the chooser.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @param chooserIntentEditor Edit the created chooser [Intent] before it is used to start the chooser.
 * Errors occurring in the [chooserIntentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startSendActivityChooser(
    subject: String? = null,
    text: String? = null,
    attachment: Uri? = null,
    chooserIntentEditor: Intent.() -> Unit = {},
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(chooserIntentEditor, InvocationKind.AT_MOST_ONCE)
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startActivityCatchingWithIntentEditor(
        intent = IntentHelper.createSendIntent(
            subject = subject,
            text = text,
            attachment = attachment
        ).apply(intentEditor).chooser(),
        intentEditor = chooserIntentEditor
    )
}

/**
 * Share plain text and/or an attachment.
 * How the individual parameters are interpreted depends on the activity that the user
 * selects to handle the intent.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param subject The string resource of the subject to send.
 * @param text The string resource of the text to send.
 * @param attachment A content URI holding a stream of data to send.
 * @param intentEditor Edit the created [Intent] before it is used to create the chooser.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @param chooserIntentEditor Edit the created chooser [Intent] before it is used to start the chooser.
 * Errors occurring in the [chooserIntentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startSendActivityChooser(
    @StringRes subject: Int? = null,
    @StringRes text: Int? = null,
    attachment: Uri? = null,
    chooserIntentEditor: Intent.() -> Unit = {},
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(chooserIntentEditor, InvocationKind.AT_MOST_ONCE)
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startSendActivityChooser(
        subject = getStringOrNull(subject),
        text = getStringOrNull(text),
        attachment = attachment,
        chooserIntentEditor = chooserIntentEditor,
        intentEditor = intentEditor
    )
}

/**
 * Start the user's default mail client to create a new mail.
 * If invoked on a non-activity context [Intent.FLAG_ACTIVITY_NEW_TASK] is added automatically.
 * @param subject The subject of the mail.
 * @param body The body of the mail.
 * @param recipient The recipient's mail address.
 * @param attachment A content URI holding a stream of data to send.
 * @param intentEditor Edit the created [Intent] before it is used to start the activity.
 * Errors occurring in the [intentEditor] are thrown further and not encapsulated in the [Result].
 * @return A [Result] object that indicates the result of the action.
 * In case of an error the exception is encapsulated in the [Result].
 * You can use [Result.onFailure] for error handling.
 */
@OptIn(ExperimentalContracts::class)
inline fun Context.startSendMailActivity(
    subject: String? = null,
    body: String? = null,
    recipient: String? = null,
    attachment: Uri? = null,
    intentEditor: Intent.() -> Unit = {}
): Result<Unit> {
    contract {
        callsInPlace(intentEditor, InvocationKind.EXACTLY_ONCE)
    }
    return startActivityCatchingWithIntentEditor(
        intent = IntentHelper.createMailSendIntent(
            subject = subject,
            body = body,
            recipient = recipient,
            attachment = attachment
        ),
        intentEditor = intentEditor
    )
}

/**
 * Same as [Context.unregisterReceiver] but swallows all exceptions that may occur.
 */
fun Context.unregisterReceiverSilent(receiver: BroadcastReceiver) {
    try {
        unregisterReceiver(receiver)
    } catch (_: Throwable) {
    }
}

/**
 * Check whether the given permission is granted for the given process and user id.
 * @param permission The permission to check.
 * @param pid The process id being checked against.
 * @param uid The user id being checked against.
 * @return `true` if the permission is granted.
 * @see Context.checkPermission
 */
@CheckResult
fun Context.isPermissionGranted(
    permission: String,
    pid: Int = Process.myPid(),
    uid: Int = Process.myUid()
): Boolean {
    return checkPermission(permission, pid, uid) == PackageManager.PERMISSION_GRANTED
}

/**
 * @return The [Application] instance if the current context is an application context or a
 * wrapper for the application context.
 */
@CheckResult
fun Context.asApplicationOrNull(): Application? {
    return when (this) {
        is Application -> this
        is ContextWrapper -> baseContext as? Application
        else -> null
    }
}

/**
 * @return The [Activity] instance if the current context is an activity context or a
 * wrapper for an activity context.
 */
@CheckResult
fun Context.asActivityOrNull(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext as? Activity
        else -> null
    }
}

/**
 * Same as [PendingIntent.getActivity] but will set the flag [PendingIntent.FLAG_MUTABLE] and
 * remove the flag [PendingIntent.FLAG_IMMUTABLE].
 * @param intent Intent of the activity to be launched.
 * @param requestCode Request code for the sender.
 * @param flags May be [PendingIntent.FLAG_ONE_SHOT], [PendingIntent.FLAG_NO_CREATE],
 * [PendingIntent.FLAG_CANCEL_CURRENT], [PendingIntent.FLAG_UPDATE_CURRENT] or any of the flags
 * as supported by [Intent.fillIn] to control which unspecified parts of the intent that can be supplied
 * when the actual send happens.
 * @return Returns an existing or new PendingIntent matching the given parameters.
 * May return `null` only if [PendingIntent.FLAG_NO_CREATE] has been set.
 */
@RequiresApi(Build.VERSION_CODES.S)
fun Context.createMutableActivityPendingIntent(
    intent: Intent,
    requestCode: Int = 0,
    flags: Int = 0
): PendingIntent? {
    return PendingIntent.getActivity(
        this,
        requestCode,
        intent,
        flags.and(PendingIntent.FLAG_IMMUTABLE.inv()).or(PendingIntent.FLAG_MUTABLE)
    )
}

/**
 * Same as [PendingIntent.getActivity] but will set the flag [PendingIntent.FLAG_IMMUTABLE] and
 * remove the flag [PendingIntent.FLAG_MUTABLE].
 * @param intent Intent of the activity to be launched.
 * @param requestCode Request code for the sender.
 * @param flags May be [PendingIntent.FLAG_ONE_SHOT], [PendingIntent.FLAG_NO_CREATE],
 * [PendingIntent.FLAG_CANCEL_CURRENT], [PendingIntent.FLAG_UPDATE_CURRENT] or any of the flags
 * as supported by [Intent.fillIn] to control which unspecified parts of the intent that can be supplied
 * when the actual send happens.
 * @return Returns an existing or new PendingIntent matching the given parameters.
 * May return `null` only if [PendingIntent.FLAG_NO_CREATE] has been set.
 */
@SuppressLint("InlinedApi")
fun Context.createImmutableActivityPendingIntent(
    intent: Intent,
    requestCode: Int = 0,
    flags: Int = 0
): PendingIntent? {
    return PendingIntent.getActivity(
        this,
        requestCode,
        intent,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flags.and(PendingIntent.FLAG_MUTABLE.inv())
            .or(PendingIntent.FLAG_IMMUTABLE) else flags
    )
}

/**
 * Get a drawable resource identifier for the given resource name in the current package.
 * @param name The resource name.
 * @return The associated resource identifier. Returns `null` if no matching resource was found.
 */
@SuppressLint("DiscouragedApi")
@DrawableRes
@Discouraged(message = "See Resources.getIdentifier for details.")
fun Context.getDrawableIdByNameOrNull(name: String): Int? {
    val resId = resources.getIdentifier(name, "drawable", packageName)
    return if (resId == ResourcesCompat.ID_NULL) null else resId
}

/**
 * Get a string resource identifier for the given resource name in the current package.
 * @param name The resource name.
 * @return The associated resource identifier. Returns `null` if no matching resource was found.
 */
@StringRes
@SuppressLint("DiscouragedApi")
@Discouraged(message = "See Resources.getIdentifier for details.")
fun Context.getStringIdByNameOrNull(name: String): Int? {
    val resId = resources.getIdentifier(name, "string", packageName)
    return if (resId == ResourcesCompat.ID_NULL) null else resId
}

/**
 * Get a layout resource identifier for the given resource name in the current package.
 * @param name The resource name.
 * @return The associated resource identifier. Returns `null` if no matching resource was found.
 */
@LayoutRes
@SuppressLint("DiscouragedApi")
@Discouraged(message = "See Resources.getIdentifier for details.")
fun Context.getLayoutIdByNameOrNull(name: String): Int? {
    val resId = resources.getIdentifier(name, "layout", packageName)
    return if (resId == ResourcesCompat.ID_NULL) null else resId
}

/**
 * Same as [ContextCompat.getColor].
 * @param id The resource identifier of the color.
 * @return A single color value in the form 0xAARRGGBB.
 * @throws Resources.NotFoundException If [id] does not exist.
 */
@Throws(Resources.NotFoundException::class)
@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

/**
 * Same as [ContextCompat.getColor] but returns null if [id] is null or not valid.
 * @param id The resource identifier of the color.
 * @return A single color value in the form 0xAARRGGBB.
 */
@ColorInt
fun Context.getColorCompatOrNull(@ColorRes id: Int?): Int? {
    return try {
        ContextCompat.getColor(this, id ?: return null)
    } catch (_: Resources.NotFoundException) {
        null
    }
}

/**
 * Same as [AppCompatResources.getColorStateList].
 * @param id The resource identifier of the [ColorStateList].
 * @return A [ColorStateList], or `null` if the resource could not be resolved.
 * @throws Resources.NotFoundException If [id] does not exist.
 * @experimental Consider using [Context.getColorStateListCompatOrNull].
 */
@Throws(Resources.NotFoundException::class)
fun Context.getColorStateListCompat(@ColorRes id: Int): ColorStateList? {
    return AppCompatResources.getColorStateList(this, id)
}

/**
 * Same as [AppCompatResources.getColorStateList] but returns null if [id] is null or not valid.
 * @param id The resource identifier of the [ColorStateList].
 * @return A [ColorStateList], or `null` if the resource could not be resolved or was invalid.
 */
fun Context.getColorStateListCompatOrNull(@ColorRes id: Int?): ColorStateList? {
    return try {
        AppCompatResources.getColorStateList(this, id ?: return null)
    } catch (_: Resources.NotFoundException) {
        null
    }
}

/**
 * Same as [AppCompatResources.getDrawable].
 * @param id The resource identifier of the [Drawable].
 * @return A [Drawable], or `null` if the resource could not be resolved.
 * @throws Resources.NotFoundException If [id] does not exist.
 * @experimental Consider using [Context.getDrawableCompatOrNull].
 */
@Throws(Resources.NotFoundException::class)
fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(this, id)
}

/**
 * Same as [AppCompatResources.getDrawable] but returns null if [id] is null or not valid.
 * @param id The resource identifier of the [Drawable].
 * @return A [Drawable], or `null` if the resource could not be resolved or was invalid.
 */
fun Context.getDrawableCompatOrNull(@DrawableRes id: Int?): Drawable? {
    return try {
        AppCompatResources.getDrawable(this, id ?: return null)
    } catch (_: Resources.NotFoundException) {
        null
    }
}

/**
 * @param id The resource id of the icon.
 * @return An [Icon] pointing to a drawable resource.
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Context.getIcon(@DrawableRes id: Int): Icon {
    return Icon.createWithResource(this, id)
}

/**
 * @param id The resource id of the icon.
 * @return An [IconCompat] pointing to a drawable resource.
 */
fun Context.getIconCompat(@DrawableRes id: Int): IconCompat {
    return IconCompat.createWithResource(this, id)
}

/**
 * Same as [Context.getString] but returns null if [id] is null or not valid.
 * @param id The resource identifier of the [String].
 * @return The [String], or `null` if the resource was invalid.
 */
fun Context.getStringOrNull(@StringRes id: Int?): String? {
    return try {
        getString(id ?: return null)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Calls [Context.startForegroundService] on [Build.VERSION_CODES.O] and above and
 * [Context.startService] on lower versions.
 * @param service Identifies the service to be started.
 * The Intent must be fully explicit (supplying a component name).
 * @return The [ComponentName] of the service that is started or already running. Null if the
 * service does not exist.
 * @throws SecurityException  If the caller does not have permission to access the service or
 * the service can not be found.
 * @throws IllegalStateException If the application is in a state where the service can not be started.
 */
@Throws(SecurityException::class, IllegalStateException::class)
fun Context.startForegroundServiceCompat(service: Intent): ComponentName? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(service)
    } else {
        startService(service)
    }
}

/**
 * Calls [Context.startForegroundService] on [Build.VERSION_CODES.O] and above and
 * [Context.startService] on lower versions.
 * @param T The class of service to start.
 * @return The [ComponentName] of the service that is started or already running. Null if the
 * service does not exist.
 * @throws SecurityException If the caller does not have permission to access the service or
 * the service can not be found.
 * @throws IllegalStateException If the application is in a state where the service can not be started.
 */
@Throws(SecurityException::class, IllegalStateException::class)
inline fun <reified T : Service> Context.startForegroundServiceCompat(): ComponentName? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(Intent(this, T::class.java))
    } else {
        startService(Intent(this, T::class.java))
    }
}

/**
 * Same as Context.startService(Intent(this, T::class.java)).
 * @param T The class of service to start.
 * @return The [ComponentName] of the service that is started or already running. Null if the
 * service does not exist.
 * @throws SecurityException  If the caller does not have permission to access the service or
 * the service can not be found.
 * @throws IllegalStateException If the application is in a state where the service can not be started.
 * @see Context.startService
 */
@Throws(SecurityException::class, IllegalStateException::class)
inline fun <reified T : Service> Context.startService(): ComponentName? {
    return startService(Intent(this, T::class.java))
}

/**
 * Same as calling Context.bindService(Intent(this, T::class.java), connection, flags).
 * @param T The class of service to bind to.
 * @param connection Receives information when the service is started or stopped.
 * @param flags See [Context.bindService] for available flags.
 * @return True if the system is binding to the service. False otherwise.
 * @throws SecurityException If you do not have permission to access the service or the service cannot be found.
 * @see Context.bindService
 */
@Throws(SecurityException::class)
inline fun <reified T : Service> Context.bindService(
    connection: ServiceConnection,
    flags: Int = 0
): Boolean {
    return bindService(Intent(this, T::class.java), connection, flags)
}

/**
 * Check if the Internet connectivity is available
 */
fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

/**
 * get color & drawable resources from context
 */
fun Context.getCompatColor(@ColorRes colorId: Int) =
    ResourcesCompat.getColor(resources, colorId, null)

fun Context.getCompatDrawable(@DrawableRes drawableId: Int) =
    AppCompatResources.getDrawable(this, drawableId)!!

/**
 * copy a text to clipboard
 */
fun Context.copyToClipboard(content: String) {
    val clipboardManager = ContextCompat.getSystemService(this, ClipboardManager::class.java)!!
    val clip = ClipData.newPlainText("clipboard", content)
    clipboardManager.setPrimaryClip(clip)
}

/**
 * show alert dialog
 */
fun Context.showAlertDialog(
    positiveButtonLabel: String = "Ok",
    title: String = "",
    message: String,
    actionOnPositiveButton: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveButtonLabel) { dialog, _ ->
            dialog.cancel()
            actionOnPositiveButton()
        }
    val alert = builder.create()
    alert.show()
}


/**
 * chack activity lifecycle
 */
fun Context.isActivityFinishing(): Boolean {
    return this is Activity && isFinishing
}

fun Context.isActivityDestroyed(): Boolean {
    return this is Activity && isDestroyed
}

/**
 * browse a url with context
 */
//fun Context.browse(url: String, newTask: Boolean = false): Boolean {
//    try {
//        val intent = Intent(ACTION_VIEW) {
//            data = Uri.parse(url)
//            if (newTask) addFlags(FLAG_ACTIVITY_NEW_TASK)
//        }
//        startActivity(intent)
//        return true
//    } catch (e: Exception) {
//        return false
//    }
//}

/**
 * send email with context
 */
//fun Context.email(email: String, subject: String = "", text: String = ""): Boolean {
//    val intent = Intent(ACTION_SENDTO) {
//        data = Uri.parse("mailto:")
//        putExtra(EXTRA_EMAIL, arrayOf(email))
//        if (subject.isNotBlank()) putExtra(EXTRA_SUBJECT, subject)
//        if (text.isNotBlank()) putExtra(EXTRA_TEXT, text)
//    }
//    if (intent.resolveActivity(packageManager) != null) {
//        startActivity(intent)
//        return true
//    }
//    return false
//}


/**
 * make call with context
 */
fun Context.makeCall(number: String): Boolean {
    try {
        val intent = Intent(ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        return false
    }
}

/**
 * send sms with context
 */
//fun Context.sendSms(number: String, text: String = ""): Boolean {
//    try {
//        val intent = Intent(ACTION_VIEW, Uri.parse("sms:$number")) {
//            putExtra("sms_body", text)
//        }
//        startActivity(intent)
//        return true
//    } catch (e: Exception) {
//        return false
//    }
//}

/**
 * get versionCode and versionName
 */
val Context.versionName: String?
    get() = try {
        val pInfo = packageManager.getPackageInfo(packageName, 0);
        pInfo?.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }

val Context.versionCode: Long?
    get() = try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            pInfo?.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            pInfo?.versionCode?.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }

/**
 * get Screen Width Easily
 */
inline val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * get Screen Height Easily
 */
inline val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * shows the toast with a Single Call, Just Provide your [msg] and [length] (Optionally)
 */
fun Context.showToast(msg: String, length: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, msg, length).show()

/**
 * There is No Such Thing name Hard Toast, Its just an AlertDialog which will the [msg] you passed until user cancels it.
 */
fun Context.showToastHard(msg: String, dismissButtonText: String? = null) =
    AlertDialog.Builder(this).setMessage(msg).apply {
        dismissButtonText?.let {
            setPositiveButton(it, null)
        }
    }.show()

val Context?.statusBarHeight: Int
    @SuppressLint("InternalInsetResource")
    get() = if (this == null) 0 else resources.getIdentifier(
        "status_bar_height",
        "dimen",
        "android"
    )
        .let { id -> if (id > 0) dimen(id) else 0 }

val Context.actionBarHeight
    get() = dimen<Int>(actionBarSizeResource)

@SuppressLint("DiscouragedApi")
@JvmOverloads
fun Context?.getNavigationBarHeight(orientation: Int = Configuration.ORIENTATION_PORTRAIT): Int =
    if (this == null) 0 else (if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape").let { name ->
        resources.getIdentifier(name, "dimen", "android").let { id -> if (id > 0) dimen(id) else 0 }
    }

/**
 * RESOURCES
 */

fun Context?.string(@StringRes res: Int, vararg any: Any?) = this?.getString(res, *any).safe()

fun Context?.integer(@IntegerRes res: Int) = this?.resources?.getInteger(res).safe()

fun Context?.color(@ColorRes res: Int) = if (this == null) 0 else ContextCompat.getColor(this, res)

inline fun <reified T : Number> Context?.dimen(@DimenRes res: Int): T =
    (if (this == null) 0f else resources.getDimension(res)).let { dimen ->
        when (T::class) {
            Float::class -> dimen as T
            Int::class -> dimen.toInt() as T
            Double::class -> dimen.toDouble() as T
            Long::class -> dimen.toLong() as T
            Short::class -> dimen.toInt().toShort() as T
            else -> throw IllegalArgumentException("Unknown dimen type")
        }
    }

@SuppressLint("UseCompatLoadingForDrawables", "ObsoleteSdkInt")
fun Context?.drawable(@DrawableRes res: Int): Drawable? =
    when {
        this == null -> null
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> getDrawable(res)
        else -> resources.getDrawable(res)
    }

fun Context?.coloredDrawable(
    @DrawableRes drawableResId: Int,
    @ColorRes filterColorResourceId: Int
): Drawable? =
    drawable(drawableResId).apply {
        this?.setColorFilter(
            color(filterColorResourceId),
            PorterDuff.Mode.SRC_ATOP
        )
    }

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int, vararg args: Any?) =
    if (this == null) "" else resources.getQuantityString(res, quantity, *args)

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int) =
    quantityString(res, quantity, quantity)

fun Context.uriFromResource(@DrawableRes resId: Int): String = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(resources.getResourcePackageName(resId))
    .appendPath(resources.getResourceTypeName(resId))
    .appendPath(resources.getResourceEntryName(resId))
    .build().toString()

val Context.actionBarSizeResource: Int
    get() = getResourceIdAttribute(android.R.attr.actionBarSize)

val Context.selectableItemBackgroundResource: Int
    get() = getResourceIdAttribute(android.R.attr.selectableItemBackground)

val Context.actionBarItemBackgroundResource: Int
    get() = getResourceIdAttribute(android.R.attr.actionBarItemBackground)

fun Context.getResourceIdAttribute(@AttrRes attribute: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.resourceId
}

val Context.inflater
    get() = LayoutInflater.from(this)

@JvmOverloads
fun Context.inflate(
    @LayoutRes layoutRes: Int,
    container: ViewGroup? = null,
    attachToRoot: Boolean = false
) = inflater.inflate(layoutRes, container, attachToRoot)

fun Context.colorString(@ColorRes res: Int) =
    color(res).toUInt().toString(16).let {
        if (it.length == 8) it else buildString {
            for (i in 1..(8 - it.length)) {
                append("0")
            }
            append(it)
        }
    }.let { "#$it" }

val Context.currentLocale: Locale
    get() = resources.configuration.run {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> locales.get(0)
            else -> locale
        }
    }

fun Context.isFirstInstall(): Boolean = try {
    packageManager.getPackageInfo(packageName, 0).run { firstInstallTime == lastUpdateTime }
} catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
    true
}

/**
 * Want to Confirm the User Action? Just call showConfirmationDialog with required + optional params to do so.
 */
fun Context.showConfirmationDialog(
    msg: String,
    onResponse: (result: Boolean) -> Unit,
    positiveText: String = "Yes",
    negetiveText: String = "No",
    cancelable: Boolean = false
) =
    AlertDialog.Builder(this).setMessage(msg)
        .setPositiveButton(positiveText) { _, _ -> onResponse(true) }.setNegativeButton(
            negetiveText
        ) { _, _ -> onResponse(false) }.setCancelable(cancelable).show()

/**
 * Want your user to choose Single thing from a bunch? call showSinglePicker and provide your options to choose from
 */
fun Context.showSinglePicker(
    choices: Array<String>,
    onResponse: (index: Int) -> Unit,
    checkedItemIndex: Int = -1
) =
    AlertDialog.Builder(this).setSingleChoiceItems(choices, checkedItemIndex) { dialog, which ->
        onResponse(which)
        dialog.dismiss()
    }.show()

/**
 * Want your user to choose Multiple things from a bunch? call showMultiPicker and provide your options to choose from
 */
fun Context.showMultiPicker(
    choices: Array<String>,
    onResponse: (index: Int, isChecked: Boolean) -> Unit,
    checkedItems: BooleanArray? = null
) =
    AlertDialog.Builder(this)
        .setMultiChoiceItems(choices, checkedItems) { _, which, isChecked ->
            onResponse(
                which,
                isChecked
            )
        }.setPositiveButton("Done", null)
        .show()

/**
 * Checks if GPS is Enabled or Not. Might Require the Location Permission
 */
fun Context.isGPSEnable(): Boolean =
    this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

/**
 * Check if Internet is Available or not. Requires ACCESS_NETWORK_STATE Permission.
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkAvailable(): Boolean {
    val info = this.connectivityManager.activeNetworkInfo
    return info != null && info.isConnected
}

/**
 * ask the system to scan your file easily with a broadcast.
 */
fun Context.requestMediaScanner(url: String) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val contentUri = Uri.fromFile(File(url))
    mediaScanIntent.data = contentUri
    this.sendBroadcast(mediaScanIntent)
}

/**
 * check for the Permission Easily. call [checkSelfPermissions] with the permissions and we will tell you if you are permitted or not.
 */
fun Context.checkSelfPermissions(permissions: Array<String>): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}

/**
 * check for the Permission Easily. call [checkSelfPermissions] with the permissions and we will tell you if you are permitted or not.
 */
fun Context.checkSelfPermissions(permissions: List<String>): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}

/**
 * Wanna check if you can resolve the intent? Call [isIntentResolvable] with your intent and check it with the ease.
 */
@SuppressLint("QueryPermissionsNeeded")
fun Context.isIntentResolvable(intent: Intent) =
    packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()

/**
 * Want to start third party App? Just get us the package Name and we will Start* It
 *
 * *If App Installed ;)
 */
fun Context.startApp(pName: String) = if (isAppInstalled(pName)) {
    startActivity(packageManager.getLaunchIntentForPackage(pName))
} else {
}

/**
 * Want All the Images from the User Phone?
 *
 * Get them easily with the below method, Make Sure You have READ_EXTERNAL_STORAGE Permission
 */
//@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//fun Context.getAllImagesFromStorage(
//    sortBy: ContentColumns = ContentColumns.DATE_ADDED,
//    order: ContentOrder = ContentOrder.DESCENDING
//): List<String> {
//    val data = mutableListOf<String>()
//    val cursor = contentResolver.query(
//        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        arrayOf(MediaStore.Images.Media.DATA),
//        null,
//        null,
//        sortBy.s + " " + order.s
//    )
//    cursor?.let {
//        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        while (cursor.isClosed.not() && cursor.moveToNext()) {
//            cursor.getString(columnIndexData).let {
//                if (it.toFile().exists()) {
//                    data.add(it)
//                }
//            }
//        }
//        cursor.close()
//    }
//    return data.toList()
//}

/**
 * Want All the Videos from the User Phone?
 *
 * Get them easily with the below method, Make Sure You have READ_EXTERNAL_STORAGE Permission
 */
//@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//fun Context.getAllVideosFromStorage(
//    sortBy: ContentColumns = ContentColumns.DATE_ADDED,
//    order: ContentOrder = ContentOrder.DESCENDING
//): List<String> {
//    val data = mutableListOf<String>()
//    val cursor = contentResolver.query(
//        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//        arrayOf(MediaStore.Video.Media.DATA),
//        null,
//        null,
//        sortBy.s + " " + order.s
//    )
//    cursor?.let {
//        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
//        while (cursor.isClosed.not() && cursor.moveToNext()) {
//            cursor.getString(columnIndexData).let {
//                if (it.toFile().exists()) {
//                    data.add(it)
//                }
//            }
//        }
//        cursor.close()
//    }
//    return data.toList()
//}

/**
 * Want All the Audios from the User Phone?
 *
 * Get them easily with the below method, Make Sure You have READ_EXTERNAL_STORAGE Permission
 */
//@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//fun Context.getAllAudios(
//    sortBy: ContentColumns = ContentColumns.DATE_ADDED,
//    order: ContentOrder = ContentOrder.DESCENDING
//): List<String> {
//    val data = mutableListOf<String>()
//    val cursor = contentResolver.query(
//        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//        arrayOf(MediaStore.Audio.Media.DATA),
//        null,
//        null,
//        sortBy.s + " " + order.s
//    )
//    cursor?.let {
//        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//        while (cursor.isClosed.not() && cursor.moveToNext()) {
//            cursor.getString(columnIndexData).let {
//                if (it.toFile().exists()) {
//                    data.add(it)
//                }
//            }
//        }
//        cursor.close()
//    }
//    return data.toList()
//}

/**
 * get Application Name,
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppName(pName: String = packageName): String {
    return packageManager.getApplicationLabel(packageManager.getApplicationInfo(pName, 0))
        .toString()
}

/**
 * get Application Icon,
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppIcon(pName: String = packageName): Drawable {
    return packageManager.getApplicationInfo(pName, 0).loadIcon(packageManager)
}

/**
 * get Application Size in Bytes
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppSize(pName: String = packageName): Long {
    return packageManager.getApplicationInfo(pName, 0).sourceDir.toFile().length()
}

/**
 * get Application Apk File
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppApk(pName: String = packageName): File {
    return packageManager.getApplicationInfo(pName, 0).sourceDir.toFile()
}


/**
 * get Application Version Name
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppVersionName(pName: String = packageName): String {
    return packageManager.getPackageInfo(pName, 0).versionName ?: ""
}

/**
 * get Application Version Code
 *
 * @property pName the Package Name of the Target Application, Default is Current.
 *
 * Provide Package or will provide the current App Detail
 */
@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppVersionCode(pName: String = packageName): Long {
    return PackageInfoCompat.getLongVersionCode(packageManager.getPackageInfo(pName, 0))
}

/**
 * Checks If the Service Is Runing or Not.
 *
 * @property clazz is the Service Class you want to check for.
 */
fun Context.isServiceRunning(clazz: Class<out Service>): Boolean {
    @Suppress("DEPRECATION")
    return activityManager.getRunningServices(Integer.MAX_VALUE).filter {
        it.service.className == clazz.name
    }.isNotEmpty()
}

/**
 * Send Sms Easily, Opens Default SMS App.
 *
 * @property to the Contact Number
 * @property body the SMS Body
 */
fun Context.sendSMS(to: String = "", body: String) {
    startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto$to")).putExtra("sms_body", body))
}

/**
 * Dials a Number
 *
 * @property number the Number you want to dial
 */
fun Context.dialNumber(number: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
}

/**
 * Open Default Email Client with the [mailID] preSetted
 */
fun Context.sendEmail(mailID: String) {
    startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$mailID")))
}

/**
 * Checks if App is in Background
 */
fun Context.isBackground(pName: String = packageName): Boolean {
    activityManager.runningAppProcesses.forEach {
        @Suppress("DEPRECATION")
        if (it.processName == pName) {
            return it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
        }
    }
    return false
}

/**
 * Creates App ShortCut to the launcher App Drawer
 *
 * @property shortCutName The Name of the SHortCut
 * @property icon the Resource Drawable for the Shortcut
 * @property cls the Activity Class Which Will be Opened from the Shortcut
 */
@Suppress("DEPRECATION")
fun Context.createDeskShortCut(shortCutName: String, icon: Int, cls: Class<out Activity>) {
    val shortcutIntent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
    shortcutIntent.putExtra("duplicate", false)
    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName)
    val ico = Intent.ShortcutIconResource.fromContext(applicationContext, icon)
    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico)
    val intent = Intent(this, cls)
    intent.action = "android.intent.action.MAIN"
    intent.addCategory("android.intent.category.LAUNCHER")
    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent)
    sendBroadcast(shortcutIntent)
}

/**
 * Show Notification Easily, With Just a Single Method
 *
 * @property contentTitle The title of the Notification
 * @property id the Id Of the Notification
 * @property contentText the ContentText Of the Notification
 * @property icon The Small Icon of the Notification
 * @property channelID the ID of the Channel
 * @property channelName the Name of the Channel
 * @property contentInfo the ContentInfo For the Notification
 * @property pendingIntent the pending Intent for the Notification
 * @property content the ContentView for the Notification
 * @property bigContent the BigContentView for the Notification
 * @property autoCancel set to True if want to autoCancel the Notification
 * @property ledColor sets the led argb Color
 * @property isColorized true if you want this Notification as a Colorized Notification
 * @property subText the SubText For the Notification
 * @property priority the priority sets param for Notification
 * @property style the Notification Style
 *
 */
fun Context.showNotification(
    contentTitle: String,
    id: Int = nextInt(),
    contentText: String? = null,
    @DrawableRes icon: Int = android.R.drawable.stat_notify_more,
    channelID: String = "default",
    channelName: String = "Default Notification",
    contentInfo: String? = null,
    pendingIntent: PendingIntent? = null,
    content: RemoteViews? = null,
    bigContent: RemoteViews? = null,
    autoCancel: Boolean = false,
    @ColorInt ledColor: Int = Color.WHITE,
    isColorized: Boolean = false,
    subText: String? = null,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT,
    style: NotificationCompat.Style? = null
) {

    val notification = NotificationCompat.Builder(this, channelID)
        .setContentTitle(contentTitle)
        .setSmallIcon(icon)
        .setContentText(contentText)
        .setContentInfo(contentInfo)
        .setContentIntent(pendingIntent).setContent(content).setCustomBigContentView(bigContent)
        .setAutoCancel(autoCancel)
        .setColor(ledColor)
        .setChannelId(channelID)
        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        .setCategory(NotificationCompat.CATEGORY_EVENT)
        .setColorized(isColorized)
        .setSubText(subText)
        .setPriority(priority)
        .setStyle(style)
        .build()
    val notificationManager = this.notificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(id, notification)
}

/**
 * Creates App ShortCut to the launcher Screen
 *
 * @property shortCutName The Name of the SHortCut
 * @property iconId the Resource Drawable for the Shortcut
 * @property presentIntent the Intent will be fire on Clicking the ShortCut.
 */
fun Context.createShortcut(shortCutName: String, iconId: Int, presentIntent: Intent) {
    val shortcutInfo =
        ShortcutInfoCompat.Builder(
            this, UUID.randomUUID()
                .toString()
        )
            .setIntent(presentIntent)
            .setIcon(
                IconCompat.createWithResource(this, iconId)
            ).setLongLabel(shortCutName).build()

    ShortcutManagerCompat.createShortcutResultIntent(this, shortcutInfo)
}

/**
 * Show Date Picker and Get the Picked Date Easily
 */
fun Context.showDatePicker(
    year: Int,
    month: Int,
    day: Int,
    onDatePicked: (year: Int, month: Int, day: Int) -> Unit
) {
    DatePickerDialog(this, { _, pyear, pmonth, pdayOfMonth ->
        onDatePicked(pyear, pmonth, pdayOfMonth)
    }, year, month, day).show()
}

/**
 * Show the Time Picker and Get the Picked Time Easily
 */
fun Context.showTimePicker(
    currentDate: Date = currentDate(),
    is24Hour: Boolean = false,
    onDatePicked: (hour: Int, minute: Int) -> Unit
) {
    @Suppress("DEPRECATION")
    TimePickerDialog(this, { _, hourOfDay, minute ->
        onDatePicked(hourOfDay, minute)

    }, currentDate.hours, currentDate.minutes, is24Hour).show()
}

/**
 * get Android ID
 */
@SuppressLint("HardwareIds")
fun Context.getAndroidID(): String =
    Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

/**
 * get Device ID a.k.a Android ID
 */
fun Context.getDeviceID() = getAndroidID()

/**
 * get Device IMEI
 *
 * Requires READ_PHONE_STATE Permission
 */
@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getIMEI() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> telephonyManager.imei
    else -> telephonyManager.deviceId
}

/**
 * Starts Activity with the class and extra values
 */
fun Context.startActivity(cls: Class<out Activity>, extras: Bundle) =
    startActivity(Intent(this, cls).putExtras(extras))

/**
 * Starts Service with the class and extra values
 */
fun Context.startService(cls: Class<out Service>, extras: Bundle) =
    startService(Intent(this, cls).putExtras(extras))

/**
 * `true` if USB-Debugging is enabled on the device.
 */
@SuppressLint("ObsoleteSdkInt")
@CheckResult
fun Context.isAdbEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getGlobalIntOrNull(Settings.Global.ADB_ENABLED) == 1
    } else {
        @Suppress("DEPRECATION")
        getSystemIntOrNull(Settings.System.ADB_ENABLED) == 1
    }
}

/**
 * `true` if airplane mode is enabled on the device.
 */
@SuppressLint("ObsoleteSdkInt")
@CheckResult
fun Context.isAirplaneModeEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getGlobalIntOrNull(Settings.Global.AIRPLANE_MODE_ON) == 1
    } else {
        @Suppress("DEPRECATION")
        getSystemIntOrNull(Settings.System.AIRPLANE_MODE_ON) == 1
    }
}

/**
 * `true` if "Always finish activities" is enabled on the device.
 * This means that the the activity manager will aggressively finish activities and processes
 * as soon as they are no longer needed.
 * It is a common source of weird behavior when users enable this option
 * without knowing what it actually does.
 */
@SuppressLint("ObsoleteSdkInt")
@CheckResult
fun Context.isAlwaysFinishActivities(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getGlobalIntOrNull(Settings.Global.ALWAYS_FINISH_ACTIVITIES) == 1
    } else {
        @Suppress("DEPRECATION")
        getSystemIntOrNull(Settings.System.ALWAYS_FINISH_ACTIVITIES) == 1
    }
}

/**
 * `true` if bluetooth is enabled on the device.
 */
@SuppressLint("ObsoleteSdkInt")
@CheckResult
fun Context.isBluetoothEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getGlobalIntOrNull(Settings.Global.BLUETOOTH_ON) == 1
    } else {
        @Suppress("DEPRECATION")
        getSystemIntOrNull(Settings.System.BLUETOOTH_ON) == 1
    }
}

/**
 * `true` if data roaming is enabled on the device.
 */
@CheckResult
fun Context.isDataRoamingEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getGlobalIntOrNull(Settings.Global.DATA_ROAMING) == 1
    } else {
        @Suppress("DEPRECATION")
        getSystemIntOrNull(Settings.System.DATA_ROAMING) == 1
    }
}

/**
 * `true` if the developer options are enabled on the device.
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@CheckResult
fun Context.isDeveloperOptionsEnabled(): Boolean =
    getGlobalIntOrNull(Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1

/**
 * The name of the device
 */
@CheckResult
@RequiresApi(Build.VERSION_CODES.N_MR1)
fun Context.deviceName(): String? = getGlobalStringOrNull(Settings.Global.DEVICE_NAME)

/**
 * `true` if mobile data is enabled on the device.
 */
@CheckResult
fun Context.isMobileDataEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getGlobalIntOrNull("mobile_data") == 1
    } else {
        getSystemIntOrNull("mobile_data") == 1
    }
}

/**
 * `true` if wifi is enabled on the device.
 */
@CheckResult
fun Context.isWifiEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getGlobalIntOrNull(Settings.Global.WIFI_ON) == 1
    } else {
        @Suppress("DEPRECATION")
        getSystemIntOrNull(Settings.System.WIFI_ON) == 1
    }
}

/**
 * `true` if accessibility is enabled.
 */
@CheckResult
fun Context.isAccessibilityEnabled() =
    getSecureIntOrNull(Settings.Secure.ACCESSIBILITY_ENABLED) == 1

/**
 * The current screen brightness between 0 and 255.
 */
//    @get:CheckResult
//    inline val screenBrightness get() = getSystemIntOrNull(Settings.System.SCREEN_BRIGHTNESS)

/**
 * Retrieve an integer settings value from [Settings.Global].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not an integer.
 */
@CheckResult
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.getGlobalIntOrNull(name: String): Int? {
    return try {
        Settings.Global.getInt(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a long settings value from [Settings.Global].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not a long.
 */
@CheckResult
@SuppressLint("ObsoleteSdkInt")
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.getGlobalLongOrNull(name: String): Long? {
    return try {
        Settings.Global.getLong(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a float settings value from [Settings.Global].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not a float.
 */
@SuppressLint("ObsoleteSdkInt")
@CheckResult
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.getGlobalFloatOrNull(name: String): Float? {
    return try {
        Settings.Global.getFloat(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a settings value from [Settings.Global].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found.
 */
@CheckResult
@SuppressLint("ObsoleteSdkInt")
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.getGlobalStringOrNull(name: String): String? {
    return Settings.Global.getString(contentResolver, name)
}

/**
 * Retrieve an integer settings value from [Settings.System].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not an integer.
 */
@CheckResult
fun Context.getSystemIntOrNull(name: String): Int? {
    return try {
        Settings.System.getInt(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a long settings value from [Settings.System].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not a long.
 */
@CheckResult
fun Context.getSystemLongOrNull(name: String): Long? {
    return try {
        Settings.System.getLong(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a float settings value from [Settings.System].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not a float.
 */
@CheckResult
fun Context.getSystemFloatOrNull(name: String): Float? {
    return try {
        Settings.System.getFloat(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a settings value from [Settings.System].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found.
 */
@CheckResult
fun Context.getSystemStringOrNull(name: String): String? {
    return Settings.System.getString(contentResolver, name)
}

/**
 * Retrieve an integer settings value from [Settings.Secure].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not an integer.
 */
@CheckResult
fun Context.getSecureIntOrNull(name: String): Int? {
    return try {
        Settings.Secure.getInt(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a long settings value from [Settings.Secure].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not a long.
 */
@CheckResult
fun Context.getSecureLongOrNull(name: String): Long? {
    return try {
        Settings.Secure.getLong(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a float settings value from [Settings.Secure].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found or the value is not a float.
 */
@CheckResult
fun Context.getSecureFloatOrNull(name: String): Float? {
    return try {
        Settings.Secure.getFloat(contentResolver, name)
    } catch (_: Throwable) {
        null
    }
}

/**
 * Retrieve a settings value from [Settings.Secure].
 * @param name The name of the setting.
 * @return The value of the setting. `null` if the setting was not found.
 */
@CheckResult
fun Context.getSecureStringOrNull(name: String): String {
    return Settings.Secure.getString(contentResolver, name)
}


