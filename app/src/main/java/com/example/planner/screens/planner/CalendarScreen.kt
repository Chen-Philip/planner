package com.example.planner.screens.planner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planner.domain.viewmodel.AgendaViewModel
import com.example.planner.domain.viewmodel.CalendarViewModel
import com.example.planner.domain.viewmodel.MainScreenViewModel
import com.example.planner.ui.custom_widgets.CustomSwitch
import com.example.planner.ui.custom_widgets.TitleRow
import java.util.Calendar
import java.util.Date
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    mainScreenViewModel: MainScreenViewModel,
) {
    calendarViewModel.getTasks(mainScreenViewModel.date.value)
    Column {
        TitleRow(
            dateText = calendarViewModel.dateTimeFormat.format(mainScreenViewModel.date.value),
            onPrevClick = { mainScreenViewModel.getPrevMonth() },
            onNextClick = { mainScreenViewModel.getNextMonth() },
        )
        var firstDayOfWeek = mainScreenViewModel.date.value.withDayOfMonth(1).dayOfWeek.value % 7
        val lastDay = mainScreenViewModel.date.value.lengthOfMonth()
        var numWeeks = ceil((lastDay + firstDayOfWeek) / 7.0).toInt() + 1
        var currentDay = 1 - firstDayOfWeek

        LazyColumn(modifier = Modifier.fillMaxSize(0.75f)) {
            items(numWeeks) {
                if (it == 0) {
                    WeekdaysRow()
                } else {
                    Row {
                        for (i in 0 .. 6) {
                            if (currentDay < 1 || currentDay > lastDay) {
                                Text(text = "_", modifier = Modifier.padding(5.dp))
                            } else {
                                Text(text = "$currentDay", modifier = Modifier.padding(5.dp))
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
        "Frr",
        "Sat"
    )

    Row {
       weekdays.forEach {
           Text(it)
       }
    }
}