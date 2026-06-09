package com.tasnimulhasan.myalarm.domain.model

import java.time.DayOfWeek

data class Alarm(
    val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val label: String = "",
    val isEnabled: Boolean = true,
    val repeatDays: Set<DayOfWeek> = emptySet(),
    val soundUri: String = "",
    val isVibrate: Boolean = true,
    val snoozeMinutes: Int = 5,
    val createdAt: Long = System.currentTimeMillis()
) {
    val isRepeating: Boolean get() = repeatDays.isNotEmpty()

    fun formattedTime(): String {
        val h = if (hour % 12 == 0) 12 else hour % 12
        val m = minute.toString().padStart(2, '0')
        val period = if (hour < 12) "AM" else "PM"
        return "$h:$m $period"
    }

    fun repeatDaysLabel(): String {
        if (repeatDays.isEmpty()) return "Once"
        if (repeatDays.size == 7) return "Every day"
        val weekdays = setOf(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        )
        val weekends = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        return when (repeatDays) {
            weekdays -> "Weekdays"
            weekends -> "Weekends"
            else -> repeatDays
                .sortedBy { it.value }
                .joinToString(", ") {
                    it.name.take(3).lowercase().replaceFirstChar { c -> c.uppercase() }
                }
        }
    }
}