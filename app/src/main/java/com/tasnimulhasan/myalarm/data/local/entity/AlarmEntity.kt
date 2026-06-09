package com.tasnimulhasan.myalarm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val label: String,
    val isEnabled: Boolean,
    val repeatDays: String,
    val soundUri: String,
    val isVibrate: Boolean,
    val snoozeMinutes: Int,
    val createdAt: Long
)