package com.tasnimulhasan.myalarm.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tasnimulhasan.myalarm.data.service.AlarmService
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("ALARM_ID", intent.getIntExtra("ALARM_ID", -1))
            putExtra("ALARM_LABEL", intent.getStringExtra("ALARM_LABEL") ?: "Alarm")
            putExtra("ALARM_VIBRATE", intent.getBooleanExtra("ALARM_VIBRATE", true))
            putExtra("ALARM_SNOOZE", intent.getIntExtra("ALARM_SNOOZE", 5))
            putExtra("ALARM_SOUND_URI", intent.getStringExtra("ALARM_SOUND_URI") ?: "")
        }
        context.startForegroundService(serviceIntent)
    }
}