package com.gurpster.octopus.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.gurpster.octopus.reflections.findClass
import com.gurpster.octopus.reflections.getBinding

/**
 * To binding
 *
 * Usage ViewHolder(parent.toBinding())
 *
 * @param V
 * @return
 */
inline fun <reified V : ViewBinding> ViewGroup.toBinding(): V {
    return V::class.java.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    ).invoke(null, LayoutInflater.from(context), this, false) as V

}

fun <V : ViewBinding> ViewGroup.viewBindings(
    inflater: LayoutInflater,
    container: ViewGroup?
): V {
    return findClass().getBinding(inflater, container)
}

/**
 * check if ViewGroup have the given View as Its Child
 */
fun ViewGroup.contains(child: View) = indexOfChild(child) > -1

/**
 * get All the Children's as Iterator
 */
fun ViewGroup.childs() = object : Iterator<View> {
    var index = 0
    override fun hasNext(): Boolean {
        return index < childCount
    }

    override fun next(): View {
        return getChildAt(index++)
    }

}