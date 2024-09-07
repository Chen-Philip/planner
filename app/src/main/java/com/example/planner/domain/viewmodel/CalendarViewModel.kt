package com.example.planner.domain.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val dateFormat: DateFormat = SimpleDateFormat("MMM, yyyy")
    val dateTimeFormat = DateTimeFormatter.ofPattern("MMM, yyyy")

    var tasks = mutableStateOf<List<Task>?>(listOf())

    fun getTasks(date: LocalDate) {
        viewModelScope.launch {
            userRepository.getTasks { value, e ->
                val temp = mutableListOf<Task>()
                if (value != null) {
                    for (task in value) {
                        if (task.date.value != null && dateTimeFormat.format(date).equals(dateFormat.format(task.date.value!!))) {
                            temp.add(task)
                        }
                    }
                }
                tasks.value = temp
            }
        }
    }


}