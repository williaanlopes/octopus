package com.gurpster.octopus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.gurpster.octopus.reflections.getBinding


/**
 * Binding fragment
 *
 * Ex: HomeFragment : BindingFragment<FragmentHomeBinding>()
 *
 * @param V
 * @constructor Create empty Binding fragment
 */
open class BindingFragment<V : ViewBinding> : Fragment() {

    private var _binding: V? = null

    open val binding: V
        get() = _binding
            ?: throw RuntimeException("Should only use binding after onCreateView and before onDestroyView")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}