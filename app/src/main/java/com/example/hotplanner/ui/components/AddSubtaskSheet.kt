package com.example.hotplanner.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.data.model.SubTask
import com.example.hotplanner.ui.theme.LocalAppColors
import com.example.hotplanner.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubtaskSheet(
    existingSubTasks: List<SubTask>,
    defaultAfterIndex: Int,
    onAdd: (text: String, afterIndex: Int, hasNotification: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val sheetState   = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var text         by remember { mutableStateOf("") }
    var afterIndex   by remember { mutableStateOf(defaultAfterIndex) }
    var notification by remember { mutableStateOf(false) }
    val ok           = text.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState,
        containerColor   = CreamCard,
        shape            = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 40.dp)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "Add Subtask", fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = CoffeeDark
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Latte.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕", fontSize = 14.sp, color = Mocha, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(18.dp))

            // ── Text field ────────────────────────────────────────────────────
            OutlinedTextField(
                value         = text,
                onValueChange = { text = it },
                placeholder   = {
                    Text("Describe this step...", fontFamily = Poppins, color = Mocha)
                },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(14.dp),
                singleLine    = true,
                textStyle     = LocalTextStyle.current.copy(fontFamily = Poppins, fontSize = 15.sp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = CoffeeBrown,
                    unfocusedBorderColor    = BorderColor,
                    focusedContainerColor   = CreamBg,
                    unfocusedContainerColor = CreamBg,
                    cursorColor             = CoffeeBrown,
                    focusedTextColor        = CoffeeDark,
                    unfocusedTextColor      = CoffeeDark
                )
            )

            // ── Position dropdown (only if subtasks already exist) ────────────
            if (existingSubTasks.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                Text(
                    "Insert position", fontFamily = Poppins,
                    fontSize = 12.sp, color = Mocha, fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(6.dp))

                var posExpanded by remember { mutableStateOf(false) }
                val posLabel = when {
                    afterIndex < 0 -> "At the beginning"
                    afterIndex >= existingSubTasks.lastIndex -> "At the end"
                    else -> {
                        val t = existingSubTasks.getOrNull(afterIndex)?.text ?: ""
                        "After: ${if (t.length > 26) t.take(26) + "…" else t}"
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick  = { posExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        border   = BorderStroke(1.5.dp, BorderColor),
                        colors   = ButtonDefaults.outlinedButtonColors(containerColor = CreamBg)
                    ) {
                        Text(posLabel, fontFamily = Poppins, color = CoffeeDark, fontSize = 13.sp)
                    }
                    DropdownMenu(
                        expanded         = posExpanded,
                        onDismissRequest = { posExpanded = false },
                        modifier         = Modifier.background(CreamCard)
                    ) {
                        DropdownMenuItem(
                            text    = { Text("At the beginning", fontFamily = Poppins, color = CoffeeDark) },
                            onClick = { afterIndex = -1; posExpanded = false }
                        )
                        existingSubTasks.forEachIndexed { i, sub ->
                            val label = if (sub.text.length > 26) sub.text.take(26) + "…" else sub.text
                            DropdownMenuItem(
                                text    = { Text("After: $label", fontFamily = Poppins, color = CoffeeDark) },
                                onClick = { afterIndex = i; posExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // ── Notification toggle ───────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Set Reminder", fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CoffeeDark
                    )
                    Text(
                        "Get notified about this step",
                        fontFamily = Poppins, fontSize = 12.sp, color = Mocha
                    )
                }
                Switch(
                    checked         = notification,
                    onCheckedChange = { notification = it },
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor   = Color.White,
                        checkedTrackColor   = CoffeeBrown,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = BorderColor
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Action buttons ────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    border   = BorderStroke(1.5.dp, BorderColor)
                ) {
                    Text("Cancel", fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold, color = CoffeeBrown)
                }
                Button(
                    onClick  = { if (ok) onAdd(text.trim(), afterIndex, notification) },
                    enabled  = ok,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = CoffeeBrown,
                        disabledContainerColor = BorderColor
                    )
                ) {
                    Text("Add Subtask", fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}