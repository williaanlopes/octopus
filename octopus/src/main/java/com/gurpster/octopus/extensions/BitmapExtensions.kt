package com.gurpster.octopus.extensions

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.core.graphics.createBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Invokes [block] with the receiver as argument, returns the result and calls [Bitmap.recycle]
 * even when an exception is thrown. Exceptions are passed through.
 * @return The result of [block].
 */
@OptIn(ExperimentalContracts::class)
inline fun <R> Bitmap.use(block: (Bitmap) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    try {
        return block(this)
    } finally {
        recycle()
    }
}

/**
 * Decode a byte array of compressed image data to a [Bitmap].
 * @param offset Offset in the receiver to start parsing.
 * @param length The number of bytes to parse.
 * @param options Options that control down sampling and whether the image should be completely decoded.
 * @return The decoded bitmap, or null if the receiver could not be decoded or the option
 * [BitmapFactory.Options.inJustDecodeBounds] was set.
 * @throws IllegalArgumentException In case of incompatible options.
 * See [BitmapFactory.decodeByteArray] for details.
 * @throws IndexOutOfBoundsException If offset or length are negative
 * or more bytes are to be read than the receiver is long (taking into account the offset)
 */
@Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
fun ByteArray.decodeToBitmap(
    offset: Int = 0,
    length: Int = size,
    options: BitmapFactory.Options? = null
): Bitmap? {
    return BitmapFactory.decodeByteArray(this, offset, length, options)
}

/**
 * Returns Bitmap Width And Height Presented as a Pair of two Int where pair.first is width and pair.second is height
 */
fun Bitmap.size(): Pair<Int, Int> = Pair(width, height)

/**
 * Save Bitmap to the provided Path.
 * Make Sure you have the permission to write the file to.
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
@Deprecated(
    "Replace this with sibling function named save(ctx,file,...",
    level = DeprecationLevel.ERROR
)
fun Bitmap.save(
    to: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    recycle: Boolean = true
): Boolean =
    FileOutputStream(File(to)).use {
        this.compress(format, quality, it)
        it.flush()
        it.close()
        if (recycle)
            recycle()
        true
    }

/**
 * Save Bitmap to the provided Path.
 * Make Sure you have the permission to write the file to.
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun Bitmap.save(
    context: Context,
    to: File,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    recycle: Boolean = true
): Boolean? =
    getOutputStream(context, to)?.use {
        this.compress(format, quality, it)
        it.flush()
        it.close()
        if (recycle)
            recycle()
        true
    }

/**
 * Save Bitmap to the provided Path <b>Asynchronously</b> and private a callback when its done.
 *
 * Make Sure you have the permission to write the file to.
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
@Deprecated(
    message = "Replace this with sibling function named saveAsync(ctx,file,...",
    level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith("false")
)
fun Bitmap.saveAsync(
    to: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    recycle: Boolean = true,
    onComplete: ((isSaved: String) -> Unit)? = null
) = false

/**
 * Save Bitmap to the provided Path <b>Asynchronously</b> and private a callback when its done.
 *
 * Make Sure you have the permission to write the file to.
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
fun Bitmap.saveAsync(
    context: Context,
    to: File,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    recycle: Boolean = true,
    onComplete: ((saved: File?) -> Unit)? = null
) = asyncAwait({
    this.save(context, to, format, quality, recycle)
}, {
    onComplete?.invoke(to)
})

/**
 * get Pixel from Bitmap Easily
 */
operator fun Bitmap.get(x: Int, y: Int) = getPixel(x, y)

/**
 * get Pixel to Bitmap Easily
 */
operator fun Bitmap.set(x: Int, y: Int, pixel: Int) = setPixel(x, y, pixel)

/**
 * Crop image easily.
 * @param r is the Rect to crop from the Bitmap
 *
 * @return cropped #android.graphics.Bitmap
 */
fun Bitmap.crop(r: Rect) = if (Rect(0, 0, width, height).contains(r))
    Bitmap.createBitmap(
        this,
        r.left,
        r.top,
        r.width(),
        r.height()
    ) else null

/**
 * Converts Bitmap to Base64 Easily.
 */
fun Bitmap.toBase64(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): String {
    val result: String
    val baos = ByteArrayOutputStream()
    compress(compressFormat, 100, baos)
    baos.flush()
    baos.close()
    val bitmapBytes = baos.toByteArray()
    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    baos.flush()
    baos.close()
    return result
}

/**
 * resize Bitmap With a ease. Just call [resize] with the [w] and [h] and you will get new Resized Bitmap
 */
