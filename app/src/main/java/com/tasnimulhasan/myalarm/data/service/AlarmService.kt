package com.tasnimulhasan.myalarm.data.service

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.tasnimulhasan.myalarm.R
import com.tasnimulhasan.myalarm.data.receiver.AlarmReceiver
import com.tasnimulhasan.myalarm.presentation.alarmring.AlarmRingActivity

class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var alarmId: Int = -1

    companion object {
        const val CHANNEL_ID = "alarm_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_DISMISS = "com.alarmapp.ACTION_DISMISS"
        const val ACTION_SNOOZE = "com.alarmapp.ACTION_SNOOZE"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DISMISS -> { stopAlarm(); return START_NOT_STICKY }
            ACTION_SNOOZE -> {
                snoozeAlarm(intent.getIntExtra("ALARM_SNOOZE", 5))
                return START_NOT_STICKY
            }
        }
        alarmId = intent?.getIntExtra("ALARM_ID", -1) ?: -1
        val label = intent?.getStringExtra("ALARM_LABEL") ?: "Alarm"
        val shouldVibrate = intent?.getBooleanExtra("ALARM_VIBRATE", true) ?: true
        val snoozeMinutes = intent?.getIntExtra("ALARM_SNOOZE", 5) ?: 5
        val soundUriStr = intent?.getStringExtra("ALARM_SOUND_URI") ?: ""

        startForeground(NOTIFICATION_ID, buildNotification(label, alarmId, snoozeMinutes))
        launchRingActivity(alarmId, label, snoozeMinutes)
        startSound(soundUriStr)
        if (shouldVibrate) startVibration()
        return START_STICKY
    }

    private fun launchRingActivity(alarmId: Int, label: String, snoozeMinutes: Int) {
        val ringIntent = Intent(this, AlarmRingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", label)
            putExtra("ALARM_SNOOZE", snoozeMinutes)
        }
        startActivity(ringIntent)
    }

    private fun startSound(soundUriStr: String) {
        val soundUri = if (soundUriStr.isNotEmpty()) Uri.parse(soundUriStr)
        else RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            setDataSource(applicationContext, soundUri)
            isLooping = true
            prepare()
            start()
        }
    }

    private fun startVibration() {
        val pattern = longArrayOf(0, 500, 500, 500, 500)
        vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
    }

    private fun snoozeAlarm(minutes: Int) {
        stopAlarm()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val snoozeIntent = Intent(this, AlarmReceiver::class.java).apply {
            action = "com.alarmapp.ALARM_TRIGGER"
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", "Snoozed Alarm")
        }
        val pi = PendingIntent.getBroadcast(
            this, alarmId + 1000, snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAt = System.currentTimeMillis() + minutes * 60 * 1000L
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(triggerAt, pi), pi)
    }

    private fun stopAlarm() {
        mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null
        vibrator?.cancel(); vibrator = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(label: String, alarmId: Int, snoozeMinutes: Int): Notification {
        val dismissPi = PendingIntent.getService(
            this, 0, Intent(this, AlarmService::class.java).apply { action = ACTION_DISMISS },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val snoozePi = PendingIntent.getService(
            this, 1, Intent(this, AlarmService::class.java).apply {
                action = ACTION_SNOOZE
                putExtra("ALARM_SNOOZE", snoozeMinutes)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Alarm")
            .setContentText(label.ifEmpty { "Wake up!" })
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .addAction(R.drawable.ic_snooze, "Snooze ${snoozeMinutes}m", snoozePi)
            .addAction(R.drawable.ic_dismiss, "Dismiss", dismissPi)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH).apply {
            setBypassDnd(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}