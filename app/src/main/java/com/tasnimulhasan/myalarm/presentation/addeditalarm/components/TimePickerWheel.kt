package com.tasnimulhasan.myalarm.presentation.addeditalarm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasnimulhasan.myalarm.ui.theme.Primary
import com.tasnimulhasan.myalarm.ui.theme.SurfaceVariant
import com.tasnimulhasan.myalarm.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun TimePickerWheel(
    hour: Int, minute: Int,
    onHourChange: (Int) -> Unit, onMinuteChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth().height(200.dp)
            .background(SurfaceVariant, RoundedCornerShape(20.dp)).padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(52.dp).background(
            Brush.horizontalGradient(listOf(Primary.copy(0.08f), Primary.copy(0.15f), Primary.copy(0.08f))),
            RoundedCornerShape(12.dp)
        ))
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            NumberWheel((0..23).toList(), hour, onHourChange, { it.toString().padStart(2, '0') }, Modifier.weight(1f))
            Text(":", style = MaterialTheme.typography.displayMedium.copy(fontSize = 40.sp, fontWeight = FontWeight.Bold), color = Primary, modifier = Modifier.padding(horizontal = 8.dp))
            NumberWheel((0..59).toList(), minute, onMinuteChange, { it.toString().padStart(2, '0') }, Modifier.weight(1f))
        }
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(0f to SurfaceVariant, 0.3f to SurfaceVariant.copy(0f), 0.7f to SurfaceVariant.copy(0f), 1f to SurfaceVariant),
            RoundedCornerShape(20.dp)
        ))
    }
}

@Composable
private fun NumberWheel(items: List<Int>, selected: Int, onSelected: (Int) -> Unit, formatter: (Int) -> String, modifier: Modifier = Modifier) {
    val itemHeight = 52.dp
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = maxOf(0, items.indexOf(selected) - 1))

    LaunchedEffect(selected) {
        val idx = items.indexOf(selected)
        if (idx >= 0) listState.animateScrollToItem(maxOf(0, idx - 1))
    }
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centered = items.getOrNull(listState.firstVisibleItemIndex + 1) ?: items.last()
            if (centered != selected) onSelected(centered)
        }
    }

    LazyColumn(state = listState, modifier = modifier.height(itemHeight * 3), horizontalAlignment = Alignment.CenterHorizontally, contentPadding = PaddingValues(vertical = itemHeight)) {
        items(items.size) { index ->
            val dist = abs(listState.firstVisibleItemIndex + 1 - index)
            val alpha = when (dist) { 0 -> 1f; 1 -> 0.4f; else -> 0.15f }
            val scale = when (dist) { 0 -> 1f; 1 -> 0.85f; else -> 0.7f }
            Box(modifier = Modifier.height(itemHeight).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = formatter(items[index]),
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = (36 * scale).sp, fontWeight = if (dist == 0) FontWeight.Bold else FontWeight.Normal),
                    color = if (dist == 0) Primary else TextPrimary,
                    modifier = Modifier.alpha(alpha)
                )
            }
        }
    }
}