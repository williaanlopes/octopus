//package com.gurpster.octopus.bindingadapters
//
//import android.widget.TextView
//import androidx.appcompat.widget.AppCompatTextView
//import androidx.core.text.PrecomputedTextCompat
//import androidx.core.widget.TextViewCompat
//import androidx.databinding.BindingAdapter
//
//class TextViewBindingAdapter {
//    /** source https://medium.com/androiddevelopers/prefetch-text-layout-in-recyclerview-4acf9103f438
//     * https://developer.android.com/reference/androidx/core/text/PrecomputedTextCompat#getTextFuture(java.lang.CharSequence,%20androidx.core.text.PrecomputedTextCompat.Params,%20java.util.concurrent.Executor)
//     * Async text
//     *  <layout xmlns:tools="http://schemas.android.com/tools"
//     *      xmlns:android="http://schemas.android.com/apk/res/android">
//     *
//     *      <data>
//     *          <variable name="item" type="com.example.ItemData"/>
//     *      </data>
//     *
//     *      <TextView
//     *          android:id="@+id/item_text"
//     *          android:layout_width="wrap_content"
//     *          android:layout_height="wrap_content"
//     *          android:textSize="@{item.isImportant ? 14 : 10}"
//     *          app:asyncText="@{item.text}"/>
//     *  </layout>
//     *
//     * @param view
//     * @param text
//     * @param textSize
//     */
//    @BindingAdapter("app:asyncText", "android:textSize", requireAll = false)
//    fun asyncText(view: TextView, text: CharSequence, textSize: Int?) {
//        // first, set all measurement affecting properties of the text
//        // (size, locale, typeface, direction, etc)
//        if (textSize != null) {
//            // interpret the text size as SP
//            view.textSize = textSize.toFloat()
//        }
//        val params = TextViewCompat.getTextMetricsParams(view)
//        (view as AppCompatTextView).setTextFuture(
//            PrecomputedTextCompat.getTextFuture(
//                text,
//                params,
//                null
//            )
//        )
//    }
//
//}