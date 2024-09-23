package com.example.planner.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.time.LocalDate
import java.util.Calendar

sealed class Screen(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    data object Agenda : Screen(name = "Agenda", route = "agenda", icon = Icons.Filled.Search)
    data object Calendar : Screen(name = "Calendar", route = "calendar", icon = Icons.Rounded.AddCircle)
    data object Schedule : Screen(name = "Schedule", route = "schedule", icon = Icons.Rounded.Person)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val showAddTaskDialog = remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = { AddTaskFloatingActionButton(mainScreenViewModel, showAddTaskDialog) }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavigationGraph(navController = navController, mainScreenViewModel = mainScreenViewModel)
        }
        if (showAddTaskDialog.value) {
            AddTaskDialog(
                currentDate = localDateToMillis(mainScreenViewModel.date.value),
                onDismissRequest = { showAddTaskDialog.value = false },
                onConfirmationRequest = { task ->
                    mainScreenViewModel.addTask(task)
                    showAddTaskDialog.value = false
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun NavigationGraph(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Agenda.route
    ) {
        composable(Screen.Agenda.route) { AgendaScreen(mainScreenViewModel =  mainScreenViewModel) }
        composable(Screen.Calendar.route) {
            CalendarScreen(
                mainScreenViewModel =  mainScreenViewModel,
                navController = navController
            )
        }
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
    mainScreenViewModel: MainScreenViewModel,
    showAddTaskDialog: MutableState<Boolean>
) {
    FloatingActionButton(
        onClick = {
            showAddTaskDialog.value = true
        },
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

@RequiresApi(Build.VERSION_CODES.O)
private fun localDateToMillis(date: LocalDate): Long {
    val tempCalendar = Calendar.getInstance()
    // todo fix this: gives one month ahead for some reason
    tempCalendar.set(date.year, date.monthValue - 1, date.dayOfMonth)
    return tempCalendar.timeInMillis
}
