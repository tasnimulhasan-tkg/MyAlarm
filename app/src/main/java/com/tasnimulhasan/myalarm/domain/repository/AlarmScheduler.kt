package com.tasnimulhasan.myalarm.domain.repository

import com.tasnimulhasan.myalarm.domain.model.Alarm

interface AlarmScheduler {
    fun schedule(alarm: Alarm)
    fun cancel(alarm: Alarm)
    fun rescheduleAll(alarms: List<Alarm>)
}