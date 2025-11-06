package com.example.el_sol

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color


@Composable
fun Info(navController: NavHostController? = null) {
    var showDatePicker by remember { mutableStateOf(false) }
    var favCount by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            MyBottomAppBar(
                string = "El Sol",
                onNavClick = { navController?.popBackStack() }
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Información",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Descarga detalles o elige una fecha para visitar la plataforma.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            IndeterminateCircularIndicator()

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Seleccionar fecha")
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
}

@Composable
fun MyBottomAppBar(
    string: String,
    onNavClick: () -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFFF54927),
        actions = {
            IconButton(onClick = onNavClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Abrir drawer")
            }
            IconButton(onClick = { /* acción ejemplo */ }) {
                Icon(Icons.Filled.Favorite, contentDescription = "Me gusta")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* acción FAB */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    )
}


@Composable
fun IndeterminateCircularIndicator() {
    var loading by remember { mutableStateOf(false) }

    // Botón ancho, con icono
    Button(
        onClick = { loading = true },
        enabled = !loading,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Spacer(Modifier.width(8.dp))
        Text("Descargar más información")
    }

    // Indicador centrado con transición suave
    AnimatedVisibility(visible = loading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Cargando…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
            val enabled = datePickerState.selectedDateMillis != null
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
                enabled = enabled
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        // Un poco de aire alrededor del DatePicker
        Box(Modifier.padding(8.dp)) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true
            )
        }
    }
}
