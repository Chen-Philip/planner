package com.example.planner.data.dataclass

import java.sql.Time
import java.sql.Timestamp

open class ScheduleTask: Task() {
    var startTime: Timestamp? = null
    var endTime: Time? = null
}