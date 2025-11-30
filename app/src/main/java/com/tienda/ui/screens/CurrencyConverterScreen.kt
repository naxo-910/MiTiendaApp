package com.tienda.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tienda.viewmodels.CurrencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConvertirMonedas(
    alVolver: () -> Unit,
    gestorMonedas: CurrencyViewModel = hiltViewModel()
) {
    val estadoConversor by gestorMonedas.uiState.collectAsState()
    
    var showFromCurrencyMenu by remember { mutableStateOf(false) }
    var showToCurrencyMenu by remember { mutableStateOf(false) }

    // Diálogo para mostrar errores
    estadoConversor.error?.let { mensajeError ->
        AlertDialog(
            onDismissRequest = { gestorMonedas.onEvent(CurrencyViewModel.CurrencyEvent.DismissError) },
            title = { Text("Error en la Conversión") },
            text = { Text(mensajeError) },
            confirmButton = {
                TextButton(
                    onClick = { gestorMonedas.onEvent(CurrencyViewModel.CurrencyEvent.DismissError) }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Conversor de Monedas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = alVolver) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💱",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Convierte entre diferentes monedas",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Amount Input Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Monto a convertir",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = estadoConversor.amount,
                        onValueChange = { gestorMonedas.onEvent(CurrencyViewModel.CurrencyEvent.UpdateAmount(it)) },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }

            // Currency Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Selecciona las monedas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // From Currency
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { showFromCurrencyMenu = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("De", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = estadoConversor.fromCurrency,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            DropdownMenu(
                                expanded = showFromCurrencyMenu,
                                onDismissRequest = { showFromCurrencyMenu = false }
                            ) {
                                estadoConversor.supportedCurrencies.forEach { currency ->
                                    DropdownMenuItem(
                                        text = { Text("${currency.code} - ${currency.name}") },
                                        onClick = {
                                            gestorMonedas.onEvent(CurrencyViewModel.CurrencyEvent.UpdateFromCurrency(currency.code))
                                            showFromCurrencyMenu = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        // Swap Button
                        IconButton(
                            onClick = { gestorMonedas.onEvent(CurrencyViewModel.CurrencyEvent.SwapCurrencies) }
                        ) {
                            Icon(
                                Icons.Default.SwapVert,
                                contentDescription = "Intercambiar monedas",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        // To Currency
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { showToCurrencyMenu = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("A", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = estadoConversor.toCurrency,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            DropdownMenu(
                                expanded = showToCurrencyMenu,
                                onDismissRequest = { showToCurrencyMenu = false }
                            ) {
                                estadoConversor.supportedCurrencies.forEach { currency ->
                                    DropdownMenuItem(
                                        text = { Text("${currency.code} - ${currency.name}") },
                                        onClick = {
                                            gestorMonedas.onEvent(CurrencyViewModel.CurrencyEvent.UpdateToCurrency(currency.code))
                                            showToCurrencyMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Convert Button
            Button(
                onClick = { 
                    gestorMonedas.onEvent(
                        CurrencyViewModel.CurrencyEvent.ConvertAmount(
                            estadoConversor.amount,
                            estadoConversor.fromCurrency,
                            estadoConversor.toCurrency
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !estadoConversor.isLoading && estadoConversor.amount.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (estadoConversor.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "CONVERTIR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Result Card
            if (estadoConversor.convertedAmount.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Resultado",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${estadoConversor.amount} ${estadoConversor.fromCurrency}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = " = ",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "${estadoConversor.convertedAmount} ${estadoConversor.toCurrency}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        if (estadoConversor.exchangeRate.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tasa de cambio: 1 ${estadoConversor.fromCurrency} = ${estadoConversor.exchangeRate} ${estadoConversor.toCurrency}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Information about API Integration
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "ℹ️ Información",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Esta funcionalidad integra datos de tasas de cambio actualizadas para proporcionar conversiones precisas entre múltiples monedas internacionales.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}