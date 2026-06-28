package com.example.hotplanner.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.ui.theme.LocalAppColors
import com.example.hotplanner.ui.utils.HapticType
import com.example.hotplanner.ui.utils.rememberAppHaptic
import com.example.hotplanner.data.model.SubTask
import com.example.hotplanner.data.model.TaskWithSubTasks
import com.example.hotplanner.ui.components.AddSubtaskSheet
import com.example.hotplanner.ui.theme.*
import com.example.hotplanner.ui.utils.*
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun TaskDetailScreen(
    taskId: Long,
    viewModel: TaskViewModel,
    onBack: () -> Unit
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val activeTasks      by viewModel.activeTasks.collectAsState()
    val taskWithSubTasks = remember(activeTasks, taskId) {
        activeTasks.find { it.task.id == taskId }
    }

    // Auto-navigate back when the task completes and leaves active list
    LaunchedEffect(taskWithSubTasks, activeTasks.size) {
        if (taskWithSubTasks == null && activeTasks.isNotEmpty()) onBack()
    }

    // Show spinner briefly while Room delivers first emission
    if (taskWithSubTasks == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CoffeeBrown, strokeWidth = 3.dp)
        }
        return
    }

    val sortedSubs          = taskWithSubTasks.subTasks.sortedBy { it.orderIndex }
    var insertAfterIndex    by remember { mutableStateOf<Int?>(null) }
    val hapticsEnabled      by viewModel.haptics.collectAsState()
    val haptic              = rememberAppHaptic(hapticsEnabled)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        DetailHeader(taskWithSubTasks = taskWithSubTasks, onBack = onBack)

        // ── Subtask list ──────────────────────────────────────────────────────
        LazyColumn(
            modifier       = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start  = 16.dp, end    = 16.dp,
                top    = 14.dp, bottom = 40.dp
            )
        ) {
            // Empty state
            if (sortedSubs.isEmpty()) {
                item {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📋", fontSize = 44.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No subtasks yet", fontFamily = Poppins,
                                fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CoffeeDark
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Add your first step below",
                                fontFamily = Poppins, fontSize = 13.sp, color = Mocha
                            )
                        }
                    }
                }
            }

            // Subtask items + insert buttons
            itemsIndexed(sortedSubs, key = { _, sub -> sub.id }) { index, subTask ->
                Column(modifier = Modifier.animateItem()) {
                    SubtaskRow(
                        subTask = subTask,
                        index   = index,
                        haptic   = haptic,
                        onToggle = {
                            viewModel.toggleSubTask(taskWithSubTasks, subTask)
                        }
                    )
                    // "Insert subtask here" button between items
                    InsertHereButton(onClick = { insertAfterIndex = index })
                }
            }

            // Add New Subtask button
            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick        = { insertAfterIndex = sortedSubs.lastIndex },
                    modifier       = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    shape          = RoundedCornerShape(16.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                    contentPadding = PaddingValues(vertical = 15.dp)
                ) {
                    Text(
                        "+  Add New Subtask", fontFamily = Poppins,
                        fontWeight = FontWeight.Bold, fontSize = 14.sp
                    )
                }
            }
        }
    }

    // ── Add Subtask Sheet ─────────────────────────────────────────────────────
    insertAfterIndex?.let { afterIdx ->
        AddSubtaskSheet(
            existingSubTasks  = sortedSubs,
            defaultAfterIndex = afterIdx,
            onAdd             = { text, afterIndex, hasNotif ->
                viewModel.addSubTask(
                    taskId          = taskWithSubTasks.task.id,
                    text            = text,
                    afterIndex      = afterIndex,
                    hasNotification = hasNotif,
                    currentSubTasks = sortedSubs
                )
                insertAfterIndex = null
            },
            onDismiss = { insertAfterIndex = null }
        )
    }
}

// ── Detail Header ──────────────────────────────────────────────────────────────

