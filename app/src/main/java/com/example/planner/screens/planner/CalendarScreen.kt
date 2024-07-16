package com.example.planner.screens.planner

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planner.domain.viewmodel.CalendarViewModel

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel()
) {

    Column {
        Text(
            text = "Calendar Screen"
        )
        Button(onClick = { calendarViewModel.addTask() }) {
            Text(
                text = "Add Task"
            )
        }
    }
    
}