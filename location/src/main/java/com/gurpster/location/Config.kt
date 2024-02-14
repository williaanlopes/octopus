package com.gurpster.location

private const val LOCATION_REQUEST_INTERVAL = 10000L
private const val LOCATION_REQUEST_FASTEST_INTERVAL = 5000L

data class Config(
    val waitForAccurateLocation: Boolean = true,
    /**
     * Sets an explicit minimum update interval.
     */
    val minUpdateIntervalMillis: Long = LOCATION_REQUEST_FASTEST_INTERVAL,
    /**
     * Sets the maximum time any location update may be delayed,
     * and thus grouped with following updates to enable location batching.
     */
    val maxUpdateDelayMillis: Long = LOCATION_REQUEST_INTERVAL,
    /**
     * Sets the minimum update distance between location updates.
     */
    val minUpdateDistanceMeters: Float = 0f,
    /**
     * Sets the request interval.
     */
    val intervalMillis: Long = 1,
    /**
     * Sets the duration this request will continue before being automatically removed.
     */
    val stopAfterMinutes: Long = Long.MAX_VALUE,
    /**
     * Sets the maximum number of location updates for this request before
     * this request is automatically removed.
     */
    val stopAfterUpdates: Int = Int.MAX_VALUE,
)