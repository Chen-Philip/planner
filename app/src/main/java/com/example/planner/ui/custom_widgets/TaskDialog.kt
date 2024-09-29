package com.example.planner.ui.custom_widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.ui.Dimen
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    task: Task? = null,
    currentDate: Long,
    onDismissRequest: () -> Unit,
    onConfirmationRequest: (Task) -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .padding(Dimen.MEDIUM_PADDING),
            shape = RoundedCornerShape(Dimen.DIALOG_CORNER),
        ) {
            var taskName by remember { task?.name ?: mutableStateOf( "") }
            val datePickerState = rememberDateRangePickerState(
                initialDisplayMode = DisplayMode.Input,
                initialSelectedStartDateMillis = task?.date?.value?.time ?: currentDate // todo fix UTC to current timezone conversion
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val pinToCalendar = remember { task?.pinToCalendar ?: mutableStateOf(false) }
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                DateRangePicker(
                    state = datePickerState,
                    showModeToggle = false,
                    title = null,
                    headline = null,
                    modifier = Modifier.fillMaxWidth().padding(0.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = pinToCalendar.value,
                        onCheckedChange = {
                            pinToCalendar.value = !pinToCalendar.value
                        }
                    )
                    Text(
                        text = "Pin to calendar"
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            val newTask = Task(
                                id = task?.id ?: "",
                                name = mutableStateOf(taskName),
                                date = mutableStateOf(Date(datePickerState.selectedStartDateMillis ?: currentDate)),
                                pinToCalendar = pinToCalendar,
                            )
                            onConfirmationRequest(newTask)
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}