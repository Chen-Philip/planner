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

    init {
        getTasks()
    }

    fun getTasks() {
        viewModelScope.launch {
            userRepository.getTasks { value, e ->
                _tasks.value = value
            }
        }
    }
}