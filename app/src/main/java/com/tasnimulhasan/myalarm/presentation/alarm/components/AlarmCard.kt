package com.tasnimulhasan.myalarm.presentation.alarm.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasnimulhasan.myalarm.domain.model.Alarm
import com.tasnimulhasan.myalarm.ui.theme.CardDisabled
import com.tasnimulhasan.myalarm.ui.theme.CardEnabled
import com.tasnimulhasan.myalarm.ui.theme.Error
import com.tasnimulhasan.myalarm.ui.theme.OnPrimary
import com.tasnimulhasan.myalarm.ui.theme.Outline
import com.tasnimulhasan.myalarm.ui.theme.Primary
import com.tasnimulhasan.myalarm.ui.theme.SurfaceVariant
import com.tasnimulhasan.myalarm.ui.theme.TextDisabled
import com.tasnimulhasan.myalarm.ui.theme.TextPrimary
import com.tasnimulhasan.myalarm.ui.theme.TextSecondary

@Composable
fun AlarmCard(
    alarm: Alarm,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(if (alarm.isEnabled) CardEnabled else CardDisabled, tween(300), label = "bg")
    val borderColor by animateColorAsState(if (alarm.isEnabled) Primary.copy(alpha = 0.3f) else Outline.copy(alpha = 0.4f), tween(300), label = "border")
    val timeColor by animateColorAsState(if (alarm.isEnabled) TextPrimary else TextDisabled, tween(300), label = "time")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onEdit() }
    ) {
        if (alarm.isEnabled) {
            Box(
                modifier = Modifier.fillMaxWidth().height(2.dp).background(
                    Brush.horizontalGradient(listOf(Color.Transparent, Primary.copy(alpha = 0.6f), Color.Transparent))
                )
            )
        }
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    val (timePart, amPm) = alarm.formattedTime().split(" ")
                    Text(timePart, style = MaterialTheme.typography.displayMedium.copy(fontSize = 40.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp), color = timeColor)
                    Spacer(Modifier.width(6.dp))
                    Text(amPm, style = MaterialTheme.typography.titleMedium, color = if (alarm.isEnabled) TextSecondary else TextDisabled, modifier = Modifier.padding(bottom = 4.dp))
                }
                if (alarm.label.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(alarm.label, style = MaterialTheme.typography.bodyMedium, color = if (alarm.isEnabled) TextSecondary else TextDisabled)
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(if (alarm.isEnabled) Primary.copy(alpha = 0.12f) else SurfaceVariant.copy(alpha = 0.5f)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                        Text(alarm.repeatDaysLabel(), style = MaterialTheme.typography.labelMedium, color = if (alarm.isEnabled) Primary else TextDisabled)
                    }
                    if (alarm.isVibrate) {
                        Icon(Icons.Outlined.Vibration, contentDescription = null, tint = if (alarm.isEnabled) Primary.copy(0.6f) else TextDisabled, modifier = Modifier.size(14.dp))
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(checked = alarm.isEnabled, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedThumbColor = OnPrimary, checkedTrackColor = Primary, uncheckedThumbColor = TextDisabled, uncheckedTrackColor = SurfaceVariant))
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Error.copy(0.6f), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}