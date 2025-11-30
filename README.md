# 🏨 Aplicación de Alojamientos Turísticos

Una aplicación Android moderna para reservas de alojamientos turísticos desarrollada con **Kotlin**, **Jetpack Compose** y arquitectura **MVVM**. Completamente **humanizada en español** con variables descriptivas y mensajes amigables.

## ✨ Estado Actual - Aplicación Funcional

La aplicación ha sido **completamente humanizada y optimizada**:
- ✅ **Código limpio** con variables descriptivas en español
- ✅ **Interfaz amigable** con mensajes humanizados y emojis
- ✅ **Aplicación ejecutándose** correctamente sin errores
- ✅ **Configuración Hilt** completamente funcional
- ✅ **ViewModels optimizados** con inyección de dependencias

## 🚀 Características Principales

### 📱 Funcionalidades de la App
- **Navegación libre** sin login obligatorio  
- **Sistema de filtros avanzado** por país, ciudad y tipo de habitación 🏖️ 🏔️ 🏙️
- **Carrito de reservas** con gestión inteligente
- **Sistema de reseñas** para cada alojamiento ⭐
- **Chat interno** entre huéspedes y administradores 💬
- **Conversión de moneda** en tiempo real 💱
- **Interfaz humanizada** con Material Design 3

### 🛠️ Tecnologías Utilizadas
- **Kotlin** + **Jetpack Compose** - UI moderna y declarativa
- **MVVM Architecture** con **StateFlow/Flow** - Gestión reactiva de estados
- **Dagger Hilt** - Inyección de dependencias (completamente configurado)
- **Retrofit** + **OkHttp** - Comunicación con APIs REST
- **Coil** - Carga optimizada de imágenes
- **Material Design 3** - Interfaz moderna y accesible
- **Corrutinas** - Programación asíncrona
- **Navigation Compose** - Navegación declarativa

## 🏗️ Arquitectura de la Aplicación

### 📦 Módulos Principales

#### 1. **ViewModels Humanizados** (Todos con Hilt)
- `ViewModelProductos` - Gestión de alojamientos con variables descriptivas
- `ViewModelUsuarios` - Manejo de autenticación y perfiles
- `ViewModelReviews` - Sistema de reseñas y calificaciones
- `ViewModelChat` - Comunicaciones internas
- `ViewModelPedidos` - Gestión de reservas
- `CarritoViewModel` - Carrito de compras inteligente
- `ViewModelForm` - Formularios dinámicos
- `CurrencyViewModel` - Conversión de monedas

#### 2. **Pantallas Principales**
- `PantallaProductos` - **"Explora Alojamientos Increíbles"** 🏖️
- `PantallaDetalle` - Información detallada con reseñas
- `PantallaCarrito` - Gestión de reservas seleccionadas
- `PantallaChat` - Comunicación con administradores
- `PantallaPedidos` - Historial de reservas
- `PantallaUsuarios` - Gestión de perfiles

#### 3. **Componentes Reutilizables**
- `PlantillaProducto` - Tarjetas de alojamiento humanizadas
- `SeccionReviews` - Sistema completo de calificaciones
- `ComponentesFiltro` - Filtros avanzados con emojis
- `BarraNavegacion` - Navegación intuitiva

### 🌐 Funcionalidades Integradas

#### 💱 **Conversión de Monedas**
- **Servicio:** Fixer.io para tasas en tiempo real
- **Monedas soportadas:** CLP, USD, EUR, ARS, PEN, COP
- **Actualización automática** de precios por ubicación

#### 📱 **Experiencia de Usuario Mejorada**
- **Mensajes humanizados:** "¡Encuentra tu alojamiento perfecto!" 
- **Emojis descriptivos:** 🏖️ Costa | 🏔️ Montaña | 🏙️ Ciudad
- **Filtros intuitivos:** Por tipo de experiencia deseada
- **Estados descriptivos:** "Cargando alojamientos increíbles..." ✨

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/evparcial2/
├── data/
│   ├── model/               # Modelos de datos humanizados
│   │   ├── Producto.kt      # Alojamientos con campos descriptivos
│   │   ├── Usuario.kt       # Usuarios con perfiles completos  
│   │   ├── Review.kt        # Sistema de reseñas
│   │   ├── Chat.kt          # Mensajes internos
│   │   └── ItemCarrito.kt   # Items del carrito
│   └── repository/          # Repositorios con lógica de negocio
│       └── BasicRepositories.kt
├── di/                      # Configuración de Hilt (completa)
│   └── RepositoryModule.kt  # Módulos de dependencias
├── domain/
│   └── viewmodels/          # ViewModels con Hilt configurado
│       ├── ViewModelProductos.kt    # Variables en español descriptivas
│       ├── ViewModelUsuarios.kt     # Gestión humanizada de usuarios
│       ├── ViewModelReviews.kt      # Calificaciones y comentarios
│       ├── ViewModelChat.kt         # Chat interno
│       ├── ViewModelPedidos.kt      # Reservas y pedidos
│       ├── CarritoViewModel.kt      # Carrito inteligente
│       └── ViewModelForm.kt         # Formularios dinámicos
├── ui/
│   ├── components/          # Componentes reutilizables
│   │   ├── common/          # Plantillas humanizadas
│   │   └── reviews/         # Sistema completo de reseñas
│   ├── pantallas/           # Pantallas principales
│   │   ├── PantallaProductos.kt    # "Explora Alojamientos" 
│   │   ├── PantallaDetalle.kt      # Detalles con reseñas
│   │   ├── PantallaCarrito.kt      # Gestión de reservas
│   │   └── PantallaChat.kt         # Chat integrado
│   └── navigation/          # Navegación con Hilt
│       └── NavPrincipal.kt
└── util/                    # Utilidades
    └── Validadores.kt       # Validaciones en español
