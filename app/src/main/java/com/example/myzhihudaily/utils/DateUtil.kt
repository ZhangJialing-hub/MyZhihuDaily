package com.example.myzhihudaily.utils

/**
 * description:   ToDo:日期显示工具
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/2   19：13
 */

/**
 * description:   ToDo:将原有的只显示星期几的日期显示工具完善
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/3   16：07
 */

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtil {

    fun formatDate(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val date = try {
            LocalDate.parse(dateStr, formatter)
        } catch (e: Exception) {
            return ""
        }
        val weekday = when (date.dayOfWeek) {
            DayOfWeek.SUNDAY -> "星期日"
            DayOfWeek.MONDAY -> "星期一"
            DayOfWeek.TUESDAY -> "星期二"
            DayOfWeek.WEDNESDAY -> "星期三"
            DayOfWeek.THURSDAY -> "星期四"
            DayOfWeek.FRIDAY -> "星期五"
            DayOfWeek.SATURDAY -> "星期六"
        }
        return "${date.year}年${date.monthValue}月${date.dayOfMonth}日 $weekday"
    }
    fun getPreviousDay(dateStr: String?): String? {
        if (dateStr.isNullOrBlank()) return null
        return try {
            val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val date = sdf.parse(dateStr)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            sdf.format(calendar.time)
        } catch (e: Exception) {
            null
        }
    }
}

