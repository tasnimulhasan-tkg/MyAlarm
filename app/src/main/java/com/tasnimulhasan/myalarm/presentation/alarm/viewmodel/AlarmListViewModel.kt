package com.tasnimulhasan.myalarm.presentation.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasnimulhasan.myalarm.di.AlarmUseCases
import com.tasnimulhasan.myalarm.domain.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlarmListState(val alarms: List<Alarm> = emptyList(), val isLoading: Boolean = false)

sealed class AlarmListEvent {
    data class ToggleAlarm(val alarm: Alarm, val isEnabled: Boolean) : AlarmListEvent()
    data class DeleteAlarm(val alarm: Alarm) : AlarmListEvent()
}

@HiltViewModel
class AlarmListViewModel @Inject constructor(
    private val useCases: AlarmUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmListState(isLoading = true))
    val state: StateFlow<AlarmListState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getAllAlarms().collect { alarms ->
                _state.update { it.copy(alarms = alarms, isLoading = false) }
            }
        }
    }

    fun onEvent(event: AlarmListEvent) {
        when (event) {
            is AlarmListEvent.ToggleAlarm -> viewModelScope.launch {
                useCases.toggleAlarm(event.alarm, event.isEnabled)
            }
            is AlarmListEvent.DeleteAlarm -> viewModelScope.launch {
                useCases.deleteAlarm(event.alarm)
            }
        }
    }
}