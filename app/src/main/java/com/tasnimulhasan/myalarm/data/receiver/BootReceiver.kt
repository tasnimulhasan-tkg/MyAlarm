package com.tasnimulhasan.myalarm.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tasnimulhasan.myalarm.data.local.dao.AlarmDao
import com.tasnimulhasan.myalarm.data.local.entity.toDomain
import com.tasnimulhasan.myalarm.domain.repository.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var alarmDao: AlarmDao
    @Inject lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.LOCKED_BOOT_COMPLETED"
        ) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val alarms = alarmDao.getEnabledAlarms().map { it.toDomain() }
                    alarmScheduler.rescheduleAll(alarms)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}