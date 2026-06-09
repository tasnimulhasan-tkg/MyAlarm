package com.tasnimulhasan.myalarm.presentation.alarm

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasnimulhasan.myalarm.presentation.alarm.components.AlarmCard
import com.tasnimulhasan.myalarm.presentation.alarm.viewmodel.AlarmListEvent
import com.tasnimulhasan.myalarm.presentation.alarm.viewmodel.AlarmListViewModel
import com.tasnimulhasan.myalarm.ui.theme.Background
import com.tasnimulhasan.myalarm.ui.theme.OnPrimary
import com.tasnimulhasan.myalarm.ui.theme.Outline
import com.tasnimulhasan.myalarm.ui.theme.Primary
import com.tasnimulhasan.myalarm.ui.theme.Surface
import com.tasnimulhasan.myalarm.ui.theme.TextDisabled
import com.tasnimulhasan.myalarm.ui.theme.TextSecondary

@Composable
fun AlarmListScreen(
    onAddAlarm: () -> Unit,
    onEditAlarm: (Int) -> Unit,
    viewModel: AlarmListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Surface, Background))).padding(horizontal = 24.dp, vertical = 20.dp)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Alarm, contentDescription = null, tint = Primary, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("ALARMS", style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp, letterSpacing = 4.sp), color = Primary)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${state.alarms.count { it.isEnabled }} active", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
                state.alarms.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Alarm, contentDescription = null, tint = Outline, modifier = Modifier.size(72.dp))
                        Spacer(Modifier.height(20.dp))
                        Text("No alarms set", style = MaterialTheme.typography.titleLarge, color = TextSecondary)
                        Spacer(Modifier.height(8.dp))
                        Text("Tap + to add your first alarm", style = MaterialTheme.typography.bodyMedium, color = TextDisabled)
                    }
                }
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.alarms, key = { it.id }) { alarm ->
                        AnimatedVisibility(visible = true, enter = slideInVertically(tween(300)) + fadeIn()) {
                            AlarmCard(
                                alarm = alarm,
                                onToggle = { viewModel.onEvent(AlarmListEvent.ToggleAlarm(alarm, it)) },
                                onDelete = { viewModel.onEvent(AlarmListEvent.DeleteAlarm(alarm)) },
                                onEdit = { onEditAlarm(alarm.id) }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddAlarm,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = Primary, contentColor = OnPrimary, shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Alarm", modifier = Modifier.size(28.dp))
        }
    }
}