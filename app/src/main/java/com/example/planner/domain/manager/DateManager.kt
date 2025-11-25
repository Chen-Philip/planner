package com.example.planner.domain.manager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@RequiresApi(Build.VERSION_CODES.O)
class DateManager @Inject constructor() {
    private val _date = MutableStateFlow(LocalDate.now())
    val dateFlow = _date.asStateFlow()

    fun getNextDate() {
        _date.value = _date.value.plusDays(1)
    }

    fun getPrevDate() {
        _date.value = _date.value.minusDays(1)
    }

    fun getNextMonth() {
        _date.value = _date.value.plusMonths(1)
    }

    fun getPrevMonth() {
        _date.value = _date.value.minusMonths(1)
    }

    fun setDate(date: LocalDate) {
        _date.value = date
    }
}