package com.gurpster.octopus.extensions


fun Math.ruleOfThree(target: Double, total: Double): Double {
    return target * (total / 100)
}

fun Math.ruleOfThree(target: Long, total: Long): Long {
    return target * (total / 100)
}

fun Math.ruleOfThree(target: Int, total: Int): Int {
    return ruleOfThree(target.toLong(), total.toLong()).toInt()
}

fun Math.ruleOfThree(target: Float, total: Float): Float {
    return ruleOfThree(target.toDouble(), total.toDouble()).toFloat()
}

fun Math.media(list: LongArray): Long {
    return list.sum() / list.size
}

fun Math.media(list: DoubleArray): Double {
    return list.sum() / list.size
}

fun Math.media(list: IntArray): Int {
    return media(list as LongArray).toInt()
}

fun Math.media(list: FloatArray): Float {
    return media(list as DoubleArray).toFloat()
}


/**
 * Fibonacci
 * fibonacci().take(10).toList()
 */
fun Math.fibonacci() = sequence {
    var terms = Pair(0, 1)
    // this sequence is infinite
    while (true) {
        yield(terms.first)
        terms = Pair(terms.second, terms.first + terms.second)
    }
}

/**
 * Fibonacci
 * fibonacci().take(10).toList()
 */
fun Math.fibonacci(n: Int) =
    (2 until n).fold(1 to 1) { (prev, curr), _ ->
        curr to (prev + curr)
    }.second