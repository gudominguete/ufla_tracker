package com.ufla.gustavo.uflatracker.database.converter

import androidx.room.TypeConverter
import java.util.*

object TimestampConverter {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Calendar? = value?.let { value ->
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = value
        }
    }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis
}