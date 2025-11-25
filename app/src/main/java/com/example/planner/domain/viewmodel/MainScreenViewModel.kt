package com.example.planner.domain.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import com.example.planner.domain.manager.DateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    dataManager: DateManager,
) : ViewModel() {
    val date = dataManager.dateFlow

    fun addTask(task: Task) {
        userRepository.setTasks(listOf(task))
    }


}