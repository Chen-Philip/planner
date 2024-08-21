package com.example.planner.domain.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.dataclass.Task
import com.example.planner.data.repository.user_repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    var tasks = mutableStateOf<List<Task>?>(listOf())

    private val _currentScreen = MutableLiveData(ScreenType.TODO)
    val currentScreen: LiveData<ScreenType> = _currentScreen

    enum class ScreenType {
        NOTES,
        TODO
    }
    init {
        getTasks()
    }

    fun toggleScreens(isNotesScreen: Boolean) {
        _currentScreen.value = if (isNotesScreen) ScreenType.NOTES else ScreenType.TODO
    }

    fun checkTask(index: Int, isChecked: Boolean) {
        tasks.value?.get(index)?.isDone?.value = isChecked
    }

    fun getTasks() {
        viewModelScope.launch {
            userRepository.getTasks { value, e ->
                println("testest changed")
                tasks.value = value
            }
        }
    }
}