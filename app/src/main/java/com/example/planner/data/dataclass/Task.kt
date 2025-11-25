package com.example.planner.data.dataclass

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate
import java.util.Date


 data class Task  (
     val id: String = "",
     val date: Date? = null,
     val name: String = "",
     val priority: Float? = null,
     val pinToCalendar: Boolean = false,
     val isDone: Boolean = false,
 )