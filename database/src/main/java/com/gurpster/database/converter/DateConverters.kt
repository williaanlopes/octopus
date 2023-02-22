package com.gurpster.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.*

@ProvidedTypeConverter
class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromStringTimestamp(value: String): Date {
        return DateFormat.getDateInstance().parse(value) as Date
    }

    @TypeConverter
    fun toStringTimestamp(date: Date): Long {
        return date.time
    }
}