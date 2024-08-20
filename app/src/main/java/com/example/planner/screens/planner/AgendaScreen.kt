package com.example.planner.screens.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planner.domain.viewmodel.AgendaViewModel
import com.example.planner.ui.custom_widgets.CustomSwitch


@Composable
fun AgendaScreen(
    agendaViewModel: AgendaViewModel  = hiltViewModel()
) {
    Column {
        val currentScreen = agendaViewModel.currentScreen.observeAsState()
        CustomSwitch(
            option1 = "Agenda",
            option2 = "Notes"
        ) {
            agendaViewModel.toggleScreens(it)
        }
        if (currentScreen.value == AgendaViewModel.ScreenType.TODO) {
            TaskColumn(agendaViewModel)
        } else {
            NotesScreen()
        }
    }
}

@Composable
private fun TaskColumn(
    agendaViewModel: AgendaViewModel
) {
    val taskList = agendaViewModel.tasks.observeAsState()
    if (taskList.value != null) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(taskList.value!!) { i, task ->
                println("testest itemsIndexed ${task.isDone}")
                var checked by remember { mutableStateOf(task.isDone ?: false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            agendaViewModel.checkTask(i, checked)
                        }
                    )
                    Text(
                        text = "${task.name} ${task.date}"
                    )
                }

            }
        }
    } else {
        Text("Empty")
    }
}

@Composable
private fun NotesScreen() {
    var text by remember { mutableStateOf("Hello") }
    Column {
        Text("Notes")
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxSize()
        )
    }
}
