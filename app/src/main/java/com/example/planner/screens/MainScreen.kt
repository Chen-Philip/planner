package com.example.planner.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.planner.screens.agenda.AgendaScreen
import com.example.planner.screens.calendar.CalendarScreen
import com.example.planner.screens.schedule.ScheduleScreen

sealed class Screen(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    object Agenda : Screen(name = "Agenda", route = "agenda", icon = Icons.Filled.Search)
    object Calendar : Screen(name = "Calendar", route = "calendar", icon = Icons.Rounded.AddCircle)
    object Schedule : Screen(name = "Schedule", route = "schedule", icon = Icons.Rounded.Person)
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Agenda.route
    ) {
        composable(Screen.Agenda.route) { AgendaScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
        composable(Screen.Schedule.route) { ScheduleScreen() }
    }
}
