package com.example.evparcial2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.evparcial2.domain.viewmodels.ViewModelProductos
import com.example.evparcial2.domain.viewmodels.ViewModelUsuarios
import com.example.evparcial2.domain.viewmodels.CarritoViewModel
import com.example.evparcial2.domain.viewmodels.ViewModelPedidos
import com.example.evparcial2.domain.viewmodels.ViewModelChat
import com.example.evparcial2.domain.viewmodels.ChatEvent
import com.example.evparcial2.ui.pantallas.*

@Composable
fun NavPrincipal(
    navController: NavHostController = rememberNavController(),
    vm: ViewModelUsuarios = viewModel(),
    vmProductos: ViewModelProductos = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel(),
    vmPedidos: ViewModelPedidos = viewModel(),
    vmChat: ViewModelChat = viewModel()
) {
    val usuarioActual by vm.usuarioActual.collectAsState()

    // Navegación libre - no forzar login automático

    NavHost(
        navController = navController,
        startDestination = "productos"
    ) {
        // ... (login, registro, inicio se mantienen igual) ...

        composable("login") {
            PantallaLogin(
                vm = vm,
                onLoginExitoso = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onIrARegistro = {
                    navController.navigate("registro")
                }
            )
        }

        composable("registro") {
            PantallaRegistro(
                vm = vm,
                onRegistroExitoso = {
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = true }
                    }
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable("inicio") {
            val usuario = usuarioActual
            if (usuario != null) {
                PantallaInicio(
                    usuario = usuario,
                    carritoViewModel = carritoViewModel,
                    irProductos = { navController.navigate("productos") },
                    irCarrito = { navController.navigate("carrito") },
                    irPedidos = { navController.navigate("pedidos") },
                    irGestion = { navController.navigate("gestion") },
                    irPerfil = { navController.navigate("perfil") },
                    irChats = { navController.navigate("chats") }
                )
            }
        }

        // ... (productos, gestion, producto_form se mantienen igual) ...

        composable("productos") {
            PantallaProductos(
                vm = vmProductos,
                carritoViewModel = carritoViewModel,
                esAdmin = usuarioActual?.rol == "admin",
                usuario = usuarioActual,
                onAgregarProducto = { navController.navigate("producto_form") },
                onEditarProducto = { producto ->
                    navController.navigate("producto_form/${producto.id}")
                },
                onVerDetalle = { producto ->
                    navController.navigate("producto_detalle/${producto.id}")
                },
                onVolver = { navController.popBackStack() },
                onIrACarrito = { 
                    if (usuarioActual != null) {
                        navController.navigate("carrito")
                    } else {
                        navController.navigate("login")
                    }
                },
                onIrALogin = { navController.navigate("login") },
                onIrAInicio = { 
                    if (usuarioActual != null) {
                        navController.navigate("inicio")
                    } else {
                        navController.navigate("login")
                    }
                }
            )
        }

        composable(
            route = "producto_detalle/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("productoId")
            val producto = vmProductos.productos.collectAsState().value.find { it.id == id }
            val usuario = usuarioActual // Variable local para smart cast
            if (producto != null && usuario != null) {
                PantallaDetalle(
                    producto = producto,
                    esAdmin = usuario.rol == "admin",
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onAgregarCarrito = { prod ->
                        if (usuario.rol != "admin") {
                            carritoViewModel.agregarProducto(prod)
                        }
                    },
                    onIniciarChat = {
                        // Navegar al chat (asumiendo que el admin tiene ID 1)
                        val adminId = 1L
                        vmChat.onEvent(ChatEvent.IniciarChat(
                            productoId = producto.id,
                            nombreProducto = producto.nombre,
                            usuario = usuario,
                            otroUsuarioId = adminId
                        ))
                        navController.navigate("chat/${producto.id}/${producto.nombre}")
                    }
                )
            }
        }

        composable("gestion") {
            PantallaProductos(
                vm = vmProductos,
                carritoViewModel = carritoViewModel,
                esAdmin = true,
                onAgregarProducto = { navController.navigate("producto_form") },
                onEditarProducto = { producto ->
                    navController.navigate("producto_form/${producto.id}")
                },
                onVerDetalle = { _ ->
                    // navController.navigate("producto_detalle/${producto.id}")
                },
                onVolver = { navController.popBackStack() },
                onIrACarrito = { /* No hay carrito en gestión */ }
            )
        }

        composable("producto_form") {
            PantallaFormProducto(
                vm = vmProductos,
                productoId = null,
                onVolver = { navController.popBackStack() },
                onGuardar = { navController.popBackStack() }
            )
        }

        composable(
            route = "producto_form/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("productoId")
            PantallaFormProducto(
                vm = vmProductos,
                productoId = id,
                onVolver = { navController.popBackStack() },
                onGuardar = { navController.popBackStack() }
            )
        }

        // --- ¡¡ARREGLO IMPORTANTE AQUÍ!! ---
        composable("carrito") {
            PantallaCarrito(
                carritoViewModel = carritoViewModel,
                onVolver = { navController.popBackStack() },
                onConfirmar = {
                    // --- ¡¡ESTA ES LA NUEVA LÓGICA!! ---
                    // 1. Obtener el usuario
                    val usuario = vm.usuarioActual.value
                    // 2. Obtener los items del carrito
                    val items = carritoViewModel.items.value.values.toList()

                    if (usuario != null && items.isNotEmpty()) {
                        // 3. ¡Llamar al ViewModel de Pedidos para crear el pedido!
                        vmPedidos.crearNuevoPedido(usuario, items)

                        // 4. Vaciar el carrito
                        carritoViewModel.confirmarPedido() // Esto vacía el carrito

                        // 5. Navegar a la pantalla de pedidos
                        navController.navigate("pedidos")
                    }
                    // --- FIN DE LA LÓGICA ---
                }
            )
        }

        // --- ¡¡Y ARREGLO AQUÍ!! ---
        composable("pedidos") {
            PantallaPedidos(
                vmPedidos = vmPedidos, // <-- ¡Le pasamos el VM!
                onVolver = { navController.popBackStack() }
            )
        }

        // ... (perfil se mantiene igual) ...
        composable("perfil") {
            val usuario = usuarioActual
            if (usuario != null) {
                PantallaPerfil(
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onCerrarSesion = {
                        vm.cerrarSesion()
                    }
                )
            }
        }

        // Pantallas de Chat
        composable("chats") {
            val usuario = usuarioActual
            if (usuario != null) {
                PantallaListaChats(
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    onAbrirChat = { chatId ->
                        navController.navigate("chat_detalle/$chatId")
                    },
                    viewModelChat = vmChat
                )
            }
        }

        composable(
            route = "chat/{productoId}/{nombreProducto}",
            arguments = listOf(
                navArgument("productoId") { type = NavType.LongType },
                navArgument("nombreProducto") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombreProducto = backStackEntry.arguments?.getString("nombreProducto") ?: ""
            val chatUiState by vmChat.chatUiState.collectAsState()
            
            val usuario = usuarioActual
            if (chatUiState.chatId != null && usuario != null) {
                PantallaChat(
                    chatId = chatUiState.chatId!!,
                    nombreProducto = nombreProducto,
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    viewModelChat = vmChat
                )
            }
        }

        composable(
            route = "chat_detalle/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
            val usuario = usuarioActual
            if (usuario != null) {
                PantallaChat(
                    chatId = chatId,
                    nombreProducto = "Chat",
                    usuario = usuario,
                    onVolver = { navController.popBackStack() },
                    viewModelChat = vmChat
                )
            }
        }
    }
}