fun Bitmap.resize(w: Number, h: Number, recycle: Boolean = true): Bitmap {
    val width = width
    val height = height
    val scaleWidth = w.toFloat() / width
    val scaleHeight = h.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    if (width > 0 && height > 0) {
        val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        if (recycle && isRecycled.not() && newBitmap != this)
            recycle()
        return newBitmap
    }
    return this
}

/**
 * rotate Bitmap With a ease. Just call [rotateTo] with the [angle] and you will get new Resized Bitmap
 */
fun Bitmap.rotateTo(angle: Float, recycle: Boolean = true): Bitmap {
    val matrix = Matrix()
    matrix.setRotate(angle)
    val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    if (recycle && isRecycled.not() && newBitmap != this)
        recycle()
    return newBitmap
}

/**
 * Makes the Bitmap Round with given params
 */
fun Bitmap.toRound(borderSize: Float = 0f, borderColor: Int = 0, recycle: Boolean = true): Bitmap {
    val width = width
    val height = height
    val size = Math.min(width, height)
    val paint = Paint(ANTI_ALIAS_FLAG)
    val ret = createBitmap(width, height, config!!)
    val center = size / 2f
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    rectF.inset((width - size) / 2f, (height - size) / 2f)
    val matrix = Matrix()
    matrix.setTranslate(rectF.left, rectF.top)
    if (width != height) {
        matrix.preScale(size.toFloat() / width, size.toFloat() / height)
    }
    val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    shader.setLocalMatrix(matrix)
    paint.shader = shader
    val canvas = Canvas(ret)
    canvas.drawRoundRect(rectF, center, center, paint)
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize
        val radius = center - borderSize / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
    if (recycle && !isRecycled && ret != this) recycle()
    return ret
}

/**
 * Blend the Bitmap Corners to Round with Given radius
 */
fun Bitmap.toRoundCorner(
    radius: Float,
    borderSize: Float = 0f,
    @ColorInt borderColor: Int = 0,
    recycle: Boolean
): Bitmap {
    val width = width
    val height = height
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val ret = createBitmap(width, height, config!!)
    val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    paint.shader = shader
    val canvas = Canvas(ret)
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    val halfBorderSize = borderSize / 2f
    rectF.inset(halfBorderSize, halfBorderSize)
    canvas.drawRoundRect(rectF, radius, radius, paint)
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawRoundRect(rectF, radius, radius, paint)
    }
    if (recycle && !isRecycled && ret != this) recycle()
    return ret
}

/**
 * Want the Image to GreyScale? Just call [toGrayScale] and get the grey Image.
 */
fun Bitmap.toGrayScale(recycle: Boolean): Bitmap? {
    val ret = createBitmap(width, height, config!!)
    val canvas = Canvas(ret)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = colorMatrixColorFilter
    canvas.drawBitmap(this, 0f, 0f, paint)
    if (recycle && !isRecycled && ret != this) recycle()
    return ret
}

/**
 * Converts Bitmap to ByteArray Easily.
 */
fun Bitmap.toByteArray(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(compressFormat, 100, stream)
    return stream.toByteArray()
}

/**
 * Compress Bitmap by Sample Size
 */
fun Bitmap.compressBySampleSize(
    maxWidth: Int,
    maxHeight: Int,
    recycle: Boolean = true
): Bitmap? {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val bytes = baos.toByteArray()
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    if (recycle && !isRecycled) recycle()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
}

/**
 * Compress Bitmap by Quality
 */
fun Bitmap.compressByQuality(
    quality: Int,
    recycle: Boolean = true
): Bitmap? {
    val baos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, baos)
    val bytes = baos.toByteArray()
    if (recycle && !isRecycled) recycle()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}


//***********Private Methods Are below**********************
private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    maxWidth: Int,
    maxHeight: Int
): Int {
    var height = options.outHeight
    var width = options.outWidth
    var inSampleSize = 1
    do {
        width = width shr 1
        height = height shr 1
        val bool = width >= maxWidth && height >= maxHeight
        if (bool.not())
            break
        else
            inSampleSize = inSampleSize shl 1
    } while (true)
    return inSampleSize
}

private fun getOutputStream(mContext: Context, file: File): OutputStream? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val picDir = if (file.absolutePath.contains("DCIM", false))
            "DCIM"
        else if (file.absolutePath.contains("Pictures", false))
            "Pictures"
        else
            throw RuntimeException("allowed directories are [DCIM, Pictures] if Android >= 10")
        val resolver = mContext.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/${file.extension}");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, file.parent?.run {
            substring(indexOf(picDir))
        })
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        imageUri?.let { resolver.openOutputStream(it) }
    } else {
        FileOutputStream(file)
    }
}