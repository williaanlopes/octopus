package com.gurpster.octopus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gurpster.octopus.reflections.getBinding

/**
 * Binding activity
 *
 * Ex: MainActivity : BindingActivity<ActivityMainBinding>()
 *
 * @param V
 * @constructor Create empty Binding activity
 */
open class BindingActivity<V : ViewBinding> : AppCompatActivity() {

    open lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getBinding()
        setContentView(binding.root)
    }
}