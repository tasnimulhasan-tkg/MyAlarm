package com.tasnimulhasan.myalarm.data.repository

import com.tasnimulhasan.myalarm.data.local.dao.AlarmDao
import com.tasnimulhasan.myalarm.data.local.entity.toDomain
import com.tasnimulhasan.myalarm.data.local.entity.toEntity
import com.tasnimulhasan.myalarm.domain.model.Alarm
import com.tasnimulhasan.myalarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val dao: AlarmDao
) : AlarmRepository {
    override fun getAllAlarms(): Flow<List<Alarm>> =
        dao.getAllAlarms().map { it.map { e -> e.toDomain() } }
    override suspend fun getAlarmById(id: Int): Alarm? = dao.getAlarmById(id)?.toDomain()
    override suspend fun insertAlarm(alarm: Alarm): Long = dao.insertAlarm(alarm.toEntity())
    override suspend fun updateAlarm(alarm: Alarm) = dao.updateAlarm(alarm.toEntity())
    override suspend fun deleteAlarm(alarm: Alarm) = dao.deleteAlarm(alarm.toEntity())
    override suspend fun deleteAlarmById(id: Int) = dao.deleteAlarmById(id)
    override suspend fun toggleAlarm(id: Int, isEnabled: Boolean) = dao.toggleAlarm(id, isEnabled)
}