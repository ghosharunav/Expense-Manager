

// ============================================================
// STEP 2: ROOM TYPE CONVERTERS
// ============================================================
// File: data/local/Converters.kt
package com.example.expensemanager.data.local

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters for Room Database.
 * Converts Date objects to Long timestamps and vice versa
 * so Room can store Date objects in SQLite database.
 */
class Converters {

    /**
     * Convert timestamp (Long) to Date object
     * Used when reading from database
     *
     * @param value Timestamp in milliseconds
     * @return Date object or null
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Convert Date object to timestamp (Long)
     * Used when writing to database
     *
     * @param date Date object
     * @return Timestamp in milliseconds or null
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

// Usage Note:
// Room will automatically use these converters for any Date fields
// in your Entity classes when @TypeConverters is applied to the Database class