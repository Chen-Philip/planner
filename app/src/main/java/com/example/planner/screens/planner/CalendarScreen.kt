package com.example.planner.screens.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.planner.domain.viewmodel.CalendarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel =
) {

    Column {
        Text(
            text = "Calendar Screen"
        )
        Button(onClick = { viewModel.addTask() }) {

        }
    }
    
}