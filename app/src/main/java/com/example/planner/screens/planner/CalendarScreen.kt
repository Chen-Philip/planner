package com.example.planner.screens.planner

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planner.domain.viewmodel.CalendarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = viewModel()
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