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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.planner.data.dataclass.Task
import com.example.planner.domain.viewmodel.BaseViewModel
import com.example.planner.domain.viewmodel.CalendarViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskRow(
    viewModel: BaseViewModel,
    task: Task,
) {

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
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
                    IconButton(
                        onClick = { /* TODO */ }
                    ) {
                        Icon(Icons.Filled.Edit, "More")
                    }
                    IconButton(
                        onClick = { /* TODO */ }
                    ) {
                        Icon(Icons.Filled.Delete, "More")
                    }
                    IconButton(
                        onClick = {
                            viewModel.unPinFromCalendar(task)
                        }
                    ) {
                        Icon(Icons.Filled.Star, "More")
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