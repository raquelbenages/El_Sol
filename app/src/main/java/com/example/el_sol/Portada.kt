package com.example.el_sol

import android.R.attr.onClick
import android.R.attr.text
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.AnnotatedString
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
    var sol by remember { mutableStateOf(getinfoplaneta()) }  // 👈 lista mutable
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    @Composable
    fun DetailedDrawerExample(
        content: @Composable (PaddingValues) -> Unit
    ) {
        ModalNavigationDrawer(
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
                            label = { Text("Build") },
                            selected = false,
                            onClick = { navController.navigate("Portada") },
                            icon = { Icon(Icons.Filled.Build, contentDescription = "build") }
                        )
                        NavigationDrawerItem(
                            label = { Text("Info") },
                            selected = false,
                            onClick = { navController.navigate("Info") },
                            icon = { Icon(Icons.Filled.Info, contentDescription = "info") }
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                bottomBar = {
                    MyBottomAppBar(
                        string = "El sol",
                        onNavClick = { scope.launch { drawerState.open() } }
                    )
                }
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
                    items(sol, key = { it.nombre }) { c ->
                        SolCardSimple(
                            foto = c.foto,
                            nombre = c.nombre,
                            onDelete = {
                                sol = sol.filterNot { it.nombre == c.nombre } // 👈 elimina la card
                            }
                            //ONCOPY Y YA
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }

    DetailedDrawerExample { }
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
    onDelete: () -> Unit, // 👈 nueva función
) {
    val context = LocalContext.current
    var menuAbierto by remember { mutableStateOf(false) }
    var pastedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Toast.makeText(context, nombre, Toast.LENGTH_SHORT).show()
            }
    ) {
        Column {
            val painter = if (pastedImageUri != null) {
                rememberAsyncImagePainter(model = pastedImageUri)
            } else {
                painterResource(foto)
            }

            Image(
                painter = painter,
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
                    IconButton(onClick = { menuAbierto = !menuAbierto }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                    }

                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Copiar imagen") },
                            onClick = {
                                copyDrawableToClipboard(context, foto, label = nombre)
                                Toast.makeText(context, "Imagen copiada", Toast.LENGTH_SHORT).show()
                                menuAbierto = false
                            },
                            leadingIcon = { Icon(Icons.Filled.Add, contentDescription = "Copiar") }
                        )
                        DropdownMenuItem(
                            text = { Text("Pegar imagen") },
                            onClick = {
                                val uri = getImageUriFromClipboard(context)
                                if (uri != null) {
                                    pastedImageUri = uri
                                    Toast.makeText(context, "Imagen pegada", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "No hay imagen en el portapapeles", Toast.LENGTH_SHORT).show()
                                }
                                menuAbierto = false
                            },
                            leadingIcon = { Icon(Icons.Filled.Check, contentDescription = "Pegar") }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                onDelete() // 👈 llama a la función pasada desde Portada
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
