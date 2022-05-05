package com.gurpster.octopus.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.gurpster.octopus.R


fun Context.showAlertDialog(
    positiveButtonLable: String = getString(R.string.okay),
    title: String = getString(R.string.app_name), message: String,
    actionOnPositveButton: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveButtonLable) { dialog, _ ->
            dialog.cancel()
            actionOnPositveButton()
        }
    val alert = builder.create()
    alert.show()
}

/**
 * isOnline{ // Do you work when connected with internet }
 + if you want to execute some code when there is no internet you can pass it as first lambda
    isOnline({
        // Ofline
    }) {
        // Online
    }
*/
fun Context?.hasInternetConnection(failBlock : () -> Unit  = { globalInternetFailBock() }, successBlock : () -> Unit ) {
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


    }?:failBlock()
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

fun Context?.globalInternetFailBock(){
    // show alter to user or implement custom code here
}

