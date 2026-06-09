package com.tasnimulhasan.myalarm.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import com.tasnimulhasan.myalarm.presentation.addeditalarm.AddEditAlarmScreen
import com.tasnimulhasan.myalarm.presentation.alarm.AlarmListScreen

sealed class Screen(val route: String) {
    object AlarmList : Screen("alarm_list")
    object AddEditAlarm : Screen("add_edit_alarm?alarmId={alarmId}") {
        fun withId(id: Int = -1) = "add_edit_alarm?alarmId=$id"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.AlarmList.route) {
        composable(Screen.AlarmList.route) {
            AlarmListScreen(
                onAddAlarm = { navController.navigate(Screen.AddEditAlarm.withId()) },
                onEditAlarm = { navController.navigate(Screen.AddEditAlarm.withId(it)) }
            )
        }
        composable(
            route = Screen.AddEditAlarm.route,
            arguments = listOf(navArgument("alarmId") { type = NavType.IntType; defaultValue = -1 })
        ) {
            AddEditAlarmScreen(onBack = { navController.popBackStack() })
        }
    }
}