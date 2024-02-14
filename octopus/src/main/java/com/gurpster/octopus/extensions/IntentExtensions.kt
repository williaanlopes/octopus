package com.gurpster.octopus.extensions

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi

/**
 * True if [Intent.FLAG_ACTIVITY_CLEAR_TOP] is set.
 */
inline var Intent.isClearTop: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_CLEAR_TOP) == Intent.FLAG_ACTIVITY_CLEAR_TOP
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_CLEAR_TOP.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] is set.
 */
inline var Intent.isExcludeFromRecents: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) == Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_FORWARD_RESULT] is set.
 */
inline var Intent.isForwardResult: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_FORWARD_RESULT) == Intent.FLAG_ACTIVITY_FORWARD_RESULT
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_FORWARD_RESULT.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT] is set.
 */
inline var Intent.isLaunchAdjacent: Boolean
    @RequiresApi(Build.VERSION_CODES.N)
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT) == Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
    @RequiresApi(Build.VERSION_CODES.N)
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_MATCH_EXTERNAL] is set.
 */
inline var Intent.isMatchExternal: Boolean
    @RequiresApi(Build.VERSION_CODES.P)
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_MATCH_EXTERNAL) == Intent.FLAG_ACTIVITY_MATCH_EXTERNAL
    @RequiresApi(Build.VERSION_CODES.P)
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_MATCH_EXTERNAL)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_MATCH_EXTERNAL.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] is set.
 */
inline var Intent.isMultipleTask: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) == Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_MULTIPLE_TASK.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_NEW_DOCUMENT] is set.
 */
inline var Intent.isNewDocument: Boolean
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_NEW_DOCUMENT) == Intent.FLAG_ACTIVITY_NEW_DOCUMENT
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_NEW_DOCUMENT.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_NEW_TASK] is set.
 */
inline var Intent.isNewTask: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_NEW_TASK) == Intent.FLAG_ACTIVITY_NEW_TASK
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_NEW_TASK.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_NO_ANIMATION] is set.
 */
inline var Intent.isNoAnimation: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_NO_ANIMATION) == Intent.FLAG_ACTIVITY_NO_ANIMATION
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_NO_ANIMATION.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_NO_HISTORY] is set.
 */
inline var Intent.isNoHistory: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_NO_HISTORY) == Intent.FLAG_ACTIVITY_NO_HISTORY
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_NO_HISTORY)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_NO_HISTORY.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_NO_USER_ACTION] is set.
 */
inline var Intent.isNoUserAction: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_NO_USER_ACTION) == Intent.FLAG_ACTIVITY_NO_USER_ACTION
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_NO_USER_ACTION.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP] is set.
 */
inline var Intent.isPreviousIsTop: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP) == Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_REORDER_TO_FRONT] is set.
 */
inline var Intent.isReorderToFront: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) == Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT] is set.
 */
inline var Intent.isRequireDefault: Boolean
    @RequiresApi(Build.VERSION_CODES.R)
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT) == Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT
    @RequiresApi(Build.VERSION_CODES.R)
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER] is set.
 */
inline var Intent.isRequireNonBrowser: Boolean
    @RequiresApi(Build.VERSION_CODES.R)
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER) == Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
    @RequiresApi(Build.VERSION_CODES.R)
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED] is set.
 */
inline var Intent.isResetTaskIfNeeded: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) == Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS] is set.
 */
inline var Intent.isRetainInRecents: Boolean
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS) == Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_SINGLE_TOP] is set.
 */
inline var Intent.isSingleTop: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_SINGLE_TOP) == Intent.FLAG_ACTIVITY_SINGLE_TOP
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_SINGLE_TOP.inv())
        }
    }

/**
 * True if [Intent.FLAG_ACTIVITY_TASK_ON_HOME] is set.
 */
inline var Intent.isTaskOnHome: Boolean
    @CheckResult
    get() = flags.and(Intent.FLAG_ACTIVITY_TASK_ON_HOME) == Intent.FLAG_ACTIVITY_TASK_ON_HOME
    set(value) {
        flags = if (value) {
            flags.or(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
        } else {
            flags.and(Intent.FLAG_ACTIVITY_TASK_ON_HOME.inv())
        }
    }

/**
 * Set the flag [Intent.FLAG_ACTIVITY_CLEAR_TOP].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setClearTop(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setExcludeFromRecents(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_FORWARD_RESULT].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setForwardResult(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT].
 * @return The receiver object, for chaining multiple calls.
 */
@RequiresApi(Build.VERSION_CODES.N)
fun Intent.setLaunchAdjacent(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_MATCH_EXTERNAL].
 * @return The receiver object, for chaining multiple calls.
 */
@RequiresApi(Build.VERSION_CODES.P)
fun Intent.setMatchExternal(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_MATCH_EXTERNAL)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_MULTIPLE_TASK].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setMultipleTask(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_NEW_DOCUMENT].
 * @return The receiver object, for chaining multiple calls.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Intent.setNewDocument(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_NEW_TASK].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setNewTask(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_NO_ANIMATION].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setNoAnimation(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_NO_HISTORY].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setNoHistory(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_NO_USER_ACTION].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setNoUserAction(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setPreviousIsTop(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_REORDER_TO_FRONT].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setReorderToFront(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT].
 * @return The receiver object, for chaining multiple calls.
 */
@RequiresApi(Build.VERSION_CODES.R)
fun Intent.setRequireDefault(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER].
 * @return The receiver object, for chaining multiple calls.
 */
@RequiresApi(Build.VERSION_CODES.R)
fun Intent.setRequireNonBrowser(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setResetTaskIfNeeded(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS].
 * @return The receiver object, for chaining multiple calls.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Intent.setRetainInRecents(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_SINGLE_TOP].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setSingleTop(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    return this
}

/**
 * Set the flag [Intent.FLAG_ACTIVITY_TASK_ON_HOME].
 * @return The receiver object, for chaining multiple calls.
 */
fun Intent.setTaskOnHome(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
    return this
}

/**
 * Wrap the current intent into a new [Intent.ACTION_CHOOSER] intent.
 * @param title Optional title that will be displayed in the chooser. This does not work with actions
 * [Intent.ACTION_SEND] and [Intent.ACTION_SEND_MULTIPLE].
 * @return The new [Intent.ACTION_CHOOSER] intent.
 */
@CheckResult
fun Intent.chooser(title: CharSequence? = null): Intent = Intent.createChooser(this, title)


/**
 * Set the data of the intent to an uri with the scheme "package"
 * and the [packageName] string as scheme-specific-part.
 * @param packageName The package name to use as scheme-specific-part.
 * @return The receiver object, for chaining multiple calls.
 * into a single statement.
 * @see Intent.setData
 */
fun Intent.setPackageUri(packageName: String): Intent {
    data = Uri.fromParts("package", packageName, null)
    return this
}