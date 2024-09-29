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
    mainScreenViewModel: MainScreenViewModel,
) {
    agendaViewModel.getTasks(mainScreenViewModel.date.value)
    Column (
        modifier = Modifier.padding(MEDIUM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val currentScreen = agendaViewModel.currentScreen.observeAsState()
        TitleRow(
            dateText = agendaViewModel.dateTimeFormat.format(mainScreenViewModel.date.value),
            onPrevClick = { mainScreenViewModel.getPrevDate() },
            onNextClick = { mainScreenViewModel.getNextDate() },
        )
        if (currentScreen.value == AgendaViewModel.ScreenType.TODO) {
            TaskColumn(
                agendaViewModel
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
    agendaViewModel: AgendaViewModel,
    modifier: Modifier = Modifier,
) {
    if (agendaViewModel.tasks.value != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            itemsIndexed(agendaViewModel.tasks.value!!) { i, task ->
                TaskRow(agendaViewModel, task)
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
