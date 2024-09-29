package com.example.planner.data

import androidx.compose.runtime.mutableStateOf
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import java.util.Date

fun transformFirebaseTasktoTask(firebaseTask: FirebaseTask): Task {
    return Task(
        id = firebaseTask.id,
        date = mutableStateOf( firebaseTask.date?.toLong().let { if (it == null) null else Date(it) }),
        name = mutableStateOf(firebaseTask.name ?: ""),
        priority = null,
        isDone = mutableStateOf(firebaseTask.isDone ?: false),
        pinToCalendar = mutableStateOf(firebaseTask.pinToCalendar)
    )
}

fun transformTasktoFirebaseTask(task: Task): FirebaseTask {
    return FirebaseTask(
        id = task.id,
        date = task.date.value?.time?.toFloat(),
        name =  task.name.value,
        priority = null,
        isDone = task.isDone.value,
        pinToCalendar = task.pinToCalendar.value
    )
}