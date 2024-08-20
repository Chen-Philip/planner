package com.example.planner.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.data_model.FirebaseTask
import com.example.planner.data.repository.user_repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _tasks = MutableLiveData<List<FirebaseTask>?>()
    val tasks: LiveData<List<FirebaseTask>?> = _tasks

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
        _tasks.value?.get(index)?.isDone = isChecked
    }

    fun getTasks() {
        viewModelScope.launch {
            userRepository.getTasks { value, e ->
                _tasks.value = value
            }
        }
    }
}