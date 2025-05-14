/* File: MainActivity.kt */
package com.example.myapplication12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicia la interfaz de usuario con Jetpack Compose
        setContent {
            MaterialTheme {
                // Establece un fondo negro y muestra la pantalla del evaluador
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    EvaluadorScreen()
                }
            }
        }
    }
}

@Composable
fun EvaluadorScreen() {
    // Variables de estado para entrada, errores, diálogo y resultado
    var nota by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var resultado by remember { mutableStateOf(Pair("", "")) }

    // Contenedor principal centrado
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título pequeño
        Text(
            text = "Parcial 1",
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Título principal
        Text(
            text = "Evaluador de Notas",
            fontSize = 24.sp,
            color = Color(0xFF00FFF7),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo para ingresar la nota
        OutlinedTextField(
            value = nota,
            onValueChange = { nuevoValor ->
                // Validación: solo números, máx. 3 cifras, 0-100
                if (nuevoValor.all { it.isDigit() } && nuevoValor.length <= 3) {
                    val num = nuevoValor.toIntOrNull()
                    if (num == null || (num in 0..100) || nuevoValor.isEmpty()) {
                        nota = nuevoValor
                    } else if (nuevoValor.length > 1 && num > 100) {
                        nota = nuevoValor.substring(0, 3)
                    }
                } else if (nuevoValor.isEmpty()) {
                    nota = nuevoValor
                }
            },
            label = {
                Text(
                    "Ingrese una nota (0 a 100)",
                    color = Color.White.copy(alpha = 0.8f)
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0x1AFFFFFF),
                unfocusedContainerColor = Color(0x1AFFFFFF),
                focusedLabelColor = Color(0xFF00FFF7),
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                cursorColor = Color(0xFF00FFF7),
                focusedIndicatorColor = Color(0xFF00FFF7),
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
            ),
            isError = showError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Muestra mensaje de error si la validación falla
        if (showError) {
            Text(
                text = "Por favor, ingresa un número válido entre 0 y 100.",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para evaluar la nota ingresada
        Button(
            onClick = {
                // Validar si la nota no está vacía ni fuera de rango
                if (nota.isBlank()) {
                    showError = true
                    return@Button
                }
                val valor = nota.toFloatOrNull()
                if (valor == null || valor !in 0f..100f) {
                    showError = true
                    return@Button
                }
                // Si es válida, evaluamos y mostramos el resultado
                showError = false
                resultado = evaluarNota(valor)
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FFF7),
                contentColor = Color.Black
            )
        ) {
            Text("Evaluar", fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Créditos
        Text(
            text = "Desarrollado por:\nKeneth Benavidez y Gabriel Ruiz",
            color = Color.Gray.copy(alpha = 0.8f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }

    // Diálogo emergente con el resultado de la nota
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            modifier = Modifier.wrapContentSize(),
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            showDialog = false
                            nota = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cerrar", fontWeight = FontWeight.Bold)
                    }
                }
            },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Resultado",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Letra de calificación
                    Text(
                        text = resultado.first,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00FFF7).copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // Mensaje aleatorio motivacional
                    Text(
                        text = resultado.second,
                        fontSize = 16.sp,
                        color = Color.LightGray.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            },
            containerColor = Color(0xFF1A1A1A),
            shape = RoundedCornerShape(16.dp),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
}

// Función que evalúa la nota y retorna letra y mensaje aleatorio
fun evaluarNota(nota: Float): Pair<String, String> {
    val mensajesA = listOf(
        "¡Excelente desempeño! Eres brillante.",
        "¡Trabajo sobresaliente! Sigue así.",
        "¡Perfecto! Un resultado impresionante.",
        "¡Notaza! ¿Eres un robot o qué? 🤖",
        "Sacaste A: ¿Vas a compartir tus poderes o los guardas?",
        "¡Perfecto! ¿Necesitas un ascenso a profesor?"
    )

    val mensajesB = listOf(
        "Muy buen trabajo. Estás progresando muy bien.",
        "¡Buenísimo! Has demostrado un gran entendimiento.",
        "¡Notable esfuerzo! Continúa con esa dedicación.",
        "B de 'Bueno, pero podrías invitarme a estudiar' 📚",
        "Casi llegas al podio... ¿Quieres un empujoncito?",
        "B de 'Bien, pero sin presumir' 😎"
    )

    val mensajesC = listOf(
        "Buen esfuerzo, puedes mejorar si te enfocas un poco más.",
        "Aprobado. Hay áreas donde puedes crecer.",
        "Suficiente, pero sé que puedes dar más.",
        "C de 'Conforme... pero no muy conforme' 🤷‍♂️",
        "Aprobaste por los pelos: ¿Fue suerte o cálculo?",
        "Resultado tipo sandwich: Ni frío ni caliente 🥪"
    )

    val mensajesD = listOf(
        "Necesitas más práctica para afianzar los conceptos.",
        "Estás cerca, pero requiere un poco más de estudio.",
        "Hay que reforzar algunos temas, ¡no te desanimes!",
        "D de 'Uy, casi... pero no' 😬",
        "¿Estudiaste con el libro bajo la almohada? 😴",
        "Resultado tipo café: Necesita más preparación ☕"
    )

    val mensajesF = listOf(
        "Reprobado. No te rindas, revisa el material y vuelve a intentarlo.",
        "Necesitas dedicar más tiempo al estudio. ¡Tú puedes!",
        "Esta vez no fue, pero el próximo esfuerzo valdrá la pena.",
        "F de '¡Fenomenal... mente mal!' Pero ánimo 💪",
        "¿Fue un ataque de pánico o te gustan los desafíos? 😅",
        "Reprobado: Hora de cambiar la estrategia de 'estudio' 📖"
    )

    // Determinar letra según el rango de la nota y retornar mensaje aleatorio
    return when {
        nota >= 91 -> "A" to (mensajesA.randomOrNull() ?: "¡Excelente desempeño!")
        nota >= 81 -> "B" to (mensajesB.randomOrNull() ?: "Muy buen trabajo.")
        nota >= 71 -> "C" to (mensajesC.randomOrNull() ?: "Buen esfuerzo, puedes mejorar.")
        nota >= 61 -> "D" to (mensajesD.randomOrNull() ?: "Necesitas más práctica.")
        else -> "F" to (mensajesF.randomOrNull() ?: "Reprobado. No te rindas, sigue estudiando.")
    }
}
