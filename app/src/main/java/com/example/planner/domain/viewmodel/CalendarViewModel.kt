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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    lateinit var tasks: List<MutableState<List<Task>?>>

    fun initTasks(numDays: Int) {
        tasks = buildList {
            for (i in 1 .. numDays) {
                add(mutableStateOf(emptyList()))
            }
        }
    }

    fun getTasks(date: LocalDate) {
        val dateFormat: DateFormat = SimpleDateFormat.getDateInstance()
        val dateTimeFormat = DateTimeFormatter.ofPattern("MMM d, yyyy")
        viewModelScope.launch {
            userRepository.getTasks { value, e ->
                val temp = mutableListOf<Task>()
                if (value != null) {
                    for (task in value) {
                        if (task.date.value != null && dateTimeFormat.format(date).equals(dateFormat.format(task.date.value!!))) {
                            temp.add(task)
                        }
                    }
                }
                tasks[date.dayOfMonth - 1].value = temp
            }
        }
    }
}