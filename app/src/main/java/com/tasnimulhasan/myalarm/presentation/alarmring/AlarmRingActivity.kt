package com.tasnimulhasan.myalarm.presentation.alarmring

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tasnimulhasan.myalarm.data.service.AlarmService
import com.tasnimulhasan.myalarm.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlarmRingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val label = intent.getStringExtra("ALARM_LABEL") ?: "Alarm"
        val snoozeMinutes = intent.getIntExtra("ALARM_SNOOZE", 5)
        setContent {
            MyAlarmTheme() {
                AlarmRingScreen(label = label, snoozeMinutes = snoozeMinutes,
                    onDismiss = { sendAction(AlarmService.ACTION_DISMISS); finish() },
                    onSnooze = { sendAction(AlarmService.ACTION_SNOOZE, snoozeMinutes); finish() }
                )
            }
        }
    }

    private fun sendAction(action: String, snoozeMinutes: Int = 5) {
        startService(Intent(this, AlarmService::class.java).apply {
            this.action = action
            putExtra("ALARM_SNOOZE", snoozeMinutes)
        })
    }
}

@Composable
fun AlarmRingScreen(label: String, snoozeMinutes: Int, onDismiss: () -> Unit, onSnooze: () -> Unit) {
    val now = remember { LocalDateTime.now() }
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(1f, 1.15f, infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "scale")
    val glowAlpha by infiniteTransition.animateFloat(0.1f, 0.35f, infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "glow")

    Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Primary.copy(alpha = glowAlpha * 0.4f), AlarmRingBg), radius = 800f)), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            Spacer(Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(now.format(DateTimeFormatter.ofPattern("hh:mm")), style = MaterialTheme.typography.displayLarge.copy(fontSize = 96.sp, fontWeight = FontWeight.Bold, letterSpacing = (-4).sp), color = TextPrimary)
                    Text(now.format(DateTimeFormatter.ofPattern("a")), style = MaterialTheme.typography.headlineMedium, color = Primary, modifier = Modifier.padding(top = 16.dp, start = 8.dp))
                }
                Text(now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")), style = MaterialTheme.typography.titleMedium, color = TextSecondary)
            }

            Box(contentAlignment = Alignment.Center) {
                Box(Modifier.size(200.dp).scale(scale).background(Primary.copy(glowAlpha * 0.3f), CircleShape))
                Box(Modifier.size(160.dp).scale(scale).background(Primary.copy(glowAlpha * 0.5f), CircleShape))
                Box(Modifier.size(120.dp).background(Primary.copy(0.15f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Alarm, contentDescription = null, tint = Primary, modifier = Modifier.size(56.dp).scale(scale))
                }
            }

            if (label.isNotEmpty()) Text(label, style = MaterialTheme.typography.headlineMedium, color = TextPrimary, textAlign = TextAlign.Center)

            Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedButton(onClick = onSnooze, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary)) {
                    Icon(Icons.Outlined.Snooze, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("SNOOZE $snoozeMinutes MIN", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp))
                }
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary)) {
                    Icon(Icons.Outlined.Close, contentDescription = null, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("DISMISS", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp))
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}