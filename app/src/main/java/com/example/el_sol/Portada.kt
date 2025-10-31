package com.example.el_sol

import android.R.attr.onClick
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    infoplaneta(R.drawable.magnetosfera, "Magnetosfera"),
    infoplaneta(R.drawable.manchasolar, "Mancha Solar")
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

    val sol = remember { getinfoplaneta() }

    Scaffold(
        bottomBar = { MyBottomAppBar("El sol") },
    ) { inner ->
        LazyVerticalGrid (
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed( 2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(sol) { c ->
                SolCardSimple(
                    foto = c.foto,
                    nombre = c.nombre
                )
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// ---------- Menu inferior ----------
@Composable
fun MyBottomAppBar(string: String) {
    var show by remember { mutableStateOf(false) }
    BottomAppBar(
        containerColor = Color(0xFFF54927),
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                )
            }

            FloatingActionButton(
                onClick = {  },
                modifier = Modifier)
            {
                Icon(Icons.Filled.Add, "Floating action button.")
            }

        }
    )
}

@Composable
fun SolCardSimple(
    foto: Int,
    nombre: String,

) {
    Card( modifier = Modifier.fillMaxWidth()) {
        Column {
                Image(
                    painter = painterResource(foto),
                    contentDescription = nombre,
                    modifier = Modifier
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Column(Modifier.padding(16.dp)) {
                    Text(nombre, fontSize = 15.sp,)
                    IconButton(onClick = {  }, modifier = Modifier.align(Alignment.End)) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Localized description",
                        )
                    }
                    }
                }
            }

    }



