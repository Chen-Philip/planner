package com.example.planner.domain.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
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

    lateinit var tasks: List<MutableState<List<Task>?>>

    fun initTasks(numDays: Int) {
        tasks = buildList {
            for (i in 1 .. numDays) {
                add(mutableStateOf(emptyList()))
            }
        }
        println("testest ${tasks.size}")
    }
//    fun getTasks(date: LocalDate) {
//        viewModelScope.launch {
//            userRepository.getTasks { value, e ->
//                val temp = mutableListOf<Task>()
//                if (value != null) {
//                    for (task in value) {
//                        if (task.date.value != null && dateTimeFormat.format(date).equals(dateFormat.format(task.date.value!!))) {
//                            temp.add(task)
//                        }
//                    }
//                }
//                tasks.value = temp
//            }
//        }
//        Task  (
//            var id: String = "",
//        var date: MutableState<Date?> = mutableStateOf(null),
//        var name: MutableState<String> = mutableStateOf(""),
//        var priority: Float? = null,
//        var isImportant: Boolean = false,
//        var isDone: MutableState<Boolean> = mutableStateOf(false),
//        )
//    }

    fun getTasks(date: LocalDate) {
        var tempTask = Task (
            id = "",
            name= mutableStateOf("test test"),
            priority= null,
            isImportant= true,
        )
        println("testest ${date.dayOfMonth}")
        tasks[date.dayOfMonth - 1].value = listOf(
            tempTask,
            tempTask,
            tempTask,
            tempTask,
            tempTask,
        )
    }


}