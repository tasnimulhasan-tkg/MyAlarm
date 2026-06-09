package com.tasnimulhasan.myalarm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tasnimulhasan.myalarm.data.local.dao.AlarmDao
import com.tasnimulhasan.myalarm.data.local.entity.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract val alarmDao: AlarmDao
    companion object { const val DATABASE_NAME = "alarm_db" }
}