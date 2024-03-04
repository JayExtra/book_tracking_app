package com.dev.james.booktracker.core.utilities

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.DateRange
import timber.log.Timber
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.UUID

fun <T> List<T>.convertToOrganisedString() : String {
    return this.toString().drop(1).dropLast(1)
}

fun String.calculateTimeToLong() : Long {
    val stringArray = this.split(" ")
    return if(stringArray.size > 1 ){
        val hourInMillis = stringArray[0].filter { it.isDigit() }.toLong() * 3600000L
        val minutesInMillis = stringArray[1].filter { it.isDigit() }.toLong() * 60000L
        hourInMillis + minutesInMillis
    }else{
        val timeString = stringArray[0]
        return if (timeString.isHour()){
            timeString.filter { it.isDigit() }.toLong() * 3600000L
        }else{
            timeString.filter { it.isDigit() }.toLong() * 60000L
        }
    }
}

fun prepareGoalString(goalTime : String, condition : String , daysList : List<String>) : String {
    return when(condition){
        "Every day except" -> {
            "Read $goalTime every day except ${daysList.joinWithAnd()}."
        }
        "Select specific days" -> {
            "Read $goalTime on ${daysList.joinWithAnd()}."
        }
        "Weekend only" -> {
            "Read $goalTime on Saturday and Sunday only."
        }
        "Weekdays" -> {
            "Read $goalTime from Monday until Friday only."
        }
        "Every day" -> {
            "Read $goalTime every day."
        }
        else -> {
            ""
        }

    }
}

fun  List<String>.joinWithAnd(): String {
    return when (this.size) {
        0 -> ""
        1 -> this[0]
        else -> "${this.dropLast(1).joinToString(", ")} and ${this.last()}"
    }
}

fun String.isHour() : Boolean {
    val firstChar = this.filter { it.isLetter() }.first()
    return firstChar == 'h'
}

fun generateSecureUUID() : String {
    return UUID.randomUUID().toString()
}

fun generateRandomId(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    val sb = StringBuilder(length)

    for (i in 0 until length) {
        val randomIndex = random.nextInt(chars.length)
        sb.append(chars[randomIndex])
    }

    return sb.toString()
}

fun Long.formatTimeToDHMS() : String {

    val units = arrayOf("d", "h", "m", "s")
    val values = longArrayOf(this / 86400000L,
        this % 86400000L / 3600000L, this % 86400000 % 3600000 / 60000,
        this % 86400000L % 3600000L % 60000L / 1000L)

    val timeStringBuilder = StringBuilder()

    for (i in units.indices) {
        if (values[i] > 0) {
            timeStringBuilder.append("${values[i]}${units[i]} ")
        }
    }
    val formattedTime = timeStringBuilder.toString()

    return formattedTime.ifBlank { "0s" }
}
fun String.splitToDHMS() : String {
    val stringArray = this.trim().split(" ")
    Timber.tag("Extentions").d(stringArray.toString())
    when (stringArray.size) {
        1 -> {
            return stringArray[0]
        }
        2 -> {
            return stringArray[0]
        }
        3 -> {
            return stringArray[0] + stringArray[1]
        }
        4 -> {
            return stringArray[0] + stringArray[2] + stringArray[3]
        }
        else -> {
            return "0s"
        }
    }
}

fun Long.checkDaysTaken() : Int {
    val values = this / 86400000L
    return values.toInt()
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(timeMillis: Long): String {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timeMillis),
        ZoneId.systemDefault()
    )
    val formatter = DateTimeFormatter.ofPattern(
        "hh:mm:ss",
        Locale.getDefault()
    )
    return localDateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.N)
fun Calendar.formatDateToString(toDateOnly : Boolean) : String {
    val pattern = if(!toDateOnly) "dd-MM-yyyy hh:mm a" else "dd-MM-yyyy"
    val formatter = SimpleDateFormat(pattern , Locale.UK)
    return formatter.format(this.time)
}

@RequiresApi(Build.VERSION_CODES.N)
fun getDateRange() : DateRange {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.set(Calendar.DAY_OF_WEEK , Calendar.SUNDAY)
    val daysOfThisWeek = mutableListOf<String>()
    (0..6).forEach { day ->
        daysOfThisWeek.add(day , calendar.formatDateToString(false))
        calendar.add(Calendar.DAY_OF_MONTH , 1)
    }
    Timber.tag("Extensions").d(daysOfThisWeek.toString())
    return DateRange(
        startDate = daysOfThisWeek.first() ,
        endDate = daysOfThisWeek.last()
    )
}

@RequiresApi(Build.VERSION_CODES.N)
fun getWeekRange() : List<String> {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.set(Calendar.DAY_OF_WEEK , Calendar.SUNDAY)
    val daysOfThisWeek = mutableListOf<String>()
    (0..6).forEach { day ->
        daysOfThisWeek.add(day , calendar.formatDateToString(true))
        calendar.add(Calendar.DAY_OF_MONTH , 1)
    }
    Timber.tag("Extensions").d("Days of week => ${daysOfThisWeek.toString()}")
    return daysOfThisWeek
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.formatToDateString() : String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return dateFormatter.format(this)
}


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.calculateDaysPast(): Int {
    val currentDate = LocalDate.now()
    //val daysBetween2 = lastLog.until(currentDate , ChronoUnit.DAYS).toInt()
    val daysBetween = ChronoUnit.DAYS.between(currentDate, this).checkDaysTaken()
    Timber.tag("Extensions").d("Days between ${currentDate.toString()} & $this is => $daysBetween")
    return daysBetween
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.getDayString() : String {
    val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val date = LocalDate.parse(this, dateFormat)
    return date.dayOfWeek.toString()
}

fun String.toAppropriateDay() : String {
    return when(this){
        "MONDAY" -> {
            "Mon"
        }
        "TUESDAY" -> {
            "Teu"
        }
        "WEDNESDAY" -> {
            "Wed"
        }
        "THURSDAY" -> {
            "Thur"
        }
        "FRIDAY" -> {
            "Fri"
        }
        "SATURDAY" -> {
            "Sat"
        }
        "SUNDAY" -> {
            "Sun"
        }

        else -> {
            ""
        }
    }
}
