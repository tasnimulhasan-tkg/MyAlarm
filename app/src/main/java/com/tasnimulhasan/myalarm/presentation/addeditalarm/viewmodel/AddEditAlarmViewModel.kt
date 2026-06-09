package com.tasnimulhasan.myalarm.presentation.addeditalarm.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasnimulhasan.myalarm.di.AlarmUseCases
import com.tasnimulhasan.myalarm.domain.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

data class AddEditAlarmState(
    val alarmId: Int = -1, val hour: Int = 8, val minute: Int = 0,
    val label: String = "", val repeatDays: Set<DayOfWeek> = emptySet(),
    val isVibrate: Boolean = true, val snoozeMinutes: Int = 5,
    val soundUri: String = "", val isSaved: Boolean = false
)

sealed class AddEditAlarmEvent {
    data class HourChanged(val hour: Int) : AddEditAlarmEvent()
    data class MinuteChanged(val minute: Int) : AddEditAlarmEvent()
    data class LabelChanged(val label: String) : AddEditAlarmEvent()
    data class ToggleRepeatDay(val day: DayOfWeek) : AddEditAlarmEvent()
    data class VibrateChanged(val isVibrate: Boolean) : AddEditAlarmEvent()
    data class SnoozeChanged(val minutes: Int) : AddEditAlarmEvent()
    data class SoundUriChanged(val uri: String) : AddEditAlarmEvent()
    object SaveAlarm : AddEditAlarmEvent()
}

@HiltViewModel
class AddEditAlarmViewModel @Inject constructor(
    private val useCases: AlarmUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditAlarmState())
    val state: StateFlow<AddEditAlarmState> = _state.asStateFlow()

    init {
        val alarmId = savedStateHandle.get<Int>("alarmId") ?: -1
        if (alarmId != -1) {
            viewModelScope.launch {
                useCases.getAlarmById(alarmId)?.let { alarm ->
                    _state.update { it.copy(alarmId = alarm.id, hour = alarm.hour, minute = alarm.minute, label = alarm.label, repeatDays = alarm.repeatDays, isVibrate = alarm.isVibrate, snoozeMinutes = alarm.snoozeMinutes, soundUri = alarm.soundUri) }
                }
            }
        }
    }

    fun onEvent(event: AddEditAlarmEvent) {
        when (event) {
            is AddEditAlarmEvent.HourChanged -> _state.update { it.copy(hour = event.hour) }
            is AddEditAlarmEvent.MinuteChanged -> _state.update { it.copy(minute = event.minute) }
            is AddEditAlarmEvent.LabelChanged -> _state.update { it.copy(label = event.label) }
            is AddEditAlarmEvent.ToggleRepeatDay -> {
                val days = _state.value.repeatDays.toMutableSet()
                if (event.day in days) days.remove(event.day) else days.add(event.day)
                _state.update { it.copy(repeatDays = days) }
            }
            is AddEditAlarmEvent.VibrateChanged -> _state.update { it.copy(isVibrate = event.isVibrate) }
            is AddEditAlarmEvent.SnoozeChanged -> _state.update { it.copy(snoozeMinutes = event.minutes) }
            is AddEditAlarmEvent.SoundUriChanged -> _state.update { it.copy(soundUri = event.uri) }
            is AddEditAlarmEvent.SaveAlarm -> viewModelScope.launch {
                val s = _state.value
                val alarm = Alarm(id = if (s.alarmId == -1) 0 else s.alarmId, hour = s.hour, minute = s.minute, label = s.label, isEnabled = true, repeatDays = s.repeatDays, soundUri = s.soundUri, isVibrate = s.isVibrate, snoozeMinutes = s.snoozeMinutes)
                if (s.alarmId == -1) useCases.addAlarm(alarm) else useCases.updateAlarm(alarm)
                _state.update { it.copy(isSaved = true) }
            }
        }
    }
}