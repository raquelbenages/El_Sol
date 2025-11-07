package com.example.el_sol

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip


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
    val navController = rememberNavController()


    var sol by remember { mutableStateOf(getinfoplaneta()) }  // lista mutable compartida
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var favCount by rememberSaveable { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.erupcionsolar),
                        contentDescription = "FOTO NAVEGACION",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    NavigationDrawerItem(
                        label = { Text("Portada") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate("portada")
                            }
                        },
                        icon = { Icon(Icons.Filled.Build, contentDescription = "build") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Info") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()          // 👈 Cierra el menú
                                navController.navigate("Info") // Navega después
                            }
                        },
                        icon = { Icon(Icons.Filled.Info, contentDescription = "info") },
                    )
                    NavigationDrawerItem(
                        label = { Text("Email") },
                        selected = false,
                        onClick = { },
                        icon = { Icon(Icons.Filled.Email, contentDescription = "email") }
                    )
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                MyBottomAppBarHoisted(
                    string = "El sol",
                    count = favCount,
                    onFavClick = { favCount++ },
                    onNavClick = { scope.launch { drawerState.open() } }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { inner ->
            NavHost(
                navController = navController,
                startDestination = "portada",
                modifier = Modifier.padding(inner)
            ) {
                composable("portada") {
                    PortadaBody(
                        sol = sol,
                        onCopy = { item ->
                            val copia = item.copy(nombre = "${item.nombre} (copia)")
                            sol = sol + copia
                            scope.launch { snackbarHostState.showSnackbar("Tarjeta duplicada") }
                        },
                        onDelete = { item ->
                            sol = sol.filterNot { it == item }
                        },
                        onCardClick = { nombre ->
                            scope.launch { snackbarHostState.showSnackbar(" $nombre") }
                        }
                    )
                }
                composable("Info") {
                    InfoBody()
                }
            }
        }
    }
}


@Composable
fun PortadaBody(
    sol: List<infoplaneta>,
    onCopy: (infoplaneta) -> Unit,
    onDelete: (infoplaneta) -> Unit,
    onCardClick: (String) -> Unit
) {
    LazyVerticalGrid(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(sol) { c ->
            SolCardSimple(
                foto = c.foto,
                nombre = c.nombre,
                onCardClick = onCardClick,
                onCopy = { onCopy(c) },
                onDelete = { onDelete(c) }
            )
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

//LA BARRA DE ABAJO
@Composable
fun MyBottomAppBarHoisted(
    string: String,
    count: Int,
    onFavClick: () -> Unit,
    onNavClick: () -> Unit = {}
) {
    BottomAppBar(
        containerColor = Color(0xFFF54927),
        actions = {
            IconButton(onClick = onNavClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Abrir drawer")
            }
            IconButton(onClick = onFavClick) {
                BadgedBox(
                    badge = {
                        if (count > 0) Badge { Text("$count") }
                    }
                ) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Me gusta")
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* acción FAB */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    )
}




// LAS CARDS CON LOS MINI MENUS
@SuppressLint("ServiceCast")
@Composable
fun SolCardSimple(
    @DrawableRes foto: Int,
    nombre: String,
    onCopy: () -> Unit,
    onDelete: () -> Unit,
    onCardClick: (String) -> Unit,
    ) {
    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var menuAbierto by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(nombre) }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD))
    ) {
        Column {
            Image(
                painter = painterResource(foto),
                contentDescription = nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // cuadrada
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = nombre, fontSize = 18.sp, modifier = Modifier.weight(1f))

                Box {
                    IconButton(onClick = { menuAbierto = !menuAbierto }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                    }

                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Copiar") },
                            onClick = {
                                onCopy()
                                menuAbierto = false
                            },
                            leadingIcon = { Icon(Icons.Filled.Add, contentDescription = "Copiar") }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                onDelete()
                                menuAbierto = false
                            },
                            leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
                        )

                    }
                }
            }
        }
    }
}
