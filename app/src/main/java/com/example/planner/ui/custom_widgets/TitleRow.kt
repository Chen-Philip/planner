package com.example.planner.ui.custom_widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TitleRow(
    dateText: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { onPrevClick() }) {
            Text("<")
        }
        Text(dateText)
        Button(onClick = { onNextClick() }) {
            Text(">")
        }
    }
}