package com.gurpster.octopus

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import android.os.Parcelable
import android.provider.Settings.Secure
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.gurpster.octopus.extensions.getTimeStamp
import com.gurpster.octopus.extensions.removeWhitespaces
import com.gurpster.octopus.extensions.screenSize
import com.gurpster.octopus.extensions.toUUID
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class Sensor(
    val id: Int,
    val name: String,
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
    val bootloader: String = Build.BOOTLOADER,
    val device: String = Build.DEVICE,
    val display: String? = Build.DISPLAY,
    val tags: String = Build.TAGS,
    val type: String = Build.TYPE,
    val user: String = Build.USER,
    val androidId: String = Secure.ANDROID_ID,
    val buildTime: Long = Build.TIME,
    val buildDate: String = Build.TIME.getTimeStamp(),
    val radioVersion: String? = Build.getRadioVersion(),
) : Parcelable {

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
        return toString().toUUID()
    }

    override fun toString(): String {
        return "DeviceInfo(_sensor_count=$_sensor_count, _sensors=$_sensors, id='$id', model='$model', manufacturer='$manufacturer', brand='$brand', board='$board', host='$host', hardware='$hardware', product='$product', fingerprint='$fingerprint', release='$release', bootloader='$bootloader', device='$device', display=$display, tags='$tags', type='$type', user='$user', androidId='$androidId', buildTime=$buildTime, buildDate='$buildDate', radioVersion=$radioVersion, sensorManager=$sensorManager, _sensorList=$_sensorList, screenSize='$screenSize', screenWidth=$screenWidth, screenHeight=$screenHeight, sensorList=$sensorList, sensorCount=$sensorCount, sensors='$sensors')"
    }


}
