package com.example.planner.dataclass

import java.util.Date

 open class Task  {
     var id: String = ""
     var date: Date? = null
     var name: String = ""
     var priority: Float? = null
     var isDone: Boolean = false
}