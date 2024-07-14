package com.example.planner.data.data_model

import com.google.firebase.firestore.FieldValue

data class FirebaseTask (
    var id: String? = null,
    var date: FieldValue? = null,
    var name: String? = null,
    var priority: Float? = null,
    var isDone: Boolean? = null,
    var dueDate: FieldValue? = null,
    var startTime: FieldValue? = null,
    var endTime: FieldValue? = null,
)