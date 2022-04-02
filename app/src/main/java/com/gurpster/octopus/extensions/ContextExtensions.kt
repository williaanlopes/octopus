package com.gurpster.octopus.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gurpster.octopus.R

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.toast(text: String, duration: Int) = Toast.makeText(this, text, duration).show()

fun Context.showAlertDialog(
    positiveButtonLable: String = getString(R.string.okay),
    title: String = getString(R.string.app_name), message: String,
    actionOnPositveButton: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveButtonLable) { dialog, id ->
            dialog.cancel()
            actionOnPositveButton()
        }
    val alert = builder.create()
    alert.show()
}

// Toash extensions
fun Context.showShotToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected){
            successBlock()
        }else{
            failBlock()
        }
    }?:failBlock()
}

fun Context?.globalInternetFailBock(){
    // show alter to user or implement custom code here
}

