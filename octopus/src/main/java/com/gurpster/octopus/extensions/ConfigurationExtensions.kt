package com.gurpster.octopus.extensions

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.CheckResult

/**
 * `true` if the screen is at least approximately 480x640 dp units.
 * This corresponds with the large resource qualifier.
 * @param requireXLarge To instead check if the screen is at least approximately 720x960 dp units.
 * This corresponds with the xlarge resource qualifier.
 * @return See description above.
 */
@CheckResult
fun Configuration.isTablet(requireXLarge: Boolean = false): Boolean {
    return screenLayoutSize >= if (requireXLarge) {
        Configuration.SCREENLAYOUT_SIZE_XLARGE
    } else {
        Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}

/**
 * `true` if the Device is displaying in a car dock.
 */
@get:CheckResult
inline val Configuration.isCar get() = uiModeType == Configuration.UI_MODE_TYPE_CAR

/**
 * `true` if the Device is displaying in a desk dock.
 */
@get:CheckResult
inline val Configuration.isDeskDock get() = uiModeType == Configuration.UI_MODE_TYPE_DESK

/**
 * `true` if the Device is displaying on a television,
 * providing a "ten foot" experience where its UI is on a large screen that the user is far away from,
 * primarily oriented around DPAD or other non-pointer interaction.
 */
@get:CheckResult
inline val Configuration.isTelevision get() = uiModeType == Configuration.UI_MODE_TYPE_TELEVISION

/**
 * `true` if the Device is serving as an appliance, with no display.
 */
@get:CheckResult
inline val Configuration.isAppliance get() = uiModeType == Configuration.UI_MODE_TYPE_APPLIANCE

/**
 * `true` if the Device has a display and is worn on the wrist.
 * Returns `false` if the sdk version is below [Build.VERSION_CODES.KITKAT_WATCH].
 */
@get:CheckResult
inline val Configuration.isWatch get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && uiModeType == Configuration.UI_MODE_TYPE_WATCH

/**
 * `true` if the Device is displaying in a virtual reality headset.
 * Returns `false` if the sdk version is below [Build.VERSION_CODES.O].
 */
@get:CheckResult
inline val Configuration.isVrHeadset get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && uiModeType == Configuration.UI_MODE_TYPE_VR_HEADSET

/**
 * `true` if the device screen's aspect ratio is considered long (e.g. WQVGA, WVGA and FWVGA).
 */
@get:CheckResult
inline val Configuration.isLongScreen get() = screenLayoutLong == Configuration.SCREENLAYOUT_LONG_YES

/**
 * Bit mask for the screen's size.
 * @see Configuration.SCREENLAYOUT_SIZE_MASK
 */
@get:CheckResult
inline val Configuration.screenLayoutSize get() = (screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK)

/**
 * Bit mask for the screen's aspect ratio.
 * @see Configuration.SCREENLAYOUT_SIZE_MASK
 */
@get:CheckResult
inline val Configuration.screenLayoutLong get() = (screenLayout and Configuration.SCREENLAYOUT_LONG_MASK)

/**
 * Bit mask for the ui mode type.
 * @see Configuration.UI_MODE_TYPE_MASK
 */
@get:CheckResult
inline val Configuration.uiModeType get() = screenLayout and Configuration.UI_MODE_TYPE_MASK

/**
 * `true` if the configuration is in landscape.
 * @see Configuration.orientation
 */
@get:CheckResult
inline val Configuration.isLandscape get() = orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * `true` if the configuration is in portrait.
 * @see Configuration.orientation
 */
@get:CheckResult
inline val Configuration.isPortrait get() = orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * `true` if the layout direction is set to "left to right".
 * Returns `false` if the sdk version is below [Build.VERSION_CODES.JELLY_BEAN_MR1].
 * @see Configuration.getLayoutDirection
 */
@get:CheckResult
inline val Configuration.isLtrLayout get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == Configuration.SCREENLAYOUT_LAYOUTDIR_LTR

/**
 * `true` if the layout direction is set to "right to left".
 * Returns `false` if the sdk version is below [Build.VERSION_CODES.JELLY_BEAN_MR1].
 * @see Configuration.getLayoutDirection
 */
@get:CheckResult
inline val Configuration.isRtlLayout get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == Configuration.SCREENLAYOUT_LAYOUTDIR_RTL

/**
 * `true` if the system is in night mode.
 */
@get:CheckResult
inline val Configuration.isNightModeActiveCompat: Boolean
    get() = uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES