package com.gurpster.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*

@ProvidedTypeConverter
class UUIDConverters {

    @TypeConverter
    fun toType(string: String): UUID {
        return UUID.fromString(string)
    }

    @TypeConverter
    fun fromType(uuid: UUID) : String {
        return uuid.toString()
    }
}