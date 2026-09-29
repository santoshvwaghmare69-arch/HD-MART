package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.data.model.ShopSettings
import com.example.ui.theme.KiranaBorder
import com.example.ui.theme.KiranaGold
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary
import com.example.ui.theme.KiranaMint
import com.example.ui.theme.KiranaSurface
import com.example.ui.theme.KiranaSurfaceVariant
import com.example.ui.theme.KiranaTextMuted
import com.example.ui.theme.KiranaTextPrimary
import com.example.ui.theme.KiranaTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShopSettingsScreen(
    shopSettings: ShopSettings,
    orders: List<Order>,
    onEditShopClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onShareOrderWhatsApp: (Order) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Shop Profile Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = KiranaSurface),
            border = BorderStroke(1.dp, KiranaBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = KiranaMint,
                            shape = CircleShape,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = KiranaGreenPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = shopSettings.shopName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = KiranaGreenDark
                            )
                            Text(
                                text = shopSettings.tagline,
                                fontSize = 12.sp,
                                color = KiranaTextSecondary
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onEditShopClick,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("edit_shop_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "बदलें", fontSize = 12.sp)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = KiranaBorder)

                // Details List
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = KiranaGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "संपर्क: ${shopSettings.phone}", fontSize = 13.sp, color = KiranaTextPrimary)
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = KiranaGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "पता: ${shopSettings.address}", fontSize = 13.sp, color = KiranaTextPrimary)
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = KiranaGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "UPI ID: ${shopSettings.upiId}", fontSize = 13.sp, color = KiranaTextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Shopkeeper Action: Add New Product
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KiranaMint),
            border = BorderStroke(1.dp, KiranaGreenPrimary.copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAddProductClick() }
                .testTag("add_product_card_btn")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = KiranaGreenPrimary,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AddShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "नया किराना सामान जोड़ें",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaGreenDark
                        )
                        Text(
                            text = "दुकानदार अपना सामान व कीमत लिस्ट करें",
                            fontSize = 12.sp,
                            color = KiranaTextSecondary
                        )
                    }
                }

                Text(
                    text = "＋ जोड़ें",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Past Orders Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = KiranaGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "पिछले ऑर्डर व पर्चियां (${orders.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenDark
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (orders.isEmpty()) {
            Surface(
                color = KiranaSurfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🧾", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "अभी तक कोई ऑर्डर नहीं हुआ है",
                        fontSize = 14.sp,
                        color = KiranaTextSecondary
                    )
                }
            }
        } else {
            val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            orders.forEach { order ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = KiranaSurface),
                    border = BorderStroke(1.dp, KiranaBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ऑर्डर #${order.orderNumber}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = KiranaGreenDark
                            )

                            Surface(
                                color = KiranaMint,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "प्राप्त हुआ ✅",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KiranaGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = dateFormat.format(Date(order.timestamp)),
                            fontSize = 11.sp,
                            color = KiranaTextMuted
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "ग्राहक: ${order.customerName} (${order.customerPhone})",
                            fontSize = 12.sp,
                            color = KiranaTextPrimary
                        )

                        Text(
                            text = order.itemsSummary,
                            fontSize = 11.sp,
                            color = KiranaTextSecondary,
                            maxLines = 2
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = KiranaBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "कुल: ₹${order.totalAmount}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = KiranaGreenDark
                            )

                            Button(
                                onClick = { onShareOrderWhatsApp(order) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "WhatsApp पर्ची", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Information Note from the prototype
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = KiranaSurfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ℹ️ HD MART मॉडल",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "यह ऐप HD MART प्लेटफॉर्म के तहत प्रत्येक किराना स्टोर को अपना डिजिटल मार्ट बनाने की सुविधा देता है। दुकान का नाम हर खरीदार/दुकानदार अपनी पसंद के अनुसार बदल सकता है।",
                    fontSize = 11.sp,
                    color = KiranaTextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
