package com.example.planner.data.repository.user_repository

import com.example.planner.data.data_model.FirebaseTask
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : UserRepository {
    val testUserId = "testUser"
    override suspend fun getTasks(): MutableList<FirebaseTask> {
        var test = mutableListOf<FirebaseTask>()
        firestore.collection(testUserId).get().await().documents.forEach {
            test.add(it.toObject<FirebaseTask>()!!)
        }
        return test
    }
    override fun setTasks(tasks: List<FirebaseTask>) {
        tasks.forEach {
            if (it.id == null) {
                val temp = firestore.collection(testUserId).document()
                it.id = temp.id
                firestore.collection(testUserId).document(it.id!!).set(it)
            }
        }
    }
}