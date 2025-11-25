package com.example.planner.data

import androidx.compose.runtime.mutableStateOf
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import java.util.Date

fun transformFirebaseTasktoTask(firebaseTask: FirebaseTask): Task {
    return Task(
        id = firebaseTask.id,
        date = firebaseTask.date?.toLong().let { if (it == null) null else Date(it) },
        name = firebaseTask.name ?: "",
        priority = null,
        isDone = firebaseTask.isDone ?: false,
        pinToCalendar = firebaseTask.pinToCalendar
    )
}

fun transformTasktoFirebaseTask(task: Task): FirebaseTask {
    return FirebaseTask(
        id = task.id,
        date = task.date?.time?.toFloat(),
        name =  task.name,
        priority = null,
        isDone = task.isDone,
        pinToCalendar = task.pinToCalendar
    )
}