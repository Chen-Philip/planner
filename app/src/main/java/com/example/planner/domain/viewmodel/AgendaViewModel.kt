package com.example.planner.domain.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import com.example.planner.domain.manager.DateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AgendaViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dateManager: DateManager,
) : ViewModel() {
    private val dateFormat: DateFormat = SimpleDateFormat.getDateInstance()
    val dateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    private val _currentScreen = MutableLiveData(ScreenType.TODO)
    val currentScreen: LiveData<ScreenType> = _currentScreen

    val date = dateManager.dateFlow

    var tasks = dateManager.dateFlow.flatMapLatest {date ->
        userRepository.getTasks().map {newTasks ->
            newTasks.filter { task: Task ->
                task.date != null && dateTimeFormat.format(date).equals(dateFormat.format(task.date))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    enum class ScreenType {
        NOTES,
        TODO
    }

    fun getNextDate() {
        dateManager.getNextDate()
    }

    fun getPrevDate() {
        dateManager.getPrevDate()
    }

    fun toggleScreens(isNotesScreen: Boolean) {
        _currentScreen.value = if (isNotesScreen) ScreenType.NOTES else ScreenType.TODO
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
}