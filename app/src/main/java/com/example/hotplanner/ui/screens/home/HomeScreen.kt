package com.example.hotplanner.ui.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import com.example.hotplanner.ui.theme.LocalAppColors
import com.example.hotplanner.ui.utils.HapticType
import com.example.hotplanner.ui.utils.rememberAppHaptic
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.data.model.TaskWithSubTasks
import com.example.hotplanner.ui.components.AddTaskSheet
import com.example.hotplanner.ui.theme.*
import com.example.hotplanner.ui.utils.*
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    padding: PaddingValues,
    onNavigateToDetail: (Long) -> Unit
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val activeTasks   by viewModel.activeTasks.collectAsState()
    var showAddTask   by remember { mutableStateOf(false) }
    var deleteTarget  by remember { mutableStateOf<TaskWithSubTasks?>(null) }
    val hapticsEnabled by viewModel.haptics.collectAsState()
    val haptic         = rememberAppHaptic(hapticsEnabled)

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Header ────────────────────────────────────────────────────────
            HomeHeader(taskCount = activeTasks.size)

            // ── Task List or Empty State ──────────────────────────────────────
            if (activeTasks.isEmpty()) {
                EmptyState(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier        = Modifier.weight(1f),
                    contentPadding  = PaddingValues(
                        start  = 16.dp, end    = 16.dp,
                        top    = 12.dp, bottom = 100.dp
                    )
                ) {
                    items(activeTasks, key = { it.task.id }) { taskWithSubs ->
                        TaskCard(
                            taskWithSubTasks = taskWithSubs,
                            modifier         = Modifier.animateItem(),
                            onTap            = { onNavigateToDetail(taskWithSubs.task.id) },
                            onComplete       = { viewModel.completeTask(taskWithSubs) },
                            onDelete         = { deleteTarget = taskWithSubs },
                            haptic           = haptic                              // ← ADD THIS
                        )
                    }
                }
            }
        }

        // ── Floating Action Button ────────────────────────────────────────────
        FloatingActionButton(
            onClick        = { showAddTask = true },
            modifier       = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 22.dp),
            containerColor = CoffeeBrown,
            shape          = RoundedCornerShape(50)
        ) {
            Text(
                text       = "+",
                fontSize   = 32.sp,
                color      = Color.White,
                fontWeight = FontWeight.Light,
                modifier   = Modifier.offset(y = (-2).dp)
            )
        }
    }

    // ── Add Task Sheet ────────────────────────────────────────────────────────
    if (showAddTask) {
        AddTaskSheet(
            onAdd = { title, category, priority, dueDate, notification ->
                viewModel.addTask(title, category, priority, dueDate, notification)
            },
            onDismiss = { showAddTask = false }
        )
    }

    // ── Delete Confirmation Dialog ────────────────────────────────────────────
    deleteTarget?.let { task ->
        AlertDialog(
            onDismissRequest  = { deleteTarget = null },
            containerColor    = CreamCard,
            shape             = RoundedCornerShape(22.dp),
            title = {
                Text("Delete Task?", fontFamily = Poppins,
                    fontWeight = FontWeight.Bold, color = CoffeeDark)
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
                    Text("Cancel", fontFamily = Poppins, color = CoffeeBrown,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

// ── Header ─────────────────────────────────────────────────────────────────────

@Composable
private fun HomeHeader(taskCount: Int) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CreamCard)
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {
        Text(
            text       = "My Tasks ☕",
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize   = 26.sp,
            color      = CoffeeDark
        )
        Text(
            text       = "$taskCount active task${if (taskCount != 1) "s" else ""}",
            fontFamily = Poppins,
            fontSize   = 13.sp,
            color      = Mocha
        )
    }
}

// ── Task Card ──────────────────────────────────────────────────────────────────

@Composable
private fun TaskCard(
    taskWithSubTasks: TaskWithSubTasks,
    modifier: Modifier = Modifier,
    onTap: () -> Unit,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    haptic: (HapticType) -> Unit
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val task      = taskWithSubTasks.task
    val doneCount = taskWithSubTasks.subTasks.count { it.isDone }
    val total     = taskWithSubTasks.subTasks.size
    val pColor    = priorityColor(task.priority)

    Column(modifier = modifier.padding(bottom = 14.dp)) {

        // Main card
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .clickable(onClick = onTap),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = CreamCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // Priority colour strip
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                        .background(
                            color = pColor,
                            shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                        )
                )

                Column(
                    modifier = Modifier.padding(
                        start = 14.dp, end = 16.dp,
                        top   = 16.dp, bottom = 14.dp
                    )
                ) {
                    // Title + percentage
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.Top
                    ) {
                        Text(
                            text       = task.title,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                            color      = CoffeeDark,
                            modifier   = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text       = "${taskWithSubTasks.progressPercent}%",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 20.sp,
                            color      = CoffeeBrown
                        )
                    }

                    // Badges row
                    Row(
                        modifier              = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        TaskBadge(
                            text      = task.category,
                            bgColor   = Latte.copy(alpha = 0.3f),
                            textColor = CoffeeBrown
                        )
                        TaskBadge(
                            text      = priorityLabel(task.priority),
                            bgColor   = pColor.copy(alpha = 0.15f),
                            textColor = pColor
                        )
                        if (task.notification) {
                            Text("🔔", fontSize = 13.sp)
                        }
                    }

                    // Progress bar
                    Spacer(Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress    = { taskWithSubTasks.progress },
                        modifier    = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(50)),
                        color       = CoffeeBrown,
                        trackColor  = BorderColor,
                    )

                    // Steps + due date + arrow
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text = buildString {
                                append("$doneCount/$total steps")
                                if (task.dueDate.isNotEmpty())
                                    append(" · 📅 ${formatDueDate(task.dueDate)}")
                            },
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Mocha
                        )
                        Text("›", fontSize = 22.sp, color = Mocha, fontWeight = FontWeight.Light)
                    }
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
                onClick = { haptic(HapticType.CONFIRM); onComplete() },
                modifier         = Modifier.weight(1f),
                shape            = RoundedCornerShape(12.dp),
                colors           = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60)),
                contentPadding   = PaddingValues(vertical = 10.dp)
            ) {
                Text("✓  Complete", fontFamily = Poppins,
                    fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Button(
                onClick = { haptic(HapticType.WARNING); onDelete() },
                modifier         = Modifier.weight(1f),
                shape            = RoundedCornerShape(12.dp),
                colors           = ButtonDefaults.buttonColors(containerColor = PriorityHigh),
                contentPadding   = PaddingValues(vertical = 10.dp)
            ) {
                Text("✕  Delete", fontFamily = Poppins,
                    fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// ── Badge ──────────────────────────────────────────────────────────────────────

@Composable
private fun TaskBadge(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(text, fontFamily = Poppins, fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp, color = textColor)
    }
}

// ── Empty State ────────────────────────────────────────────────────────────────

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    Box(
        modifier          = modifier.fillMaxWidth(),
        contentAlignment  = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎉", fontSize = 56.sp)
            Spacer(Modifier.height(16.dp))
            Text("All caught up!",
                fontFamily = Poppins, fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp, color = CoffeeDark)
            Spacer(Modifier.height(8.dp))
            Text("Tap + to add a new task",
                fontFamily = Poppins, fontSize = 14.sp, color = Mocha)
        }
    }
}