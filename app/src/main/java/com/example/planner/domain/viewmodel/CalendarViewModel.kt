package com.example.planner.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.repository.UserRepository
import com.google.firebase.firestore.FieldValue

class CalendarViewModel() : ViewModel() {
    val userRepository = UserRepository()
    fun addTask() {
        val task = FirebaseTask(
            name = "test",
            date = FieldValue.serverTimestamp(),
            startTime = FieldValue.serverTimestamp(),
            endTime = FieldValue.serverTimestamp(),
            dueDate = FieldValue.serverTimestamp(),
        )
        userRepository.setTasks(listOf(task))
    }
}