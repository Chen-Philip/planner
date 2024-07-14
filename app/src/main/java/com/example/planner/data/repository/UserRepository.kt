package com.example.planner.data.repository

import com.example.planner.data.data_model.FirebaseTask
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UserRepository {
    val db = Firebase.firestore
    val testUserId = "testUser"

    suspend fun getTasks(): MutableList<FirebaseTask> {
        var test = mutableListOf<FirebaseTask>()
        db.collection(testUserId).get().await().documents.forEach {
            test.add(it.toObject<FirebaseTask>()!!)
        }
        return test
    }
    fun setTasks(tasks: List<FirebaseTask>) {
        tasks.forEach {
            if (it.id == null) {
                db.collection(testUserId).add(it)
            }
        }
    }
}