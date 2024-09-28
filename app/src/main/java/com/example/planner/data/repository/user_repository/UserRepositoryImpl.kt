package com.example.planner.data.repository.user_repository

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.planner.data.User
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun getTasks(listener: (List<Task>?, FirebaseFirestoreException?) -> Unit) {
        firestore.collection(User.userId).addSnapshotListener { value, e ->
            listener((value?.toObjects(FirebaseTask::class.java))?.map { transformFirebaseTasktoTask(it) }, e)
        }
    }

    override fun updateTask(task: Task) {
        val firebaseTask = transformTasktoFirebaseTask(task)
        if (firebaseTask.id == "") {
            val temp = firestore.collection(User.userId).document()
            firebaseTask.id = temp.id

        }
        firestore.collection(User.userId).document(firebaseTask.id).set(firebaseTask)
    }

    override fun setTasks(tasks: List<FirebaseTask>) {
        tasks.forEach {
            if (it.id == "") {
                val temp = firestore.collection(User.userId).document()
                it.id = temp.id
                firestore.collection(User.userId).document(it.id).set(it)
            }
        }
    }

    private fun transformFirebaseTasktoTask(firebaseTask: FirebaseTask): Task {
        return Task(
            id = firebaseTask.id,
            date = mutableStateOf( firebaseTask.date?.toLong().let { if (it == null) null else Date(it)}),
            name = mutableStateOf(firebaseTask.name ?: ""),
            priority = null,
            isDone = mutableStateOf(firebaseTask.isDone ?: false),
            pinToCalendar = mutableStateOf(firebaseTask.pinToCalendar)
        )
    }

    private fun transformTasktoFirebaseTask(task: Task): FirebaseTask {
        return FirebaseTask(
            id = task.id,
            date = task.date.value?.time?.toFloat(),
            name =  task.name.value,
            priority = null,
            isDone = task.isDone.value,
            pinToCalendar = task.pinToCalendar.value
        )
    }
}