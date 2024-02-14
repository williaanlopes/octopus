package com.gurpster.octopus.helpers

import android.net.Uri
import androidx.annotation.CheckResult


/**
 * The UriCompanion functions are used internally by NanoKt.
 * They are placed in this object because only very few developers need them directly.
 * The usage is experimental because the object solution is not optimal.
 * It will be resolved as soon as statics are available in Kotlin.
 */
object UriHelper {

    /**
     * Builds an [Uri] to show the application with [packageName] in the Google Play Store.
     * @param packageName The unique application id for the desired application.
     * @param referrer Value for the referrer parameter in the URI. The value is encoded by the function.
     * If null, no referrer is added.
     * @return The created [Uri].
     */
    @CheckResult
    fun createPlayStoreUriForApp(packageName: String, referrer: String? = null): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority("play.google.com")
            .path("store/apps/details")
            .appendQueryParameter("id", packageName)
            .apply {
                if (referrer != null) appendQueryParameter("referrer", referrer)
            }
            .build()
    }

    /**
     * Builds an [Uri] to show the developer with [developerName] in the Google Play Store.
     * @param developerName The unique developer id for the desired developer account.
     * @return The created [Uri].
     */
    @CheckResult
    fun createPlayStoreUriForDeveloper(developerName: String): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority("play.google.com")
            .path("store/apps/developer")
            .appendQueryParameter("id", developerName)
            .build()
    }

    /**
     * Builds an [Uri] to show the Google Play test-track join page for the application with [packageName].
     * @param packageName The unique application id for the desired application.
     * @return The created [Uri].
     */
    @CheckResult
    fun createPlayStoreTestTrackUriForApp(packageName: String): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority("play.google.com")
            .path("apps/testing/$packageName")
            .build()
    }

}
