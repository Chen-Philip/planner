package com.example.planner.data.repository.user_repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.planner.data.User
import com.example.planner.data.data_model.FirebaseTask
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun getTasks(listener: (List<FirebaseTask>?, FirebaseFirestoreException?) -> Unit) {
        firestore.collection(User.userId).addSnapshotListener { value, e ->
            listener(value?.toObjects(FirebaseTask::class.java), e)
        }
    }

    override suspend fun getTasks2(listener: (List<FirebaseTask>?, FirebaseFirestoreException?) -> Unit): LiveData<List<FirebaseTask>> {
        var tasks: MutableLiveData<List<FirebaseTask>> = MutableLiveData()
        firestore.collection(User.userId).addSnapshotListener { value, e ->
            tasks.postValue(value?.toObjects(FirebaseTask::class.java))
        }
        return tasks
    }

    override fun setTasks(tasks: List<FirebaseTask>) {
        tasks.forEach {
            if (it.id == null) {
                val temp = firestore.collection(User.userId).document()
                it.id = temp.id
                firestore.collection(User.userId).document(it.id!!).set(it)
            }
        }
    }
}