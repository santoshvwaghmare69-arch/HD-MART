package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.data.model.ShopSettings
import com.example.ui.theme.KiranaBorder
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary
import com.example.ui.theme.KiranaMint
import com.example.ui.theme.KiranaSurfaceVariant
import com.example.ui.theme.KiranaTextMuted
import com.example.ui.theme.KiranaTextPrimary
import com.example.ui.theme.KiranaTextSecondary

@Composable
fun OrderSuccessDialog(
    order: Order,
    shopSettings: ShopSettings,
    onDismiss: () -> Unit,
    onShareWhatsApp: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(KiranaMint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "सफल",
                        tint = KiranaGreenPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "ऑर्डर प्राप्त हुआ!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenDark
                )
                Text(
                    text = "ऑर्डर सं: #${order.orderNumber}",
                    fontSize = 13.sp,
                    color = KiranaGreenPrimary,
                    fontWeight = FontWeight.SemiBold
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
                // Receipt Box
                Surface(
                    color = KiranaSurfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, KiranaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = shopSettings.shopName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaGreenDark
                        )
                        Text(
                            text = "ग्राहक: ${order.customerName} (${order.customerPhone})",
                            fontSize = 12.sp,
                            color = KiranaTextSecondary
                        )
                        Text(
                            text = "प्रकार: ${if (order.deliveryType == "HOME_DELIVERY") "🚚 होम डिलीवरी" else "🏪 दुकान से पिकअप"}",
                            fontSize = 12.sp,
                            color = KiranaTextSecondary
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = KiranaBorder
                        )

                        Text(
                            text = "सामान विवरण:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaTextPrimary
                        )

                        Text(
                            text = order.itemsSummary,
                            fontSize = 12.sp,
                            color = KiranaTextPrimary,
                            lineHeight = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = KiranaBorder
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "सामान का कुल मूल्य:", fontSize = 12.sp, color = KiranaTextSecondary)
                            Text(text = "₹${order.subtotal}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        if (order.discount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "कुल छूट / बचत:", fontSize = 12.sp, color = KiranaGreenPrimary)
                                Text(text = "-₹${order.discount}", fontSize = 12.sp, color = KiranaGreenPrimary, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "डिलीवरी शुल्क:", fontSize = 12.sp, color = KiranaTextSecondary)
                            Text(
                                text = if (order.deliveryFee == 0) "मुफ्त" else "₹${order.deliveryFee}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "कुल देय राशि:", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = KiranaGreenDark)
                            Text(text = "₹${order.totalAmount}", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = KiranaGreenDark)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onShareWhatsApp,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // WhatsApp Green
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("share_whatsapp_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WhatsApp पर पर्ची भेजें",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("बंद करें")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
