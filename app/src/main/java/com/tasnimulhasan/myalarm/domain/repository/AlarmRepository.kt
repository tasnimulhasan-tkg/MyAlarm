package com.tasnimulhasan.myalarm.domain.repository

import com.tasnimulhasan.myalarm.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAllAlarms(): Flow<List<Alarm>>
    suspend fun getAlarmById(id: Int): Alarm?
    suspend fun insertAlarm(alarm: Alarm): Long
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun deleteAlarmById(id: Int)
    suspend fun toggleAlarm(id: Int, isEnabled: Boolean)
}