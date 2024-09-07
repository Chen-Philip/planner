package com.example.planner.domain.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AgendaViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel() {
    val dateFormat: DateFormat = SimpleDateFormat.getDateInstance()
    val dateTimeFormat = DateTimeFormatter.ofPattern("MMM d, yyyy")
    var tasks = mutableStateOf<List<Task>?>(listOf())

    private val _currentScreen = MutableLiveData(ScreenType.TODO)
    val currentScreen: LiveData<ScreenType> = _currentScreen

    enum class ScreenType {
        NOTES,
        TODO
    }

    fun toggleScreens(isNotesScreen: Boolean) {
        _currentScreen.value = if (isNotesScreen) ScreenType.NOTES else ScreenType.TODO
    }

    fun checkTask(index: Int, isChecked: Boolean) {
        tasks.value?.get(index)?.isDone?.value = isChecked
    }

    fun getTasks(date: LocalDate) {
        viewModelScope.launch {
            userRepository.getTasks { value, e ->
                val temp = mutableListOf<Task>()
                if (value != null) {
                    for (task in value) {
                        println("testest ${dateFormat.format(task.date.value!!)}")
                        if (task.date.value != null && dateTimeFormat.format(date).equals(dateFormat.format(task.date.value!!))) {
                            temp.add(task)
                        }
                    }
                }
                tasks.value = temp
            }
        }
    }
}