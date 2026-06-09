package com.tasnimulhasan.myalarm.domain.usecase

import com.tasnimulhasan.myalarm.domain.model.Alarm
import com.tasnimulhasan.myalarm.domain.repository.AlarmRepository
import com.tasnimulhasan.myalarm.domain.repository.AlarmScheduler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAlarmsUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getAllAlarms()
}

class GetAlarmByIdUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Int): Alarm? = repository.getAlarmById(id)
}

class AddAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarm: Alarm): Long {
        val id = repository.insertAlarm(alarm)
        if (alarm.isEnabled) scheduler.schedule(alarm.copy(id = id.toInt()))
        return id
    }
}

class UpdateAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.updateAlarm(alarm)
        scheduler.cancel(alarm)
        if (alarm.isEnabled) scheduler.schedule(alarm)
    }
}

class DeleteAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarm: Alarm) {
        scheduler.cancel(alarm)
        repository.deleteAlarm(alarm)
    }
}

class ToggleAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarm: Alarm, isEnabled: Boolean) {
        repository.toggleAlarm(alarm.id, isEnabled)
        if (isEnabled) scheduler.schedule(alarm.copy(isEnabled = true))
        else scheduler.cancel(alarm)
    }
}