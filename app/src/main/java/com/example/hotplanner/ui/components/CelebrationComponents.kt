package com.example.hotplanner.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

// ── Confetti Rain ──────────────────────────────────────────────────────────────

private data class ConfettiPiece(
    val x: Float,           // 0..1 normalized screen width
    val size: Float,        // dp size
    val color: Color,
    val delay: Float,       // 0..0.25 normalized delay before falling
    val duration: Float,    // 0.4..0.75 normalized fall duration
    val startRotation: Float,
    val isCircle: Boolean
)

@Composable
fun ConfettiOverlay(visible: Boolean, onFinish: () -> Unit) {
    if (!visible) return

    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue   = 1f,
            animationSpec = tween(durationMillis = 3500, easing = LinearEasing)
        )
        delay(100)
        onFinish()
    }

    val colors = listOf(
        CoffeeBrown, Latte,
        Color(0xFFE74C3C), Color(0xFFF39C12),
        Color(0xFF27AE60), Color(0xFF3498DB),
        Color(0xFF9B59B6), Color(0xFFE91E63)
    )

    val pieces = remember {
        List(55) { i ->
            ConfettiPiece(
                x             = Random.nextFloat(),
                size          = 4f + Random.nextFloat() * 10f,
                color         = colors[i % colors.size],
                delay         = Random.nextFloat() * 0.25f,
                duration      = 0.4f + Random.nextFloat() * 0.35f,
                startRotation = Random.nextFloat() * 360f,
                isCircle      = Random.nextBoolean()
            )
        }
    }

    val p = progress.value

    Canvas(modifier = Modifier.fillMaxSize()) {
        pieces.forEach { piece ->
            val localP = ((p - piece.delay) / piece.duration).coerceIn(0f, 1f)
            if (localP <= 0f) return@forEach

            val xPos   = piece.x * size.width
            val yPos   = localP * (size.height + 120.dp.toPx())
            val alpha  = if (localP < 0.75f) 1f
            else ((1f - (localP - 0.75f) / 0.25f)).coerceIn(0f, 1f)
            val halfSz = piece.size.dp.toPx() / 2f
            val rot    = piece.startRotation + localP * 720f

            withTransform({
                translate(xPos - halfSz, yPos - halfSz)
                rotate(rot, pivot = Offset(halfSz, halfSz))
            }) {
                if (piece.isCircle) {
                    drawCircle(
                        color  = piece.color.copy(alpha = alpha),
                        radius = halfSz,
                        center = Offset(halfSz, halfSz)
                    )
                } else {
                    drawRect(
                        color = piece.color.copy(alpha = alpha),
                        size  = Size(halfSz * 2f, halfSz * 2f)
                    )
                }
            }
        }
    }
}

// ── "Task Complete!" Banner ────────────────────────────────────────────────────

@Composable
fun CelebrationBanner(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(200)) + scaleIn(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)),
        exit    = fadeOut(tween(300)) + scaleOut(tween(300))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(CreamCard, RoundedCornerShape(22.dp))
                    .border(1.5.dp, BorderColor, RoundedCornerShape(22.dp))
                    .padding(horizontal = 36.dp, vertical = 22.dp)
            ) {
                Text("🎉", fontSize = 56.sp)
                Spacer(Modifier.height(10.dp))
                Text(
                    text       = "Task Complete!",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 22.sp,
                    color      = CoffeeDark
                )
            }
        }
    }
}