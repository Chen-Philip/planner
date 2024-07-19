package com.example.planner.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.planner.domain.viewmodel.MainScreenViewModel
import com.example.planner.screens.planner.AgendaScreen
import com.example.planner.screens.planner.CalendarScreen
import com.example.planner.screens.planner.ScheduleScreen

sealed class Screen(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    data object Agenda : Screen(name = "Agenda", route = "agenda", icon = Icons.Filled.Search)
    data object Calendar : Screen(name = "Calendar", route = "calendar", icon = Icons.Rounded.AddCircle)
    data object Schedule : Screen(name = "Schedule", route = "schedule", icon = Icons.Rounded.Person)
}

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = { AddTaskFloatingActionButton(mainScreenViewModel) }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
private fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Agenda.route
    ) {
        composable(Screen.Agenda.route) { AgendaScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
        composable(Screen.Schedule.route) { ScheduleScreen() }
    }
}

@Composable
private fun BottomNavBar(navController: NavHostController) {
    val navBarItems = listOf(Screen.Agenda, Screen.Calendar, Screen.Schedule)
    NavigationBar {
        val currentRoute = getCurrentRoute(navController = navController)
        navBarItems.forEach{
            val isSelected =  it.route == currentRoute
            NavigationBarItem(
                icon = {
                    Icon(imageVector = it.icon, contentDescription = "temp")
                },
                onClick = {
                    if (!isSelected)
                        navController.navigate(it.route)
                },
                selected = isSelected
            )
        }
    }
}

@Composable
private fun AddTaskFloatingActionButton(
    mainScreenViewModel: MainScreenViewModel
) {
    FloatingActionButton(
        onClick = { mainScreenViewModel.addTask() },
    ) {
        Icon(Icons.Filled.Add, "Add task")
    }
}

// Helper Functions
@Composable
private fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
