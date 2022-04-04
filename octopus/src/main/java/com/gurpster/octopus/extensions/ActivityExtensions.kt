package com.gurpster.sample.extensions

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import com.gurpster.sample.reflections.findClass
import com.gurpster.sample.reflections.getBinding

// private val binding by viewBinding(ActivityMainBinding::inflate)
inline fun <T : ViewBinding> ComponentActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

inline fun <T : ViewBinding> ComponentActivity.viewBindings() = lazy(LazyThreadSafetyMode.NONE) {
    this.findClass().getBinding<T>(layoutInflater)
}

// val firstName by bundleArgs<String>("firstName") // String?
inline fun <reified T : Any> ComponentActivity.bundleArgs(lable: String, defaultvalue: T? = null) =
    lazy {
        val value = intent?.extras?.get(lable)
        if (value is T) value else defaultvalue
    }

