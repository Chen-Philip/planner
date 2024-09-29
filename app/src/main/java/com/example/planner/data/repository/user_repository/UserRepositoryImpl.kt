package com.example.planner.data.repository.user_repository


import com.example.planner.data.User
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.transformFirebaseTasktoTask
import com.example.planner.data.transformTasktoFirebaseTask
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun getTasks(listener: (List<Task>?, FirebaseFirestoreException?) -> Unit) {
        firestore.collection(User.userId).addSnapshotListener { value, e ->
            listener((value?.toObjects(FirebaseTask::class.java))?.map { transformFirebaseTasktoTask(it) }, e)
        }
    }

    override fun deleteTask(task: Task) {
        val firebaseTask = transformTasktoFirebaseTask(task)
        if (firebaseTask.id == "") {
            val temp = firestore.collection(User.userId).document()
            firebaseTask.id = temp.id

        }
        firestore.collection(User.userId).document(firebaseTask.id).delete()
    }

    override fun updateTask(task: Task) {
        val firebaseTask = transformTasktoFirebaseTask(task)
        if (firebaseTask.id == "") {
            val temp = firestore.collection(User.userId).document()
            firebaseTask.id = temp.id

        }
        firestore.collection(User.userId).document(firebaseTask.id).set(firebaseTask)
    }

    override fun setTasks(tasks: List<Task>) {
        tasks.forEach {
            val firebaseTask = transformTasktoFirebaseTask(it)
            if (firebaseTask.id == "") {
                val temp = firestore.collection(User.userId).document()
                firebaseTask.id = temp.id
                firestore.collection(User.userId).document(firebaseTask.id).set(firebaseTask)
            }
        }
    }
}