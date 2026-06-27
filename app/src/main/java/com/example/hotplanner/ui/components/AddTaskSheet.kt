package com.example.hotplanner.ui.components

import android.app.DatePickerDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.ui.theme.*
import com.example.hotplanner.ui.utils.formatDueDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskSheet(
    onAdd: (title: String, category: String, priority: String,
            dueDate: String, notification: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title        by remember { mutableStateOf("") }
    var category     by remember { mutableStateOf("Personal") }
    var priority     by remember { mutableStateOf("MEDIUM") }
    var dueDate      by remember { mutableStateOf("") }
    var notification by remember { mutableStateOf(false) }

    val context  = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                dueDate = "$year-${(month + 1).toString().padStart(2,'0')}-${day.toString().padStart(2,'0')}"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    ModalBottomSheet(
        onDismissRequest  = onDismiss,
        sheetState        = sheetState,
        containerColor    = CreamCard,
        shape             = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 40.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("New Task", fontFamily = Poppins, fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp, color = CoffeeDark)
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

            // Title field
            OutlinedTextField(
                value            = title,
                onValueChange    = { title = it },
                placeholder      = {
                    Text("What needs to be done?", fontFamily = Poppins, color = Mocha)
                },
                modifier         = Modifier.fillMaxWidth(),
                shape            = RoundedCornerShape(14.dp),
                singleLine       = true,
                textStyle        = LocalTextStyle.current.copy(fontFamily = Poppins, fontSize = 15.sp),
                colors           = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor    = CoffeeBrown,
                    unfocusedBorderColor  = BorderColor,
                    focusedContainerColor = CreamBg,
                    unfocusedContainerColor = CreamBg,
                    cursorColor           = CoffeeBrown,
                    focusedTextColor      = CoffeeDark,
                    unfocusedTextColor    = CoffeeDark
                )
            )

            Spacer(Modifier.height(12.dp))

            // Category + Priority row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Category dropdown
                var catExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick  = { catExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        border   = BorderStroke(1.5.dp, BorderColor),
                        colors   = ButtonDefaults.outlinedButtonColors(containerColor = CreamBg)
                    ) {
                        Text(category, fontFamily = Poppins, color = CoffeeDark, fontSize = 13.sp)
                    }
                    DropdownMenu(
                        expanded          = catExpanded,
                        onDismissRequest  = { catExpanded = false },
                        modifier          = Modifier.background(CreamCard)
                    ) {
                        listOf("Personal","Work","Finance","Health","Shopping","Other")
                            .forEach { cat ->
                                DropdownMenuItem(
                                    text    = { Text(cat, fontFamily = Poppins, color = CoffeeDark) },
                                    onClick = { category = cat; catExpanded = false }
                                )
                            }
                    }
                }

                // Priority dropdown
                var priExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    val priEmoji = when (priority) { "HIGH" -> "🔴"; "MEDIUM" -> "🟡"; else -> "🟢" }
                    val priName  = priority.lowercase().replaceFirstChar { it.uppercase() }
                    OutlinedButton(
                        onClick  = { priExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        border   = BorderStroke(1.5.dp, BorderColor),
                        colors   = ButtonDefaults.outlinedButtonColors(containerColor = CreamBg)
                    ) {
                        Text("$priEmoji $priName", fontFamily = Poppins, color = CoffeeDark, fontSize = 13.sp)
                    }
                    DropdownMenu(
                        expanded          = priExpanded,
                        onDismissRequest  = { priExpanded = false },
                        modifier          = Modifier.background(CreamCard)
                    ) {
                        listOf("HIGH" to "🔴 High", "MEDIUM" to "🟡 Medium", "LOW" to "🟢 Low")
                            .forEach { (value, label) ->
                                DropdownMenuItem(
                                    text    = { Text(label, fontFamily = Poppins, color = CoffeeDark) },
                                    onClick = { priority = value; priExpanded = false }
                                )
                            }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Due date button
            OutlinedButton(
                onClick  = { datePicker.show() },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(12.dp),
                border   = BorderStroke(1.5.dp, BorderColor),
                colors   = ButtonDefaults.outlinedButtonColors(containerColor = CreamBg)
            ) {
                Text(
                    text  = if (dueDate.isEmpty()) "📅  Set due date (optional)"
                    else "📅  ${formatDueDate(dueDate)}",
                    fontFamily = Poppins,
                    color      = if (dueDate.isEmpty()) Mocha else CoffeeDark,
                    fontSize   = 14.sp
                )
            }

            Spacer(Modifier.height(18.dp))

            // Notification toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Set Reminder", fontFamily = Poppins, fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp, color = CoffeeDark)
                    Text("Get notified about this task", fontFamily = Poppins,
                        fontSize = 12.sp, color = Mocha)
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

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    border   = BorderStroke(1.5.dp, BorderColor)
                ) {
                    Text("Cancel", fontFamily = Poppins, fontWeight = FontWeight.SemiBold,
                        color = CoffeeBrown)
                }
                Button(
                    onClick  = {
                        if (title.isNotBlank()) {
                            onAdd(title.trim(), category, priority, dueDate, notification)
                            onDismiss()
                        }
                    },
                    enabled  = title.isNotBlank(),
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = CoffeeBrown,
                        disabledContainerColor = BorderColor
                    )
                ) {
                    Text("Create Task", fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}