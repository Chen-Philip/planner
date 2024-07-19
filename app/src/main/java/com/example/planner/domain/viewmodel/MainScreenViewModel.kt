package com.example.planner.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.repository.user_repository.UserRepository
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
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