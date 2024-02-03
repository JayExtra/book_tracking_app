package com.dev.james.booktracker.core_database.room.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DateTypeConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromDateToString(date : LocalDate) : String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return date.format(dateFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromStringToDate(string : String) : LocalDate {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return LocalDate.parse(string , dateFormatter)
    }
}