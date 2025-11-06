package com.example.el_sol

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Info() {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        IndeterminateCircularIndicator()

        Spacer(modifier= Modifier)

        Button(
            onClick = { showDatePicker = true }
        ) {
            Text("Visit platform - select date")
        }

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { selectedDate ->
                    println("Fecha seleccionada: $selectedDate")
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Composable
fun IndeterminateCircularIndicator() {
    var loading by remember { mutableStateOf(false) }

    Button(onClick = { loading = true }, enabled = !loading) {
        Text("Download more info")
    }

    if (loading) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .padding(top = 8.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}