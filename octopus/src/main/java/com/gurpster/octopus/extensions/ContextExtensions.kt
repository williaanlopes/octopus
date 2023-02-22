package com.gurpster.octopus.extensions

import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import java.util.*

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

fun Context.isDevMode(): Boolean {
    return when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN -> {
            Settings.Secure.getInt(contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
        }
        Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN -> {
            @Suppress("DEPRECATION")
            Settings.Secure.getInt(contentResolver,
                Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
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

