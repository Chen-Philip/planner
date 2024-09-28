package com.example.planner.domain.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
abstract class BaseViewModel: ViewModel() {
    @Inject lateinit var userRepository: UserRepository

    abstract fun getTasks(date: LocalDate)

    fun deleteTask(task: Task) {
        userRepository.deleteTask(task)
    }

    fun pinToCalendar(task: Task) {
        task.pinToCalendar.value = !task.pinToCalendar.value
        userRepository.updateTask(task)
    }

    fun checkTask(task: Task, isChecked: Boolean) {
        task.isDone.value = isChecked
        userRepository.updateTask(task)
    }
}