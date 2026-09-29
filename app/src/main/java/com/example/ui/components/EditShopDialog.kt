package com.example.ui.components

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShopSettings
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary
import com.example.ui.theme.KiranaTextSecondary

@Composable
fun EditShopDialog(
    currentSettings: ShopSettings,
    onDismiss: () -> Unit,
    onSave: (shopName: String, tagline: String, ownerName: String, phone: String, address: String, upiId: String) -> Unit
) {
    var shopName by remember { mutableStateOf(currentSettings.shopName) }
    var tagline by remember { mutableStateOf(currentSettings.tagline) }
    var ownerName by remember { mutableStateOf(currentSettings.ownerName) }
    var phone by remember { mutableStateOf(currentSettings.phone) }
    var address by remember { mutableStateOf(currentSettings.address) }
    var upiId by remember { mutableStateOf(currentSettings.upiId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = KiranaGreenPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🏪 दुकान की जानकारी सेट करें",
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
                Text(
                    text = "दुकान का नाम (हर दुकानदार अपना नाम रख सकता है):",
                    fontSize = 12.sp,
                    color = KiranaTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = shopName,
                    onValueChange = { shopName = it },
                    placeholder = { Text("जैसे: श्री गणेश किराणा स्टोर") },
                    leadingIcon = {
                        Icon(Icons.Default.Store, contentDescription = null, tint = KiranaGreenPrimary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "दुकान की टैगलाइन / विशेषता:",
                    fontSize = 12.sp,
                    color = KiranaTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = tagline,
                    onValueChange = { tagline = it },
                    placeholder = { Text("हाई डिस्काउंट मार्ट · शुद्ध व ताजा किराना") },
                    leadingIcon = {
                        Icon(Icons.Default.Subtitles, contentDescription = null, tint = KiranaGreenPrimary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "दुकानदार / संचालक का नाम:",
                    fontSize = 12.sp,
                    color = KiranaTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    placeholder = { Text("नाम") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = KiranaGreenPrimary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "दुकान का फोन नंबर (WhatsApp):",
                    fontSize = 12.sp,
                    color = KiranaTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("+91 98765 43210") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Icon(Icons.Default.Call, contentDescription = null, tint = KiranaGreenPrimary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "दुकान का पता:",
                    fontSize = 12.sp,
                    color = KiranaTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text("दुकान नं., बाजार, शहर") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = KiranaGreenPrimary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "UPI ID (पेमेंट के लिए):",
                    fontSize = 12.sp,
                    color = KiranaTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    placeholder = { Text("kirana@upi") },
                    leadingIcon = {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = KiranaGreenPrimary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(shopName, tagline, ownerName, phone, address, upiId)
                },
                colors = ButtonDefaults.buttonColors(containerColor = KiranaGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("save_shop_btn")
            ) {
                Text("दुकान का नाम सेव करें", fontWeight = FontWeight.Bold)
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
