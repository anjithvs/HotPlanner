package com.example.hotplanner.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {

    // Auto-navigate after 2.9 seconds
    LaunchedEffect(Unit) {
        delay(2900)
        onNavigateToHome()
    }

    // ── Animation states ──────────────────────────────────────────────────────
    val logoScale  = remember { Animatable(0.25f) }
    val logoAlpha  = remember { Animatable(0f) }
    val textAlpha  = remember { Animatable(0f) }
    val textSlideY = remember { Animatable(24f) }
    val dotsAlpha  = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 1 — Logo bounces in
        launch { logoAlpha.animateTo(1f, tween(300)) }
        logoScale.animateTo(
            targetValue    = 1f,
            animationSpec  = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
        )
        // 2 — Text slides up
        delay(120)
        launch { textAlpha.animateTo(1f, tween(480)) }
        launch {
            textSlideY.animateTo(
                targetValue   = 0f,
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
            )
        }
        // 3 — Dots fade in
        delay(380)
        dotsAlpha.animateTo(1f, tween(400))
    }

    // ── Pulsing ring animation (infinite) ─────────────────────────────────────
    val ringAnim = rememberInfiniteTransition(label = "rings")
    val ring1 by ringAnim.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1900, easing = LinearEasing), RepeatMode.Restart),
        label = "r1"
    )
    val ring2 by ringAnim.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1900, 420, easing = LinearEasing), RepeatMode.Restart),
        label = "r2"
    )

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(CreamBg, CreamDark))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Logo + rings
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {

                // Rings drawn on Canvas
                Canvas(modifier = Modifier.size(200.dp)) {
                    val cx         = size.width / 2f
                    val cy         = size.height / 2f
                    val baseRadius = 57.dp.toPx()
                    // Ring 1
                    drawCircle(
                        color  = Latte,
                        radius = baseRadius * (0.55f + ring1),
                        center = Offset(cx, cy),
                        style  = Stroke(2.dp.toPx()),
                        alpha  = (1f - ring1).coerceIn(0f, 0.9f)
                    )
                    // Ring 2
                    drawCircle(
                        color  = Latte,
                        radius = baseRadius * (0.55f + ring2),
                        center = Offset(cx, cy),
                        style  = Stroke(1.5.dp.toPx()),
                        alpha  = ((1f - ring2) * 0.55f).coerceIn(0f, 0.55f)
                    )
                }

                // Coffee logo circle
                Box(
                    modifier = Modifier
                        .size(114.dp)
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value)
                        .background(
                            brush  = Brush.linearGradient(
                                colors = listOf(CoffeeBrown, CoffeeDeep),
                                start  = Offset(0f, 0f),
                                end    = Offset(114f, 114f)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", fontSize = 52.sp, color = Color.White)
                }
            }

            Spacer(Modifier.height(24.dp))

            // App name + tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset(y = textSlideY.value.dp)
            ) {
                Text(
                    text          = "HotPlanner",
                    style         = MaterialTheme.typography.displaySmall,
                    color         = CoffeeDark,
                    letterSpacing = (-1).sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text  = "Your tasks, beautifully organized",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Mocha
                )
            }

            Spacer(Modifier.height(56.dp))

            BouncingDots(modifier = Modifier.alpha(dotsAlpha.value))
        }
    }
}

@Composable
private fun BouncingDots(modifier: Modifier = Modifier) {
    val anim = rememberInfiniteTransition(label = "dots")
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        repeat(3) { i ->
            val offsetY by anim.animateFloat(
                initialValue  = 0f,
                targetValue   = -9f,
                animationSpec = infiniteRepeatable(
                    animation           = tween(420, easing = FastOutSlowInEasing),
                    repeatMode          = RepeatMode.Reverse,
                    initialStartOffset  = StartOffset(i * 160)
                ),
                label = "dot$i"
            )
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .offset(y = offsetY.dp)
                    .background(Latte, CircleShape)
            )
        }
    }
}