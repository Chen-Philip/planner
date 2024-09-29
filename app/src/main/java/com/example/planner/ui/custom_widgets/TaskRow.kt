package com.example.planner.ui.custom_widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.domain.viewmodel.BaseViewModel
import com.example.planner.domain.viewmodel.CalendarViewModel
import com.example.planner.ui.Dimen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskRow(
    viewModel: BaseViewModel,
    task: Task,
) {

    // Can use decorator or smth
    val showConfirmDialog = remember { mutableStateOf(false) }
    ConfirmDialog(showConfirmDialog, onDismissRequest = { showConfirmDialog.value = false }) {
        viewModel.deleteTask(task)
        showConfirmDialog.value = false
    }

    MainTaskRow(
        viewModel = viewModel,
        task = task
    ) {
        showConfirmDialog.value = true
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MainTaskRow(
    viewModel: BaseViewModel,
    task: Task,
    openDialog: () -> Unit
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.isDone.value,
                onCheckedChange = {
                    viewModel.checkTask(task, it)
                }
            )
            Text(
                text = task.name.value
            )
        }

        Row (
            horizontalArrangement = Arrangement.Absolute.Right
        ){
            var visible by remember { mutableStateOf(false) }
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + expandHorizontally(
                    // Expand from the top.
                    expandFrom = Alignment.Start
                ) + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
            ) {
                Row {
                    val showEditDialog = remember { mutableStateOf(false) }
                    if (showEditDialog.value) {
                        TaskDialog(
                            task = task,
                            currentDate = task.date.value?.time!!,
                            onDismissRequest = { showEditDialog.value = false },
                            onConfirmationRequest = { firebaseTask ->
                                viewModel.updateTask(firebaseTask)
                                showEditDialog.value = false

                            }
                        )
                    }
                    IconButton(
                        onClick = { showEditDialog.value = true }
                    ) {
                        Icon(Icons.Filled.Edit, "More")
                    }
                    IconButton(
                        onClick = openDialog
                    ) {
                        Icon(Icons.Filled.Delete, "More")
                    }
                    IconButton(
                        onClick = {
                            viewModel.pinToCalendar(task)
                        }
                    ) {
                        if (task.pinToCalendar.value) {
                            Icon(Icons.Filled.Favorite, "More")
                        } else {
                            Icon(Icons.Outlined.FavoriteBorder, "More")
                        }
                    }
                }
            }

            IconButton(
                onClick = { visible = !visible }
            ) {
                Icon(Icons.Filled.MoreVert, "More")
            }
        }
    }
}

@Composable
private fun ConfirmDialog(
    showConfirmDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmationRequest: () -> Unit,
) {
    if (showConfirmDialog.value) {
        Dialog(
            onDismissRequest = { onDismissRequest() }
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Card(
                modifier = Modifier
                    .padding(Dimen.MEDIUM_PADDING),
                shape = RoundedCornerShape(Dimen.DIALOG_CORNER),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val pinToCalendar = remember { mutableStateOf(false) }
                    Text(" Are you sure you want to delete this task?")

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = onConfirmationRequest,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}