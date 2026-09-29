package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAdd: (
        nameHindi: String,
        nameEnglish: String,
        category: String,
        price: Int,
        oldPrice: Int,
        unit: String,
        emoji: String,
        isOffer: Boolean
    ) -> Unit
) {
    var nameHindi by remember { mutableStateOf("") }
    var nameEnglish by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("किराना") }
    var priceStr by remember { mutableStateOf("") }
    var oldPriceStr by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("1 किलो") }
    var emoji by remember { mutableStateOf("🛒") }
    var isOffer by remember { mutableStateOf(false) }

    var categoryExpanded by remember { mutableStateOf(false) }
    val categories = listOf("अनाज", "तेल व घी", "दालें", "मसाले", "चाय व चीनी", "डेयरी", "स्नैक्स", "किराना", "घरेलू सामान")

    val quickEmojis = listOf("🍚", "🌾", "🫙", "🧂", "🥛", "🫘", "🧈", "☕", "🌶️", "🍪", "🍜", "🧼", "🧅", "🥔", "📦")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = null,
                    tint = KiranaGreenPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "दुकान में नया सामान जोड़ें",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = nameHindi,
                    onValueChange = { nameHindi = it },
                    label = { Text("सामान का नाम (हिंदी)") },
                    placeholder = { Text("जैसे: बासमती चावल") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_product_name_hi")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameEnglish,
                    onValueChange = { nameEnglish = it },
                    label = { Text("सामान का नाम (English - वैकल्पिक)") },
                    placeholder = { Text("e.g. Basmati Rice") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("कैटेगरी") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("बिक्री मूल्य (₹)") },
                        placeholder = { Text("120") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("new_product_price")
                    )

                    OutlinedTextField(
                        value = oldPriceStr,
                        onValueChange = { oldPriceStr = it },
                        label = { Text("MRP (पुरानी कीमत)") },
                        placeholder = { Text("140") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("मात्रा / इकाई") },
                        placeholder = { Text("1 किलो / 500 ग्राम") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = emoji,
                        onValueChange = { emoji = it },
                        label = { Text("आइकन") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = "त्वरित आइकन चुनें:", fontSize = 11.sp, color = KiranaGreenDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickEmojis.take(7).forEach { ic ->
                        Text(
                            text = ic,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .clickable { emoji = ic }
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isOffer = !isOffer }
                ) {
                    Checkbox(
                        checked = isOffer,
                        onCheckedChange = { isOffer = it }
                    )
                    Text(text = "आज के ऑफर में शामिल करें (बचत डील)", fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceStr.toIntOrNull() ?: 0
                    val oldPrice = oldPriceStr.toIntOrNull() ?: price
                    if (nameHindi.isNotBlank() && price > 0) {
                        onAdd(nameHindi, nameEnglish, category, price, oldPrice, unit, emoji, isOffer)
                    }
                },
                enabled = nameHindi.isNotBlank() && (priceStr.toIntOrNull() ?: 0) > 0,
                colors = ButtonDefaults.buttonColors(containerColor = KiranaGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("submit_add_product_btn")
            ) {
                Text("सामान जोड़ें", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("रद्द करें")
            }
        },
        shape = RoundedCornerShape(18.dp)
    )
}
