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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.data.dataclass.Task
import com.example.planner.domain.viewmodel.CalendarViewModel
import com.example.planner.domain.viewmodel.MainScreenViewModel
import com.example.planner.screens.Screen
import com.example.planner.ui.Dimen
import com.example.planner.ui.custom_widgets.TaskRow
import com.example.planner.ui.custom_widgets.TitleRow
import java.lang.Integer.min
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    mainScreenViewModel: MainScreenViewModel,
    navController: NavController,
) {
    val selectedDay = remember { mutableIntStateOf(0) }
    val showAddTaskDialog = remember { mutableStateOf(false) }

    calendarViewModel.initTasks(mainScreenViewModel.date.value.lengthOfMonth())

    CalendarView(
        mainScreenViewModel = mainScreenViewModel,
        calendarViewModel = calendarViewModel,
        onOpenDialogRequest = {
            selectedDay.intValue = it
            showAddTaskDialog.value = true
        }
    )
    AgendaDialog(
        calendarViewModel = calendarViewModel,
        showAddTaskDialog = showAddTaskDialog,
        selectedDay = selectedDay,
        onDismissRequest = { showAddTaskDialog.value = false },
        onNavigateToAgendaRequest = {
            val date = mainScreenViewModel.date.value
            mainScreenViewModel.date.value = LocalDate.of(date.year, date.monthValue, it)
            navController.navigate(Screen.Agenda.route)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarView(
    mainScreenViewModel: MainScreenViewModel,
    calendarViewModel: CalendarViewModel,
    onOpenDialogRequest: (Int) -> Unit
) {
    val monthTimeFormat = DateTimeFormatter.ofPattern("MMM, yyyy")
    val date = mainScreenViewModel.date.value

    Column {
        TitleRow(
            dateText = monthTimeFormat.format(date),
            onPrevClick = { mainScreenViewModel.getPrevMonth() },
            onNextClick = { mainScreenViewModel.getNextMonth() },
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
                                CalendarDay(
                                    day = LocalDate.of(date.year,date.monthValue,currentDay),
                                    modifier = Modifier.weight(1f),
                                    calendarViewModel = calendarViewModel,
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
    calendarViewModel: CalendarViewModel,
    onOpenDialogRequest: (Int) -> Unit,
) {
    calendarViewModel.getTasks(day)
    Column (
        modifier = modifier
            .padding(1.dp)
            .fillMaxHeight()
            .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .clickable { onOpenDialogRequest(day.dayOfMonth - 1) },
    ) {
        Text(text = "${day.dayOfMonth}")
        val tasks = calendarViewModel.tasks[day.dayOfMonth-1].value
        if (tasks.isNullOrEmpty()) {
            Text("No Tasks")
        } else {
            for (i in 0..< min(2, tasks.size)) {
                Text(text = tasks[i].name.value, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp)
            }

            val tasksLeft = tasks.size - 2
            if (tasksLeft > 0) {
                Text(text = "$tasksLeft more...", fontSize = 12.sp)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AgendaDialog(
    calendarViewModel: CalendarViewModel,
    selectedDay: MutableIntState,
    showAddTaskDialog: MutableState<Boolean>,
    onNavigateToAgendaRequest: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (showAddTaskDialog.value) {
        val tasks = calendarViewModel.tasks[selectedDay.intValue].value
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
                Column {
                    if (tasks != null) {
                        LazyColumn(modifier = Modifier.fillMaxHeight(0.75f).fillMaxWidth()) {
                            itemsIndexed(tasks) { i, task ->
                                TaskRow(calendarViewModel, task)
                            }
                        }
                    } else {
                        Text("Empty")
                    }
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            onDismissRequest()
                            onNavigateToAgendaRequest(selectedDay.intValue + 1)
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
