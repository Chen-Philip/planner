package com.example.planner.screens.planner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planner.domain.viewmodel.AgendaViewModel

@Composable
fun AgendaScreen(
    agendaViewModel: AgendaViewModel  = hiltViewModel()
) {
    TaskColumn(agendaViewModel)
}

@Composable
private fun TaskColumn(
    agendaViewModel: AgendaViewModel
) {
    val taskList = agendaViewModel.tasks.observeAsState()
    if (taskList.value != null) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(taskList.value!!) { i, task ->
                Text(
                    text = "${task.name} ${task.date}"
                )
            }
        }
    } else {
        Text("Empty")
    }
}