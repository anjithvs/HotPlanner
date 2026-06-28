package com.example.hotplanner.ui.screens.completed

import androidx.compose.foundation.background
import com.example.hotplanner.ui.theme.LocalAppColors
import com.example.hotplanner.ui.utils.HapticType
import com.example.hotplanner.ui.utils.rememberAppHaptic
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.data.model.TaskWithSubTasks
import com.example.hotplanner.ui.theme.*
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun CompletedScreen(
    viewModel: TaskViewModel,
    padding: PaddingValues
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val completedTasks by viewModel.completedTasks.collectAsState()
    var deleteTarget   by remember { mutableStateOf<TaskWithSubTasks?>(null) }
    val hapticsEnabled by viewModel.haptics.collectAsState()
    val haptic         = rememberAppHaptic(hapticsEnabled)

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CreamCard)
                    .padding(horizontal = 22.dp, vertical = 18.dp)
            ) {
                Text(
                    "Completed ✅",
                    fontFamily = Poppins, fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp, color = CoffeeDark
                )
                Text(
                    "${completedTasks.size} completed task${if (completedTasks.size != 1) "s" else ""}",
                    fontFamily = Poppins, fontSize = 13.sp, color = Mocha
                )
            }

            // ── List or Empty State ───────────────────────────────────────────
            if (completedTasks.isEmpty()) {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", fontSize = 56.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Nothing here yet",
                            fontFamily = Poppins, fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp, color = CoffeeDark
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Completed tasks will appear here",
                            fontFamily = Poppins, fontSize = 14.sp, color = Mocha
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp,
                        top   = 12.dp, bottom = 40.dp
                    )
                ) {
                    items(completedTasks, key = { it.task.id }) { task ->
                        CompletedTaskCard(
                            taskWithSubTasks = task,
                            modifier         = Modifier.animateItem(),
                            onRestore        = { viewModel.restoreTask(task) },
                            onDelete         = { deleteTarget = task },
                            haptic           = haptic
                        )
                    }
                }
            }
        }
    }

    // ── Delete Confirmation ───────────────────────────────────────────────────
    deleteTarget?.let { task ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            containerColor   = CreamCard,
            shape            = RoundedCornerShape(22.dp),
            title = {
                Text(
                    "Delete Task?", fontFamily = Poppins,
                    fontWeight = FontWeight.Bold, color = CoffeeDark
                )
            },
            text = {
                Text(
                    "\"${task.task.title}\" will be permanently deleted and cannot be recovered.",
                    fontFamily = Poppins, color = Mocha, fontSize = 14.sp, lineHeight = 22.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteTask(task); deleteTarget = null },
                    shape   = RoundedCornerShape(12.dp),
                    colors  = ButtonDefaults.buttonColors(containerColor = PriorityHigh)
                ) {
                    Text("Delete", fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { deleteTarget = null },
                    shape   = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Cancel", fontFamily = Poppins,
                        color = CoffeeBrown, fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }
}

// ── Completed Task Card ────────────────────────────────────────────────────────

@Composable
private fun CompletedTaskCard(
    taskWithSubTasks: TaskWithSubTasks,
    modifier: Modifier = Modifier,
    onRestore: () -> Unit,
    onDelete: () -> Unit,
    haptic: (HapticType) -> Unit
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val task = taskWithSubTasks.task

    Column(modifier = modifier.padding(bottom = 14.dp)) {

        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = CreamCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // Green completion strip
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                        .background(
                            Color(0xFF27AE60),
                            RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                        )
                )

                Column(
                    modifier = Modifier.padding(
                        start = 14.dp, end = 16.dp,
                        top   = 16.dp, bottom = 14.dp
                    )
                ) {
                    // Title + tick
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.Top
                    ) {
                        Text(
                            text           = task.title,
                            fontFamily     = Poppins,
                            fontWeight     = FontWeight.Bold,
                            fontSize       = 16.sp,
                            color          = Mocha,
                            textDecoration = TextDecoration.LineThrough,
                            modifier       = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        Text("✅", fontSize = 18.sp)
                    }

                    // Badges
                    Row(
                        modifier              = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        CompletedBadge(
                            text      = task.category,
                            bgColor   = Latte.copy(alpha = 0.3f),
                            textColor = CoffeeBrown
                        )
                        CompletedBadge(
                            text      = "Completed",
                            bgColor   = Color(0xFF27AE60).copy(alpha = 0.14f),
                            textColor = Color(0xFF27AE60)
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${taskWithSubTasks.subTasks.size} subtasks · All done",
                        fontFamily = Poppins, fontSize = 12.sp, color = Mocha
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { haptic(HapticType.CONFIRM); onRestore() },
                modifier       = Modifier.weight(1f),
                shape          = RoundedCornerShape(12.dp),
                colors         = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    "↩  Restore to Active",
                    fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 12.sp
                )
            }
            Button(
                onClick = { haptic(HapticType.WARNING); onDelete() },
                modifier       = Modifier.width(58.dp),
                shape          = RoundedCornerShape(12.dp),
                colors         = ButtonDefaults.buttonColors(containerColor = PriorityHigh),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text("✕", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun CompletedBadge(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text, fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = textColor
        )
    }
}