```

## 🔧 Configuración y Desarrollo

### ✅ Estado de Compilación
- **Última compilación:** `BUILD SUCCESSFUL` ✅
- **Configuración Hilt:** Totalmente funcional ✅ 
- **ViewModels:** Todos configurados con `@HiltViewModel` e `@Inject`
- **MainActivity:** Configurada con `@AndroidEntryPoint`
- **Aplicación:** Ejecutándose correctamente sin crashes

### 🚀 Ejecución en Desarrollo
```bash
# Compilar proyecto
./gradlew assembleDebug

# Instalar en dispositivo/emulador  
./gradlew installDebug

# Limpiar y recompilar (si es necesario)
./gradlew clean assembleDebug
```

## 🎯 Características Humanizadas Implementadas

### 📝 **Variables Descriptivas en Español**
```kotlin
// Antes: items, products, users
// Ahora: alojamientosDisponibles, huespedRegistrados, reservasConfirmadas

val listaAlojamientosCompleta = mutableStateOf<List<Producto>>(emptyList())
val alojamientosFiltrados = mutableStateOf<List<Producto>>(emptyList())  
val estadoCargaAlojamientos = mutableStateOf(false)
val mensajeEstadoUsuario = mutableStateOf("¡Bienvenido! Explora alojamientos increíbles ✨")
```

### 💬 **Mensajes Amigables con Emojis**
- **Estados de carga:** "Cargando alojamientos increíbles..." ✨
- **Filtros:** "🏖️ Experiencia Costera" | "🏔️ Aventura en Montaña" 
- **Carrito:** "🛒 ¡Genial! Alojamiento agregado a tus favoritos"
- **Errores:** "😅 Ups, algo no salió bien. ¡Inténtalo de nuevo!"

### 🏗️ **Configuración Hilt Completa**
```kotlin
@HiltViewModel
class ViewModelProductos @Inject constructor(
    private val repositoryController: BasicRepositories
) : ViewModel() { /* ... */ }

@AndroidEntryPoint  
class MainActivity : ComponentActivity() { /* ... */ }
```

## 🏨 Alojamientos de Ejemplo

La aplicación incluye **alojamientos turísticos variados** en múltiples países:

### 🌎 **Destinos Disponibles**
- **🇨🇱 Chile:** Santiago, Valparaíso, San Pedro de Atacama, Pucón, Viña del Mar
- **🇦🇷 Argentina:** El Calafate, Buenos Aires  
- **🇵🇪 Perú:** Lima, Cusco
- **🇨🇴 Colombia:** Cartagena, Medellín

### 🏷️ **Rangos de Precio**
- **Económico:** Desde $18,000 CLP por noche 💰
- **Medio:** $45,000 - $80,000 CLP por noche 🏨
- **Premium:** Hasta $150,000 CLP por noche ⭐

### 🏖️ **Tipos de Experiencia**
- **🏖️ Experiencia Costera** - Alojamientos frente al mar
- **🏔️ Aventura en Montaña** - Cabañas y refugios de montaña  
- **🏙️ Vida Urbana** - Hoteles y apart-hoteles en ciudades

## 🔧 Instalación y Ejecución

### 📋 **Requisitos Previos**
- **Android Studio** Hedgehog | 2023.1.1 o superior
- **JDK 17** o superior  
- **Android SDK API 34+**
- **Emulador Android** o dispositivo físico

### 🚀 **Pasos de Instalación**
```bash
# 1. Clonar repositorio
git clone https://github.com/naxo-910/MiTiendaApp.git
cd tiendav2-main

# 2. Abrir en Android Studio
# Sincronizar proyecto (Sync Project with Gradle Files)

# 3. Compilar e instalar
./gradlew clean assembleDebug
./gradlew installDebug

# 4. Ejecutar aplicación
# La app se abrirá automáticamente o desde el menú de apps del dispositivo
```

### ⚡ **Verificación de Funcionamiento**
Al abrir la aplicación deberías ver:
- ✅ Pantalla principal con **"Explora Alojamientos Increíbles"** 🏖️
- ✅ Lista de alojamientos con **filtros intuitivos**  
- ✅ **Navegación fluida** sin crashes
- ✅ **Carrito de reservas** funcional

## 🎉 Estado del Proyecto - COMPLETADO

### ✅ **Logros Alcanzados**
- **🧹 Código limpiado** con variables descriptivas en español
- **🔧 Errores corregidos** y aplicación estable  
- **👥 Interfaz humanizada** con mensajes amigables y emojis
- **⚙️ Configuración Hilt** completamente funcional
- **📱 Aplicación ejecutándose** sin errores en emulador/dispositivo

### 🏗️ **Arquitectura Implementada**
- **MVVM** con ViewModels configurados con Hilt
- **Inyección de dependencias** totalmente funcional
- **Navegación declarativa** con Jetpack Compose Navigation
- **Gestión de estados** reactiva con StateFlow
- **Componentes reutilizables** y código modular

### 🎨 **Experiencia de Usuario**
- **Interfaz intuitiva** con Material Design 3
- **Mensajes descriptivos** y emojis contextuales  
- **Filtros avanzados** por tipo de experiencia
- **Carrito inteligente** para gestionar reservas
- **Sistema de reseñas** completo con calificaciones

---

📧 **Contacto:** Para consultas sobre la implementación de los microservicios o configuración del proyecto.