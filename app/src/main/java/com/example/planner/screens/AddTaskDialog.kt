package com.example.planner.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.example.planner.R
import com.example.planner.ui.Dimen.DIALOG_CORNER
import com.example.planner.ui.Dimen.MEDIUM_PADDING
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismissRequest: () -> Unit,
    onConfirmationRequest: (String, Long?, Long?) -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }

    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .padding(MEDIUM_PADDING),
            shape = RoundedCornerShape(DIALOG_CORNER),
        ) {
            var taskName by remember { mutableStateOf("") }
            val datePickerState = rememberDateRangePickerState(
                initialDisplayMode = DisplayMode.Input,
                initialSelectedStartDateMillis = System.currentTimeMillis()
            )
            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
                        onClick = { onConfirmationRequest(taskName, datePickerState.selectedStartDateMillis, datePickerState.selectedEndDateMillis) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

