package com.tasnimulhasan.myalarm.presentation.addeditalarm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tasnimulhasan.myalarm.presentation.addeditalarm.components.DaySelector
import com.tasnimulhasan.myalarm.presentation.addeditalarm.components.TimePickerWheel
import com.tasnimulhasan.myalarm.presentation.addeditalarm.viewmodel.AddEditAlarmEvent
import com.tasnimulhasan.myalarm.presentation.addeditalarm.viewmodel.AddEditAlarmViewModel
import com.tasnimulhasan.myalarm.ui.theme.*

@Composable
fun AddEditAlarmScreen(onBack: () -> Unit, viewModel: AddEditAlarmViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.isSaved) { if (state.isSaved) onBack() }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            // Top bar
            Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Surface, Background))).padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary) }
                    Text(if (state.alarmId != -1) "EDIT ALARM" else "NEW ALARM", style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 3.sp), color = Primary)
                    Spacer(Modifier.size(48.dp))
                }
            }

            Spacer(Modifier.height(8.dp))
            TimePickerWheel(state.hour, state.minute, { viewModel.onEvent(AddEditAlarmEvent.HourChanged(it)) }, { viewModel.onEvent(AddEditAlarmEvent.MinuteChanged(it)) }, Modifier.padding(horizontal = 24.dp))
            Spacer(Modifier.height(28.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Label
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceVariant)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Label, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
                        OutlinedTextField(
                            value = state.label, onValueChange = { viewModel.onEvent(AddEditAlarmEvent.LabelChanged(it)) },
                            placeholder = { Text("Alarm label", color = TextDisabled) },
                            singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedBorderColor = Primary, unfocusedBorderColor = Color.Transparent, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, cursorColor = Primary),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                // Repeat days
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceVariant)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Outlined.Repeat, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Repeat", style = MaterialTheme.typography.titleMedium, color = TextSecondary) }
                        Spacer(Modifier.height(12.dp))
                        DaySelector(
                            state.repeatDays,
                            onDayToggle = {
                                viewModel.onEvent(AddEditAlarmEvent.ToggleRepeatDay(it))
                            }
                        )
                    }
                }
                // Vibrate
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceVariant)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Outlined.Vibration, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Vibrate", style = MaterialTheme.typography.titleMedium, color = TextSecondary) }
                        Switch(checked = state.isVibrate, onCheckedChange = { viewModel.onEvent(AddEditAlarmEvent.VibrateChanged(it)) }, colors = SwitchDefaults.colors(checkedThumbColor = OnPrimary, checkedTrackColor = Primary, uncheckedTrackColor = SurfaceVariant))
                    }
                }
                // Snooze
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceVariant)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Outlined.Snooze, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Snooze: ${state.snoozeMinutes} min", style = MaterialTheme.typography.titleMedium, color = TextSecondary) }
                        Slider(value = state.snoozeMinutes.toFloat(), onValueChange = { viewModel.onEvent(AddEditAlarmEvent.SnoozeChanged(it.toInt())) }, valueRange = 1f..30f, steps = 28, colors = SliderDefaults.colors(thumbColor = Primary, activeTrackColor = Primary, inactiveTrackColor = Outline))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("1 min", style = MaterialTheme.typography.labelMedium, color = TextDisabled); Text("30 min", style = MaterialTheme.typography.labelMedium, color = TextDisabled) }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Button(onClick = { viewModel.onEvent(AddEditAlarmEvent.SaveAlarm) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary)) {
                Text(if (state.alarmId != -1) "UPDATE ALARM" else "SET ALARM", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp))
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}