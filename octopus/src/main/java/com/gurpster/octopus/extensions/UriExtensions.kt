package com.gurpster.octopus.extensions

import android.net.Uri
import androidx.annotation.CheckResult
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

/**
 * Convert an android [Uri] to a java [URI].
 * @return A java [URI] identical to the android [Uri].
 * @throws URISyntaxException If the given [Uri] violates RFC 2396.
 * @see URI
 * @see toJavaUriOrNull
 */
@Throws(URISyntaxException::class)
fun Uri.toJavaUri(): URI = URI(toString())

/**
 * Convert an android [Uri] to a java [URI].
 * @return A java [URI] identical to the android [Uri].
 * If the [Uri] violates RFC 2396 null is returned.
 * @see URI
 * @see toJavaUri
 */
@CheckResult
fun Uri.toJavaUriOrNull(): URI? {
    return try {
        URI(toString())
    } catch (_: Throwable) {
        null
    }
}

/**
 * Convert an android [Uri] to a java [URL].
 * @return A java [URL] identical to the android [Uri].
 * @throws MalformedURLException If the [Uri]'s protocol is not supported.
 * @see URI
 * @see toJavaUrlOrNull
 */
@Throws(MalformedURLException::class)
fun Uri.toJavaUrl(): URL = URL(toString())

/**
 * Convert an android [Uri] to a java [URL].
 * @return A java [URL] identical to the android [Uri].
 * If the [Uri] violates RFC 2396 null is returned.
 * @see URI
 * @see toJavaUrl
 */
@CheckResult
fun Uri.toJavaUrlOrNull(): URL? {
    return try {
        URL(toString())
    } catch (_: Throwable) {
        null
    }
}

/**
 * Convert a java [URI] to an android [Uri].
 * @return An android [Uri] identical to the java [URI].
 * @see URI
 */
@CheckResult
fun URI.toAndroidUri(): Uri = Uri.parse(toString())

/**
 * Convert a java [URL] to an android [Uri].
 * @return An android [Uri] identical to the java [URL].
 * @see URL
 */
@CheckResult
fun URL.toAndroidUri(): Uri = Uri.parse(toString())