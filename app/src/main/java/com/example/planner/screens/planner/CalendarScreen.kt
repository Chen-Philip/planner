package com.example.planner.screens.planner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planner.domain.viewmodel.AgendaViewModel
import com.example.planner.domain.viewmodel.CalendarViewModel
import com.example.planner.domain.viewmodel.MainScreenViewModel
import com.example.planner.ui.Dimen
import com.example.planner.ui.custom_widgets.CustomSwitch
import com.example.planner.ui.custom_widgets.TitleRow
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    mainScreenViewModel: MainScreenViewModel,
) {
    Column {

        val date = mainScreenViewModel.date.value
        TitleRow(
            dateText = calendarViewModel.dateTimeFormat.format(date),
            onPrevClick = { mainScreenViewModel.getPrevMonth() },
            onNextClick = { mainScreenViewModel.getNextMonth() },
        )

        var firstDayOfWeek = date.withDayOfMonth(1).dayOfWeek.value % 7
        val lastDay = date.lengthOfMonth()
        var numWeeks = ceil((lastDay + firstDayOfWeek) / 7.0).toInt() + 1
        var currentDay = 1 - firstDayOfWeek

        calendarViewModel.initTasks(lastDay)

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)) {
            for (it in 0..< numWeeks) {
                if (it == 0) {
                    WeekdaysRow()
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f)
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
                                    calendarViewModel = calendarViewModel
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
    calendarViewModel: CalendarViewModel
) {
    calendarViewModel.getTasks(day)
    val numTasks =
    Column (
        modifier = modifier
            .padding(1.dp)
            .fillMaxHeight()
            .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp)),
    ) {
        Text(text = "${day.dayOfMonth}")
        for (i in 0..1) {
            Text(text = calendarViewModel.tasks[day.dayOfMonth-1].value!![i].name.value,maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp)
        }
        val tasksLeft = calendarViewModel.tasks[day.dayOfMonth-1].value!!.size - 2
        if (tasksLeft > 0) {
            Text(text = "$tasksLeft more...", fontSize = 12.sp)
        }
    }
}