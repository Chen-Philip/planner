package com.example.planner.data.repository.user_repository

import com.example.planner.data.data_model.FirebaseTask
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

interface UserRepository {
    suspend fun getTasks(): MutableList<FirebaseTask>

    fun setTasks(tasks: List<FirebaseTask>)
}

