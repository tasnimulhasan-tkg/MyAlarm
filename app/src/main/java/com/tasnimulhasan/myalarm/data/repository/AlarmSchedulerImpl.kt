package com.tasnimulhasan.myalarm.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tasnimulhasan.myalarm.data.receiver.AlarmReceiver
import com.tasnimulhasan.myalarm.domain.model.Alarm
import com.tasnimulhasan.myalarm.domain.repository.AlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
) : AlarmScheduler {

    override fun schedule(alarm: Alarm) {
        if (!alarm.isEnabled) return
        val triggerTime = if (alarm.repeatDays.isEmpty()) {
            nextTriggerTime(alarm.hour, alarm.minute, null)
        } else {
            val nextDay = nextRepeatDay(alarm.hour, alarm.minute, alarm.repeatDays)
            nextTriggerTime(alarm.hour, alarm.minute, nextDay)
        }
        scheduleExact(alarm.id, triggerTime, alarm)
    }

    override fun cancel(alarm: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context, alarm.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pi)
    }

    override fun rescheduleAll(alarms: List<Alarm>) {
        alarms.filter { it.isEnabled }.forEach { schedule(it) }
    }

    private fun scheduleExact(alarmId: Int, triggerAtMillis: Long, alarm: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.alarmapp.ALARM_TRIGGER"
            putExtra("ALARM_ID", alarm.id)
            putExtra("ALARM_LABEL", alarm.label)
            putExtra("ALARM_VIBRATE", alarm.isVibrate)
            putExtra("ALARM_SNOOZE", alarm.snoozeMinutes)
            putExtra("ALARM_SOUND_URI", alarm.soundUri)
        }
        val pi = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(triggerAtMillis, pi), pi)
            }
        } else {
            alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(triggerAtMillis, pi), pi)
        }
    }

    private fun nextTriggerTime(hour: Int, minute: Int, dayOfWeek: DayOfWeek?): Long {
        val now = LocalDateTime.now()
        var trigger = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (dayOfWeek != null) {
            while (trigger.dayOfWeek != dayOfWeek || !trigger.isAfter(now)) trigger = trigger.plusDays(1)
        } else {
            if (!trigger.isAfter(now)) trigger = trigger.plusDays(1)
        }
        return trigger.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun nextRepeatDay(hour: Int, minute: Int, days: Set<DayOfWeek>): DayOfWeek {
        val now = LocalDateTime.now()
        for (day in days.sortedBy { it.value }) {
            var trigger = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
            while (trigger.dayOfWeek != day) trigger = trigger.plusDays(1)
            if (trigger.isAfter(now)) return day
        }
        return days.sortedBy { it.value }.first()
    }
}