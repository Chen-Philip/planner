package com.example.planner.data.repository.user_repository


import com.example.planner.data.User
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.transformFirebaseTasktoTask
import com.example.planner.data.transformTasktoFirebaseTask
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : UserRepository {
    override fun getTasks(): Flow<List<Task>> = callbackFlow {
        val subscription = firestore.collection(User.userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Close the stream on error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // Convert to your domain objects
                val tasks = snapshot.toObjects(FirebaseTask::class.java)
                    .map { transformFirebaseTasktoTask(it) }

                // Send data to the pipe
                trySend(tasks)
            }
        }

        awaitClose {
            subscription.remove() // Disconnects from Firebase
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