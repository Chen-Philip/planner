package com.example.planner.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.repository.user_repository.UserRepository
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun addTask(name: String, startDate: Long?, endDate: Long?) {
        val task = FirebaseTask(
            id = "",
            name = name,
            date = startDate?.toFloat() ?: System.currentTimeMillis().toFloat(),
            dueDate = endDate?.toFloat(),
            startTime = null,
            endTime = null,
        )
        userRepository.setTasks(listOf(task))
    }
}