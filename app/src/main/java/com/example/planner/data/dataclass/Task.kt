package com.example.planner.data.dataclass

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.Date

 open class Task  (
     var id: String = "",
     var date: MutableState<Date?> = mutableStateOf(null),
     var name: MutableState<String> = mutableStateOf(""),
     var priority: Float? = null,
     var isImportant: Boolean = false,
     var isDone: MutableState<Boolean> = mutableStateOf(false),
 )