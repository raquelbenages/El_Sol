package com.example.el_sol

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


data class infoplaneta(
    @DrawableRes val foto: Int,
    val nombre: String,
)

fun getinfoplaneta(): List<infoplaneta> = listOf(
    infoplaneta(R.drawable.corona_solar,  "Corona Solar"),
    infoplaneta(R.drawable.erupcionsolar, "Erupcion solar"),
    infoplaneta(R.drawable.espiculas, "Espiculas"),
    infoplaneta(R.drawable.filamentos, "Filamentos"),
    infoplaneta(R.drawable.magnetosfera, "Magnetosfera")
)

// ---------- App + Nav ----------
@Composable
fun ElSolApp() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "portada") {
        composable("portada") { Portada(nav) }
    }
}


// ---------- Main ----------
@Composable
fun Portada(navController: NavHostController){
    Scaffold(
        topBar = { MyBottomAppBar("El sol") },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { /* no-op */ }) { Text("Inc") }
        }
    ) { inner ->
        LazyColumn(
            contentPadding = inner,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(cafeterias) { c ->
                SolCardSimple(
                    foto = c.foto,
                    nombre = c.nombre,
                    lugar = c.lugar,
                    onClick = { ""}
                )
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// ---------- Menu inferior ----------
@Composable
fun MyBottomAppBar() {
    var show by remember { mutableStateOf(false) }
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(Icons.Filled.Check, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Mic,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Image,
                    contentDescription = "Localized description",
                )
            }
        }
    )
}

@Composable
fun SolCardSimple(
    foto: Int,
    nombre: String,
    lugar: String,
    onClick: () -> Unit
) {
    var rating by remember { mutableStateOf(0) }

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column {
            Image(
                painter = painterResource(foto),
                contentDescription = nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(16.dp)) {
                Text(nombre, fontFamily = FontFamily(Font(R.font.aliviaregular)),fontSize = 30.sp,)
                Spacer(Modifier.height(15.dp))
                Text(lugar, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                RatingBarSimple(value = rating, onChange = { rating = it })
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
                Button(onClick = { /* reservar */ }, modifier = Modifier.align(Alignment.End)) {
                    Text("Reserve")
                }
            }
        }
    }
}


