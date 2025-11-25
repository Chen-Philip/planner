package com.example.planner.screens.planner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.planner.data.dataclass.Task
import com.example.planner.domain.viewmodel.AgendaViewModel
import com.example.planner.domain.viewmodel.MainScreenViewModel
import com.example.planner.ui.Dimen.MEDIUM_PADDING
import com.example.planner.ui.Dimen.SMALL_PADDING
import com.example.planner.ui.custom_widgets.CustomSwitch
import com.example.planner.ui.custom_widgets.TaskRow
import com.example.planner.ui.custom_widgets.TitleRow


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgendaScreen(
    agendaViewModel: AgendaViewModel  = hiltViewModel(),
) {
    val tasks by agendaViewModel.tasks.collectAsStateWithLifecycle()
    val date by agendaViewModel.date.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier.padding(MEDIUM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val currentScreen = agendaViewModel.currentScreen.observeAsState()
        TitleRow(
            dateText = agendaViewModel.dateTimeFormat.format(date),
            onPrevClick = { agendaViewModel.getPrevDate() },
            onNextClick = { agendaViewModel.getNextDate() },
        )
        if (currentScreen.value == AgendaViewModel.ScreenType.TODO) {
            TaskColumn(
                tasks,
                agendaViewModel::deleteTask,
                agendaViewModel::checkTask,
                agendaViewModel::updateTask,
                agendaViewModel::pinToCalendar,
            )
        } else {
            NotesScreen()
        }
        CustomSwitch(
            option1 = "Agenda",
            option2 = "Notes",
            modifier = Modifier.padding(vertical = SMALL_PADDING)
        ) {
            agendaViewModel.toggleScreens(it)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TaskColumn(
    tasks: List<Task>,
    onDeleteTask: (task: Task) -> Unit,
    onCheckTask: (task: Task, isChecked: Boolean) -> Unit,
    onUpdateTask: (task: Task) -> Unit,
    onPinToCalendar: (task: Task) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tasks.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            itemsIndexed(tasks) { i, task ->
                TaskRow(
                    onDeleteTask,
                    onCheckTask,
                    onUpdateTask,
                    onPinToCalendar,
                    task
                )
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
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        )
    }
}
