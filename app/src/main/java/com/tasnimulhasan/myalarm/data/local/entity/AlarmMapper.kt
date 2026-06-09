package com.tasnimulhasan.myalarm.data.local.entity

import com.tasnimulhasan.myalarm.domain.model.Alarm
import java.time.DayOfWeek

fun AlarmEntity.toDomain(): Alarm {
    val days = if (repeatDays.isBlank()) emptySet()
    else repeatDays.split(",").mapNotNull { it.trim().toIntOrNull() }
        .map { DayOfWeek.of(it) }.toSet()
    return Alarm(
        id = id, hour = hour, minute = minute, label = label,
        isEnabled = isEnabled, repeatDays = days, soundUri = soundUri,
        isVibrate = isVibrate, snoozeMinutes = snoozeMinutes, createdAt = createdAt
    )
}

fun Alarm.toEntity(): AlarmEntity {
    val daysStr = repeatDays.joinToString(",") { it.value.toString() }
    return AlarmEntity(
        id = id, hour = hour, minute = minute, label = label,
        isEnabled = isEnabled, repeatDays = daysStr, soundUri = soundUri,
        isVibrate = isVibrate, snoozeMinutes = snoozeMinutes, createdAt = createdAt
    )
}