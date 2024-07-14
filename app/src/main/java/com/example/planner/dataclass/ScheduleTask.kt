package com.example.planner.dataclass

import java.sql.Time

open class ScheduleTask: Task() {
    var startTime: Time? = null
    var endTime: Time? = null
}