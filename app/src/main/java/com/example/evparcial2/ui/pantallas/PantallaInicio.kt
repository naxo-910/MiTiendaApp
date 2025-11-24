package com.example.evparcial2.ui.pantallas

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evparcial2.data.model.Usuario
import com.example.evparcial2.domain.viewmodels.CarritoViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    usuario: Usuario,
    carritoViewModel: CarritoViewModel,
    irProductos: () -> Unit,
    irCarrito: () -> Unit,
    irPedidos: () -> Unit,
    irGestion: () -> Unit,
    irPerfil: () -> Unit,
    irChats: () -> Unit = {}
) {
    // Observamos los items del carrito para el contador
    val itemsCarrito by carritoViewModel.items.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(onClick = irPerfil) {
                        Icon(
                            Icons.Default.Person, 
                            contentDescription = "Perfil",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            
            // Header mejorado con gradiente y animación
            item {
                val headerVisible by remember { mutableStateOf(true) }
                
                AnimatedVisibility(
                    visible = headerVisible,
                    enter = fadeIn(animationSpec = tween(800)) + 
                           slideInVertically(initialOffsetY = { -it })
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Avatar del usuario con animación de entrada
                            val avatarScale by animateFloatAsState(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ), 
                                label = "avatar_scale"
                            )
                            
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .scale(avatarScale)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Saludo personalizado con animación
                            Text(
                                text = "¡Hola, ${usuario.nombre}! 👋",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = if (usuario.rol == "admin") 
                                    "Panel de Gestión Hostal" 
                                else 
                                    "¿Listo para tu aventura?",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            // Espaciado después del header
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Botón principal destacado para ver productos con animación
            if (usuario.rol != "admin") {
                item {
                    var buttonVisible by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(Unit) {
                        delay(300)
                        buttonVisible = true
                    }
                    
                    AnimatedVisibility(
                        visible = buttonVisible,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(600, easing = EaseOutCubic)
                        ) + fadeIn(animationSpec = tween(600))
                    ) {
                        Card(
                            onClick = irProductos,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Hotel,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.White
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🏨 Explorar Alojamientos",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = "Hostales, habitaciones y experiencias únicas",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    )
                                }
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Grid de opciones principales con animación escalonada
            item {
                var gridVisible by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    delay(600)
                    gridVisible = true
                }
                
                AnimatedVisibility(
                    visible = gridVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(700, easing = EaseOutCubic)
                    ) + fadeIn(animationSpec = tween(700))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        // Primera fila
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Productos - MÁS PROMINENTE
                            TarjetaOpcion(
                                titulo = "🏨 Ver Alojamientos",
                                descripcion = "Hostales, habitaciones y más",
                                icono = Icons.Default.Hotel,
                                onClick = irProductos,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                isDestacado = true
                            )
                            
                            // Carrito con badge
                            TarjetaOpcionConBadge(
                                titulo = "Mis Reservas",
                                descripcion = "Revisa tus reservas",
                                icono = Icons.Default.ShoppingCart,
                                onClick = irCarrito,
                                modifier = Modifier.weight(1f),
                                badgeCount = itemsCarrito.size,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Segunda fila
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Pedidos
                            TarjetaOpcion(
                                titulo = "Mis Pedidos",
                                descripcion = "Historial de compras",
                                icono = Icons.AutoMirrored.Filled.ListAlt,
                                onClick = irPedidos,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.tertiaryContainer
                            )
                            
                            // Chats
                            TarjetaOpcion(
                                titulo = "Conversaciones",
                                descripcion = "Chatea con hosts",
                                icono = Icons.AutoMirrored.Filled.Chat,
                                onClick = irChats,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                        
                        // Botón de Admin (si es admin)
                        if (usuario.rol == "admin") {
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            TarjetaOpcion(
                                titulo = "Panel de Administrador",
                                descripcion = "Gestionar alojamientos y sistema",
                                icono = Icons.Default.Settings,
                                onClick = irGestion,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                                isAdmin = true
                            )
                        }
                    }
                }
            }
            
            // Espaciado inferior
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Composables mejorados para las tarjetas con animaciones
@Composable
private fun TarjetaOpcion(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    isAdmin: Boolean = false,
    isDestacado: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ), 
        label = "card_scale"
    )
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
    
    Card(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
            .height(120.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDestacado) 8.dp else 4.dp
        ),
        border = if (isDestacado) {
            androidx.compose.foundation.BorderStroke(
                2.dp, 
                Color.White.copy(alpha = 0.5f)
            )
        } else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(
                        when {
                            isDestacado -> 36.dp
                            isAdmin -> 32.dp
                            else -> 28.dp
                        }
                    ),
                    tint = when {
                        isDestacado -> Color.White
                        isAdmin -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = if (isDestacado) FontWeight.Bold else FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    color = when {
                        isDestacado -> Color.White
                        isAdmin -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = when {
                        isDestacado -> Color.White.copy(alpha = 0.9f)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    },
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun TarjetaOpcionConBadge(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    color: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    BadgedBox(
        badge = {
            if (badgeCount > 0) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ) { 
                    Text(
                        text = badgeCount.toString(),
                        style = MaterialTheme.typography.labelSmall
                    ) 
                }
            }
        }
    ) {
        TarjetaOpcion(
            titulo = titulo,
            descripcion = descripcion,
            icono = icono,
            onClick = onClick,
            modifier = modifier,
            color = color
        )
    }
}

// Composable anterior simplificado para compatibilidad
@Composable
private fun BotonMenu(
    texto: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icono, contentDescription = null)
            Text(texto)
        }
    }
}