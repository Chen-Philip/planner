package com.example.planner.data.repository.user_repository

import com.example.planner.data.dataclass.Task
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getTasks(): Flow<List<Task>>

    fun deleteTask(task: Task)

    fun updateTask(task: Task)

    fun setTasks(tasks: List<Task>)
}

