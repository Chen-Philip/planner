package com.example.planner.domain.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var date = mutableStateOf(
        LocalDate.now()
    )


    fun addTask(task: Task) {
        userRepository.setTasks(listOf(task))
    }

    fun getNextDate() {
        date.value = date.value.plusDays(1)
    }

    fun getPrevDate() {
        date.value = date.value.minusDays(1)
    }

    fun getNextMonth() {
        date.value = date.value.plusMonths(1)
    }

    fun getPrevMonth() {
        date.value = date.value.minusMonths(1)
    }
}