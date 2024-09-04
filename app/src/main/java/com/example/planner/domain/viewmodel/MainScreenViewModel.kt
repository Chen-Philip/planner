package com.example.planner.domain.viewmodel

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var calendar = Calendar.getInstance()
    var date = mutableLongStateOf(calendar.timeInMillis)


    fun addTask(name: String, startDate: Long?, endDate: Long?) {
        val task = FirebaseTask(
            id = "",
            name = name,
            date = startDate?.toFloat() ?: System.currentTimeMillis().toFloat(),
            dueDate = endDate?.toFloat(),
            startTime = null,
            endTime = null,
        )
        userRepository.setTasks(listOf(task))
    }

    fun getNextDate() {
        calendar.add(Calendar.DATE, 1)
        date.longValue = calendar.timeInMillis
    }

    fun getPrevDate() {
        calendar.add(Calendar.DATE, -1)
        date.longValue = calendar.timeInMillis
    }
}