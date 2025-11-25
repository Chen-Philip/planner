package com.example.planner.domain.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import com.example.planner.domain.manager.DateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dateManager: DateManager,
) : ViewModel() {
    val date = dateManager.dateFlow

    val uiState: StateFlow<CalendarUiState> = dateManager.dateFlow.flatMapLatest { date ->
        userRepository.getTasks().map {
            var numDays = date.lengthOfMonth()

            val newTasks = mutableListOf<ImmutableList<Task>>()
            for (i in 0 until numDays) {
                val dayTasks = it.filter {task ->
                    (task.date != null) &&
                            (date.monthValue == task.date.toLocalDate().monthValue) &&
                            (date.year == task.date.toLocalDate().year) &&
                            (i + 1 == task.date.toLocalDate().dayOfMonth) &&
                            task.pinToCalendar
                }
               newTasks.add(dayTasks.toImmutableList())

            }
            CalendarUiState.Success(newTasks.toImmutableList())as CalendarUiState
        }.onStart { emit(CalendarUiState.Loading) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState.Loading
    )

    fun getNextMonth() {
        dateManager.getNextMonth()
    }

    fun getPrevMonth() {
        dateManager.getPrevMonth()
    }

    fun setDate(date: LocalDate) {
        dateManager.setDate(date)
    }

    fun updateTask(task: Task) {
        userRepository.updateTask(task)
    }

    fun deleteTask(task: Task) {
        userRepository.deleteTask(task)
    }

    fun pinToCalendar(task: Task) {
        var newTask = task.copy(
            pinToCalendar = !task.pinToCalendar
        )
        userRepository.updateTask(newTask)
    }

    fun checkTask(task: Task, isChecked: Boolean) {
        var newTask = task.copy(
            isDone = isChecked
        )
        userRepository.updateTask(newTask)
    }

    // Create this extension function in a utils file (e.g., DateExtensions.kt)
    private fun Date.toLocalDate(): LocalDate {
        return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    sealed interface CalendarUiState {
        data object Loading : CalendarUiState
        data class Success(val tasks: ImmutableList<ImmutableList<Task>>) : CalendarUiState
    }
}