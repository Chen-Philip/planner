package com.example.planner.screens.planner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.planner.data.dataclass.Task
import com.example.planner.domain.viewmodel.CalendarViewModel
import com.example.planner.screens.Screen
import com.example.planner.ui.Dimen
import com.example.planner.ui.Dimen.TINY_PADDING
import com.example.planner.ui.custom_widgets.TaskRow
import com.example.planner.ui.custom_widgets.TitleRow
import kotlinx.collections.immutable.ImmutableList
import java.lang.Integer.min
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    navController: NavController,
) {
    var selectedDay by remember { mutableIntStateOf(0) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    val uiState by calendarViewModel.uiState.collectAsStateWithLifecycle()
    val date by calendarViewModel.date.collectAsStateWithLifecycle()
    when (val state = uiState) {
        is CalendarViewModel.CalendarUiState.Loading -> {
            // Render the Spinner
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is CalendarViewModel.CalendarUiState.Success -> {
            CalendarView(
                state.tasks,
                date,
                calendarViewModel::getNextMonth,
                calendarViewModel::getPrevMonth,
                onOpenDialogRequest = {
                    selectedDay = it
                    showAddTaskDialog = true
                }
            )
            AgendaDialog(
                tasks = state.tasks[selectedDay],
                showAddTaskDialog = showAddTaskDialog,
                selectedDay = selectedDay,
                onDismissRequest = { showAddTaskDialog = false },
                onNavigateToAgendaRequest = {
                    calendarViewModel.setDate(LocalDate.of(date.year, date.monthValue, it))
                    navController.navigate(Screen.Agenda.route)
                },
                onDeleteTask = calendarViewModel::deleteTask,
                onCheckTask = calendarViewModel::checkTask,
                onUpdateTask = calendarViewModel::updateTask,
                onPinToCalendar = calendarViewModel::pinToCalendar,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarView(
    tasks: ImmutableList<ImmutableList<Task>>,
    date: LocalDate,
    onNextMonth: () -> Unit,
    onPrevMonth: () -> Unit,
    onOpenDialogRequest: (Int) -> Unit
) {
    val monthTimeFormat = DateTimeFormatter.ofPattern("MMM, yyyy")

    Column {
        TitleRow(
            dateText = monthTimeFormat.format(date),
            onPrevClick = onPrevMonth,
            onNextClick = onNextMonth,
        )

        val firstDayOfWeek = date.withDayOfMonth(1).dayOfWeek.value % 7
        val lastDay = date.lengthOfMonth()
        val numWeeks = ceil((lastDay + firstDayOfWeek) / 7.0).toInt() + 1
        var currentDay = 1 - firstDayOfWeek

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)) {
            for (it in 0..< numWeeks) {
                if (it == 0) {
                    WeekdaysRow()
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (i in 0 .. 6) {
                            if (currentDay < 1 || currentDay > lastDay) {
                                Text(text = "", modifier = Modifier
                                    .padding(5.dp)
                                    .weight(1f))
                            } else {
                                println("testest ${currentDay}")
                                CalendarDay(
                                    day = LocalDate.of(date.year,date.monthValue, currentDay),
                                    modifier = Modifier.weight(1f),
                                    tasks = tasks[currentDay - 1],
                                    onOpenDialogRequest = { onOpenDialogRequest(it) }
                                )
                            }
                            currentDay++
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekdaysRow() {
    val weekdays = listOf(
        "Sun",
        "Mon",
        "Tues",
        "Wed",
        "Thurs",
        "Fri",
        "Sat"
    )

    Row (
        modifier = Modifier.fillMaxWidth()
    ) {
       weekdays.forEach {
           Text(text = it, modifier = Modifier
               .padding(5.dp)
               .weight(1f))
       }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarDay(
    day: LocalDate,
    modifier: Modifier,
    tasks: ImmutableList<Task>,
    onOpenDialogRequest: (Int) -> Unit,
) {
    Box(modifier = modifier
        .padding(1.dp)
        .fillMaxHeight()
        .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
        .clickable { onOpenDialogRequest(day.dayOfMonth - 1) }
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = TINY_PADDING, vertical = 4.dp)
                .fillMaxSize(),
        ) {
            Text(text = "${day.dayOfMonth}")
            if (tasks.isNullOrEmpty()) {
                Text(text = "No Tasks", fontSize = 12.sp)
            } else {
                for (i in 0..<min(2, tasks.size)) {
                    Text(
                        modifier = Modifier.wrapContentHeight(),
                        text = tasks[i].name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp
                    )
                }

                val tasksLeft = tasks.size - 2
                if (tasksLeft > 0) {
                    Text(text = "$tasksLeft more...",
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AgendaDialog(
    tasks: ImmutableList<Task>,
    selectedDay: Int,
    showAddTaskDialog: Boolean,
    onNavigateToAgendaRequest: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onDeleteTask: (task: Task) -> Unit,
    onCheckTask: (task: Task, isChecked: Boolean) -> Unit,
    onUpdateTask: (task: Task) -> Unit,
    onPinToCalendar: (task: Task) -> Unit,
) {
    if (showAddTaskDialog) {
        Dialog(
            onDismissRequest = { onDismissRequest() }
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Card(
                modifier = Modifier
                    .padding(Dimen.MEDIUM_PADDING)
                    .height(200.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(Dimen.DIALOG_CORNER),
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    if (tasks.isNotEmpty()) {
                        LazyColumn(modifier = Modifier
                            .fillMaxHeight(0.75f)
                            .fillMaxWidth()) {
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
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            onDismissRequest()
                            onNavigateToAgendaRequest(selectedDay + 1)
                        }
                    ) {
                        Text("See in agenda")
                    }
                }
            }
        }
    }
}
// todo task with decorator