@Composable
private fun DetailHeader(taskWithSubTasks: TaskWithSubTasks, onBack: () -> Unit) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val task   = taskWithSubTasks.task
    val pColor = priorityColor(task.priority)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CreamCard)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        // Back button
        Row(
            modifier          = Modifier
                .clickable(onClick = onBack)
                .padding(bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("←", fontSize = 20.sp, color = CoffeeBrown, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Text(
                "My Tasks", fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CoffeeBrown
            )
        }

        // Title + progress badge
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.Top
        ) {
            Text(
                text       = task.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 22.sp,
                color      = CoffeeDark,
                lineHeight = 30.sp,
                modifier   = Modifier.weight(1f).padding(end = 12.dp)
            )
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(CoffeeBrown, CoffeeDeep)),
                        shape = RoundedCornerShape(17.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${taskWithSubTasks.progressPercent}%",
                        fontFamily = Poppins, fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp, color = Color.White, lineHeight = 18.sp
                    )
                    Text(
                        "done", fontFamily = Poppins,
                        fontSize = 9.sp, color = Latte
                    )
                }
            }
        }

        // Badges
        Row(
            modifier              = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            DetailBadge(task.category, Latte.copy(alpha = 0.3f), CoffeeBrown)
            DetailBadge(priorityLabel(task.priority), pColor.copy(alpha = 0.15f), pColor)
            if (task.dueDate.isNotEmpty()) {
                DetailBadge("📅 ${formatDueDate(task.dueDate)}", Latte.copy(alpha = 0.3f), CoffeeBrown)
            }
        }

        // Progress bar
        Spacer(Modifier.height(14.dp))
        LinearProgressIndicator(
            progress   = { taskWithSubTasks.progress },
            modifier   = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color      = CoffeeBrown,
            trackColor = BorderColor
        )
        Spacer(Modifier.height(7.dp))
        Text(
            "${taskWithSubTasks.subTasks.count { it.isDone }} of ${taskWithSubTasks.subTasks.size} steps completed",
            fontFamily = Poppins, fontSize = 12.sp, color = Mocha
        )
    }
}

// ── Subtask Row ────────────────────────────────────────────────────────────────

@Composable
private fun SubtaskRow(
    subTask: SubTask,
    index: Int,
    haptic: (HapticType) -> Unit,
    onToggle: () -> Unit
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (subTask.isDone)
                Color(0xFF27AE60).copy(alpha = 0.07f) else CreamCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Step number / done bubble
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (subTask.isDone) Color(0xFF27AE60)
                        else Latte.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(9.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (subTask.isDone) {
                    Text("✓", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
                } else {
                    Text(
                        "${index + 1}", fontSize = 12.sp,
                        color = Mocha, fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Text + notification indicator
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text           = subTask.text,
                    fontFamily     = Poppins,
                    fontWeight     = FontWeight.Medium,
                    fontSize       = 14.sp,
                    color          = if (subTask.isDone) Mocha else CoffeeDark,
                    textDecoration = if (subTask.isDone) TextDecoration.LineThrough
                    else TextDecoration.None,
                    lineHeight     = 20.sp
                )
                if (subTask.notification) {
                    Text(
                        "🔔 Reminder set",
                        fontFamily = Poppins, fontSize = 11.sp, color = Mocha,
                        modifier   = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Checkbox
            Checkbox(
                checked         = subTask.isDone,
                onCheckedChange = { haptic(HapticType.TICK)
                    onToggle() },
                colors          = CheckboxDefaults.colors(
                    checkedColor   = Color(0xFF27AE60),
                    uncheckedColor = Latte,
                    checkmarkColor = Color.White
                )
            )
        }
    }
}

// ── Insert Here Button ─────────────────────────────────────────────────────────

@Composable
private fun InsertHereButton(onClick: () -> Unit) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 5.dp, horizontal = 8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier         = Modifier
                .size(20.dp)
                .background(Latte.copy(alpha = 0.28f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("+", fontSize = 13.sp, color = Mocha, fontWeight = FontWeight.Bold,
                modifier = Modifier.offset(y = (-1).dp))
        }
        Spacer(Modifier.width(8.dp))
        Text(
            "Insert subtask here",
            fontFamily = Poppins, fontSize = 11.sp,
            color      = Mocha.copy(alpha = 0.8f), fontWeight = FontWeight.Medium
        )
    }
}

// ── Badges ─────────────────────────────────────────────────────────────────────

@Composable
private fun DetailBadge(text: String, bgColor: Color, textColor: Color) {
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