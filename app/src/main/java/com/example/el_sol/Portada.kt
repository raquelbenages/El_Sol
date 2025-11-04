package com.example.el_sol

import android.R.attr.onClick
import android.app.ProgressDialog.show
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.TopAppBarDefaults
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
import com.google.rpc.Help
import kotlinx.coroutines.launch



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
        composable("portada") { Portada(nav)}
        composable ("Info") { Info() }
    }
}


// ---------- Main ----------
@Composable
fun Portada(navController: NavHostController) {

    val sol = remember { getinfoplaneta() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    @Composable
    fun DetailedDrawerExample(
        content: @Composable (PaddingValues) -> Unit
    ) {

        // ---------- Menu desplegable lateral ----------
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.erupcionsolar),
                            contentDescription = "FOTO NAVEGACION",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        NavigationDrawerItem(
                            label = { Text("Build") },
                            selected = false,
                            onClick = {  navController.navigate("Portada")},
                            icon = { Icon(Icons.Filled.Build, contentDescription = "build")}
                        )

                        NavigationDrawerItem(
                            label = { Text("Info") },
                            selected = false,
                            onClick = {navController.navigate("Info") },
                            icon = { Icon(Icons.Filled.Info, contentDescription = "info")}
                        )

                        NavigationDrawerItem(
                            label = { Text("Email") },
                            selected = false,
                            onClick = { /* Handle click */ },
                            icon = { Icon(Icons.Filled.Email, contentDescription = "Email")}
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            // ---------- Main info ----------
            Scaffold(
                bottomBar = { MyBottomAppBar(
                    string = "El sol",
                    onNavClick = { scope.launch { drawerState.open() } }
                ) },
            ) { inner ->
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
                            nombre = c.nombre
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
    DetailedDrawerExample { /* inner padding no usado en tu versión */ }
}


    // ---------- Menu inferior ----------
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
                IconButton(onClick = { /* do something */ }) {
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
fun SolCardSimple(
    foto: Int,
    nombre: String,

) {
    val context = LocalContext.current
    var show by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().clickable {
        Toast
            .makeText(context, nombre, Toast.LENGTH_SHORT)
            .show()
    }) {
        Column {
            Image(
                painter = painterResource(foto),
                contentDescription = nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nombre,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                Box {
                    IconButton(onClick = { show = !show }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones"
                        )
                    }

                    DropdownMenu(
                        expanded = show,
                        onDismissRequest = { show = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Copiar") },
                            onClick = {
                                // acción copiar
                                show = false
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Add, contentDescription = "Copiar")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                // acción eliminar
                                show = false
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                            }
                        )
                    }
                }
            }
        }
    }
}



