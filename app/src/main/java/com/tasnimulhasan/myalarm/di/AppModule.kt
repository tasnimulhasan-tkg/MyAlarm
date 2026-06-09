package com.tasnimulhasan.myalarm.di

import android.app.AlarmManager
import android.content.Context
import androidx.room.Room
import com.tasnimulhasan.myalarm.data.local.AlarmDatabase
import com.tasnimulhasan.myalarm.data.local.dao.AlarmDao
import com.tasnimulhasan.myalarm.data.repository.AlarmRepositoryImpl
import com.tasnimulhasan.myalarm.data.repository.AlarmSchedulerImpl
import com.tasnimulhasan.myalarm.domain.repository.AlarmRepository
import com.tasnimulhasan.myalarm.domain.repository.AlarmScheduler
import com.tasnimulhasan.myalarm.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideAlarmDatabase(@ApplicationContext context: Context): AlarmDatabase =
        Room.databaseBuilder(context, AlarmDatabase::class.java, AlarmDatabase.DATABASE_NAME).build()

    @Provides @Singleton
    fun provideAlarmDao(db: AlarmDatabase): AlarmDao = db.alarmDao

    @Provides @Singleton
    fun provideAlarmRepository(dao: AlarmDao): AlarmRepository = AlarmRepositoryImpl(dao)

    @Provides @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager
    ): AlarmScheduler = AlarmSchedulerImpl(context, alarmManager)

    @Provides @Singleton
    fun provideAlarmUseCases(
        repository: AlarmRepository,
        scheduler: AlarmScheduler
    ): AlarmUseCases = AlarmUseCases(
        getAllAlarms = GetAllAlarmsUseCase(repository),
        getAlarmById = GetAlarmByIdUseCase(repository),
        addAlarm = AddAlarmUseCase(repository, scheduler),
        updateAlarm = UpdateAlarmUseCase(repository, scheduler),
        deleteAlarm = DeleteAlarmUseCase(repository, scheduler),
        toggleAlarm = ToggleAlarmUseCase(repository, scheduler)
    )
}

data class AlarmUseCases(
    val getAllAlarms: GetAllAlarmsUseCase,
    val getAlarmById: GetAlarmByIdUseCase,
    val addAlarm: AddAlarmUseCase,
    val updateAlarm: UpdateAlarmUseCase,
    val deleteAlarm: DeleteAlarmUseCase,
    val toggleAlarm: ToggleAlarmUseCase
)