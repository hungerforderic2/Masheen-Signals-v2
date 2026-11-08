package com.hungyplusthamasheen.masheensignals

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private data class Signal(
    val name: String,
    val color: Color,
    val description: String,
    val timings: LongArray
)

private val signals = listOf(
    Signal("Affection", Color(0xFFE879A8), "Gentle, flowing pulses", longArrayOf(0, 90, 70, 140, 90, 190)),
    Signal("Joy", Color(0xFFFFD54F), "Light, bouncing rhythm", longArrayOf(0, 65, 65, 65, 110, 65)),
    Signal("Curiosity", Color(0xFF4DD0E1), "Two inquisitive pulses", longArrayOf(0, 80, 130, 160)),
    Signal("Calm", Color(0xFF64B5F6), "Slow, sustained presence", longArrayOf(0, 260, 220)),
    Signal("Frustration", Color(0xFFEF5350), "Firm, interrupted rhythm", longArrayOf(0, 180, 90, 75, 70, 180)),
    Signal("Sadness", Color(0xFF7986CB), "Slow, spaced pulses", longArrayOf(0, 210, 260, 140)),
    Signal("Excitement", Color(0xFFFF9800), "Rapid, energetic rhythm", longArrayOf(0, 55, 45, 55, 45, 95, 45, 55)),
    Signal("Celebration", Color(0xFFAB80D6), "Layered celebratory rhythm", longArrayOf(0, 70, 70, 130, 70, 70, 70, 190))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MasheenSignalsScreen(this) }
    }
}

@Composable
private fun MasheenSignalsScreen(context: Context) {
    var selected by remember { mutableIntStateOf(0) }
    var intensity by remember { mutableFloatStateOf(3f) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    val signal = signals[selected]

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101820))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("MASHEEN SIGNALS", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Text("HLP • Expressive communication prototype", color = Color(0xFFB8C7D1))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Selected signal", style = MaterialTheme.typography.titleMedium)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Spacer(Modifier.height(28.dp).weight(0.04f).background(signal.color))
                        Text(signal.name, style = MaterialTheme.typography.headlineSmall)
                    }
                    Text(signal.description)
                    Text("Intensity: ${intensity.toInt()} / 5")
                    Slider(value = intensity, onValueChange = { intensity = it }, valueRange = 1f..5f, steps = 3)
                }
            }
            Text("Choose an expression", color = Color.White, style = MaterialTheme.typography.titleMedium)
            signals.forEachIndexed { index, item ->
                Button(
                    onClick = { selected = index },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selected == index) "●  ${item.name}" else item.name)
                }
            }
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Vibration")
                        Switch(checked = vibrationEnabled, onCheckedChange = { vibrationEnabled = it })
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Sound")
                        Switch(checked = soundEnabled, onCheckedChange = { soundEnabled = it })
                    }
                    Button(
                        onClick = { previewSignal(context, signal, intensity.toInt(), vibrationEnabled, soundEnabled) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Preview signal") }
                }
            }
            Text("Signals are designed expressions, not claims that a device literally feels emotions.", color = Color(0xFFB8C7D1))
        }
    }
}

private fun previewSignal(context: Context, signal: Signal, intensity: Int, vibration: Boolean, sound: Boolean) {
    if (vibration) {
        val vibrator = if (android.os.Build.VERSION.SDK_INT >= 31) {
            context.getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (vibrator.hasVibrator()) {
            val scale = 0.65f + (intensity - 1) * 0.18f
            val timings = signal.timings.mapIndexed { index, value ->
                if (index == 0) value else (value / scale).toLong().coerceAtLeast(25L)
            }.toLongArray()
            val amplitudes = IntArray(timings.size) { if (it == 0) 0 else (70 + intensity * 35).coerceAtMost(255) }
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        }
    }
    if (sound) {
        val tone = ToneGenerator(AudioManager.STREAM_NOTIFICATION, (35 + intensity * 12).coerceAtMost(95))
        tone.startTone(ToneGenerator.TONE_PROP_BEEP, 120 + intensity * 35)
        android.os.Handler(context.mainLooper).postDelayed({ tone.release() }, 500)
    }
}
