package com.example.planner.ui.custom_widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TitleRow(
    dateText: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Row {
        Button(onClick = { onPrevClick() }) {
            Text("<")
        }
        Text(dateText)
        Button(onClick = { onNextClick() }) {
            Text(">")
        }
    }
}