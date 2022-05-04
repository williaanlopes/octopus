package com.gurpster.octopus.extensions

import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.gurpster.octopus.reflections.findClass
import com.gurpster.octopus.reflections.getBinding

// private val binding by viewBinding(ActivityMainBinding::inflate)
inline fun <T : ViewBinding> ComponentActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun <T : ViewBinding> ComponentActivity.viewBindings() = lazy(LazyThreadSafetyMode.NONE) {
    this.findClass().getBinding<T>(layoutInflater)
}

// val firstName by bundleArgs<String>("firstName") // String?
inline fun <reified T : Any> ComponentActivity.bundleArgs(lable: String, defaultvalue: T? = null) =
    lazy {
        val value = intent?.extras?.get(lable)
        if (value is T) value else defaultvalue
    }

fun ComponentActivity.shortToast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun ComponentActivity.longToast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun ComponentActivity.toast(text: String, length: Int) =
    Toast.makeText(this, text, length).show()

