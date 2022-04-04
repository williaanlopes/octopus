package com.gurpster.octopus.helpers

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat

class TextViewHelper {

    companion object {

        /**
         * source: https://medium.com/androiddevelopers/prefetch-text-layout-in-recyclerview-4acf9103f438
         * https://developer.android.com/reference/androidx/core/text/PrecomputedTextCompat#getTextFuture(java.lang.CharSequence,%20androidx.core.text.PrecomputedTextCompat.Params,%20java.util.concurrent.Executor)
         * Async text

         * @param view
         * @param text
         * @param textSize
         */
        fun asyncText(view: TextView, text: CharSequence, textSize: Int?) {
            // first, set all measurement affecting properties of the text
            // (size, locale, typeface, direction, etc)
            if (textSize != null) {
                // interpret the text size as SP
                view.textSize = textSize.toFloat()
            }
            val params = TextViewCompat.getTextMetricsParams(view)
            (view as AppCompatTextView).setTextFuture(
                PrecomputedTextCompat.getTextFuture(
                    text,
                    params,
                    null
                )
            )
        }
    }
}