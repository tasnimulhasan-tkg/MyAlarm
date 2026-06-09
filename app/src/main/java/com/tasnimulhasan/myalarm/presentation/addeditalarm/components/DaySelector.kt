package com.tasnimulhasan.myalarm.presentation.addeditalarm.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tasnimulhasan.myalarm.ui.theme.*
import java.time.DayOfWeek

@Composable
fun DaySelector(selectedDays: Set<DayOfWeek>, onDayToggle: (DayOfWeek) -> Unit, modifier: Modifier = Modifier) {
    val days = listOf(DayOfWeek.MONDAY to "M", DayOfWeek.TUESDAY to "T", DayOfWeek.WEDNESDAY to "W", DayOfWeek.THURSDAY to "T", DayOfWeek.FRIDAY to "F", DayOfWeek.SATURDAY to "S", DayOfWeek.SUNDAY to "S")
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        days.forEach { (day, letter) ->
            val isSelected = day in selectedDays
            val bg by animateColorAsState(if (isSelected) Primary else SurfaceVariant, tween(200), label = "bg")
            val textColor by animateColorAsState(if (isSelected) OnPrimary else TextSecondary, tween(200), label = "text")
            val border by animateColorAsState(if (isSelected) Primary else Outline, tween(200), label = "border")
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(bg).border(1.dp, border, CircleShape).clickable { onDayToggle(day) }, contentAlignment = Alignment.Center) {
                Text(letter, style = MaterialTheme.typography.labelLarge, color = textColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}