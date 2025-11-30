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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.evparcial2.domain.viewmodels.*
import com.example.evparcial2.domain.viewmodels.ChatEvent
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.ui.components.reviews.SeccionReviews
import com.example.evparcial2.ui.pantallas.*
import com.tienda.ui.screens.PantallaConvertirMonedas
import com.tienda.viewmodels.CurrencyViewModel

@Composable
fun NavPrincipal(
    navController: NavHostController = rememberNavController(),
    vm: ViewModelUsuarios = hiltViewModel(),
    vmProductos: ViewModelProductos = hiltViewModel(),
    carritoViewModel: CarritoViewModel = viewModel(),
    vmPedidos: ViewModelPedidos = hiltViewModel(),
    vmChat: ViewModelChat = hiltViewModel(),
    @Suppress("UNUSED_PARAMETER") vmReviews: ViewModelReviews = hiltViewModel(),
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val usuarioActual by vm.usuarioAutenticadoActual.collectAsState()

    // Navegación libre - no forzar login automático

    NavHost(
        navController = navController,
        startDestination = "productos"
    ) {
        // ... (login, registro, inicio se mantienen igual) ...

        composable("login") {
            PantallaInicioSesion(
                gestorUsuarios = vm,
                alIniciarSesionExitoso = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                alNavegarÁRegistro = {
                    navController.navigate("registro")
                }
            )
        }

        composable("registro") {
            PantallaCrearCuenta(
                gestorUsuarios = vm,
                alCrearCuentaExitoso = {
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = true }
                    }
                },
                alVolver = {
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
                    irChats = { navController.navigate("chats") },
                    irCurrency = { navController.navigate("currency_converter") }
                )
            }
        }

        // ... (productos, gestion, producto_form se mantienen igual) ...

        composable("productos") {
            PantallaAlojamientos(
                gestorProductos = vmProductos,
                gestorCarrito = carritoViewModel,
                esAdmin = usuarioActual?.rol == "admin",
                usuario = usuarioActual,
                onAgregarProducto = { navController.navigate("producto_form") },
                onEditarProducto = { producto ->
                    navController.navigate("producto_form/${producto.id}")
                },
                onVerDetalle = { producto ->
                    navController.navigate("producto_detalle/${producto.id}")
                },
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
            val producto = vmProductos.listaAlojamientosCompleta.collectAsState().value.find { it.id == id }
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
                        vmChat.onEvent(ChatEvent.CreateChat(
                            usuario1 = usuario,
                            usuario2 = Usuario(id = adminId, nombre = "Admin", email = "admin@hostal.com", rol = "admin"),
                            productoId = producto.id
                        ))
                        navController.navigate("chat/${producto.id}/${producto.nombre}")
                    }
                )
            }
        }

        composable("gestion") {
            PantallaAlojamientos(
                gestorProductos = vmProductos,
                gestorCarrito = carritoViewModel,
                esAdmin = true,
                onAgregarProducto = { navController.navigate("producto_form") },
                onEditarProducto = { producto ->
                    navController.navigate("producto_form/${producto.id}")
                },
                onVerDetalle = { _ ->
                    // navController.navigate("producto_detalle/${producto.id}")
                },
                onIrACarrito = { /* No hay carrito en gestión */ },
                onIrALogin = { navController.navigate("login") },
                onIrAInicio = { navController.navigate("inicio") }
            )
        }

        composable("producto_form") {
            PantallaFormularioAlojamiento(
                gestorAlojamientos = vmProductos,
                idAlojamientoAEditar = null,
                alVolver = { navController.popBackStack() },
                alGuardarExitosamente = { navController.popBackStack() }
            )
        }

        composable(
            route = "producto_form/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("productoId")
            PantallaFormularioAlojamiento(
                gestorAlojamientos = vmProductos,
                idAlojamientoAEditar = id,
                alVolver = { navController.popBackStack() },
                alGuardarExitosamente = { navController.popBackStack() }
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
                    val usuario = vm.usuarioAutenticadoActual.value
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
                        vm.cerrarSesionUsuarioActual()
                    }
                )
            }
        }

        // Pantallas de Chat
        composable("chats") {
            PantallaListaChats(
                vmChat = vmChat,
                onNavigateToChat = { chatId ->
                    navController.navigate("chat_detalle/$chatId")
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = "chat/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getLong("productoId") ?: 0L
            val usuario = usuarioActual
            
            if (usuario != null) {
                // Crear un nuevo chat para el producto
                LaunchedEffect(productoId) {
                    // Aquí crearías el chat, pero por ahora navega a un chat demo
                    navController.navigate("chat_detalle/1")
                }
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
                    vmChat = vmChat,
                    usuarioActual = usuario,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
        
        // Pantalla de Conversión de Monedas
        composable("currency_converter") {
            PantallaConvertirMonedas(
                alVolver = { navController.popBackStack() },
                gestorMonedas = currencyViewModel
            )
        }
    }
}