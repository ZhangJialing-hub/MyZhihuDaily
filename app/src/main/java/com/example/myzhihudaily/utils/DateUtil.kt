package com.example.myzhihudaily.utils

/**
 * description:   ToDo:日期显示工具
 * author:zjl
 * email:3507386031@qq.com
 * date:2026/5/2   19：13
 */

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek

object DateUtil {
    fun formatWeekDay(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val date = try {
            LocalDate.parse(dateStr, formatter)
        } catch (e: Exception) {
            return ""
        }

        return when (date.dayOfWeek) {
            DayOfWeek.SUNDAY    -> "星期日"
            DayOfWeek.MONDAY    -> "星期一"
            DayOfWeek.TUESDAY   -> "星期二"
            DayOfWeek.WEDNESDAY -> "星期三"
            DayOfWeek.THURSDAY  -> "星期四"
            DayOfWeek.FRIDAY    -> "星期五"
            DayOfWeek.SATURDAY  -> "星期六"
        }
    }
}