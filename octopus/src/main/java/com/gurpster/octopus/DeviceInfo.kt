package com.gurpster.octopus

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.gurpster.octopus.extensions.removeWhitespaces
import com.gurpster.octopus.extensions.screenSize
import com.gurpster.octopus.extensions.toUUID
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlin.collections.ArrayList

data class Sensor(
    val id: Int,
    val name: String
)

@Parcelize
data class DeviceInfo(
    private val _sensor_count: Int = 0,
    private val _sensors: @RawValue List<Sensor> = ArrayList(),

    val id: String = Build.ID,
    val model: String = Build.MODEL,
    val manufacturer: String = Build.MANUFACTURER,
    val brand: String = Build.BRAND,
    val board: String = Build.BOARD,
    val host: String = Build.HOST,
    val hardware: String = Build.HARDWARE,
    val product: String = Build.PRODUCT,
    val fingerprint: String = Build.FINGERPRINT,
    val release: String = Build.VERSION.RELEASE,
): Parcelable {

    @IgnoredOnParcel
    private var sensorManager: SensorManager? = null
    @IgnoredOnParcel
    private var _sensorList: List<android.hardware.Sensor> = ArrayList()

    @IgnoredOnParcel
    var screenSize: String = ""
    @IgnoredOnParcel
    var screenWidth: Int = 0
    @IgnoredOnParcel
    var screenHeight: Int = 0

    constructor(context: Context) : this() {
        sensorManager =
            context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        _sensorList =
            sensorManager?.getSensorList(android.hardware.Sensor.TYPE_ALL) as List<android.hardware.Sensor>

        screenHeight = context.screenSize.x
        screenWidth = context.screenSize.y
        screenSize = "${context.screenSize.x}x${context.screenSize.y}"
    }

    @IgnoredOnParcel
    private val sensorList = _sensorList.map {
        Sensor(
            it.type,
            it.name.removeWhitespaces()
        )
    }

    val sensorCount: Int
        get() = sensorList.size

    val sensors: String
        get() = Gson().toJson(sensorList)

    fun generateUUID(): String {
//        return UUID.nameUUIDFromBytes(toString().encodeToByteArray()).toString()
        return toString().toUUID()
    }

    override fun toString(): String {
        return "DeviceInfo(id='$id', model='$model', manufacturer='$manufacturer', brand='$brand', board='$board', host='$host', hardware='$hardware', product='$product', fingerprint='$fingerprint', release='$release', screenSize='$screenSize', screenWidth=$screenWidth, screenHeight=$screenHeight, sensor_count=$sensorCount, sensors='$sensors')"
    }

}
