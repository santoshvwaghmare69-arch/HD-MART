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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItemWithProduct
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

@Composable
fun CartScreen(
    cartItems: List<CartItemWithProduct>,
    shopSettings: ShopSettings,
    onUpdateQuantity: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onClearCart: () -> Unit,
    onPlaceOrder: (customerName: String, customerPhone: String, customerAddress: String, deliveryType: String) -> Unit,
    onExploreProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (cartItems.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🛒", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "आपकी टोकरी खाली है",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = KiranaGreenDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "चावल, तेल, आटा, दाल व अन्य किराना सामान पर बेहतरीन छूट का लाभ उठाएं।",
                fontSize = 13.sp,
                color = KiranaTextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onExploreProducts,
                colors = ButtonDefaults.buttonColors(containerColor = KiranaGreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "किराना सामान देखें", fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    var deliveryType by remember { mutableStateOf("HOME_DELIVERY") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerAddress by remember { mutableStateOf("") }

    val subtotal = remember(cartItems) {
        cartItems.sumOf { it.product.price * it.cartItem.quantity }
    }
    val originalTotal = remember(cartItems) {
        cartItems.sumOf { it.product.oldPrice * it.cartItem.quantity }
    }
    val totalDiscount = remember(subtotal, originalTotal) {
        if (originalTotal > subtotal) originalTotal - subtotal else 0
    }
    val isFreeDelivery = deliveryType == "STORE_PICKUP" || subtotal >= shopSettings.freeDeliveryAbove
    val deliveryFee = if (isFreeDelivery) 0 else shopSettings.deliveryFee
    val finalTotal = subtotal + deliveryFee

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "आपकी टोकरी (${cartItems.size} सामान)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenDark
                )
                Text(
                    text = "${shopSettings.shopName} से खरीदारी",
                    fontSize = 12.sp,
                    color = KiranaTextSecondary
                )
            }

            IconButton(
                onClick = onClearCart,
                modifier = Modifier.testTag("clear_cart_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "खाली करें",
                    tint = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Cart Items List Cards
        cartItems.forEach { item ->
            val product = item.product
            val qty = item.cartItem.quantity
            val itemTotal = product.price * qty

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = KiranaSurface),
                border = BorderStroke(1.dp, KiranaBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = KiranaSurfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = product.emoji, fontSize = 28.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.nameHindi,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaTextPrimary
                        )
                        Text(
                            text = "${product.unit} • ₹${product.price}",
                            fontSize = 12.sp,
                            color = KiranaTextSecondary
                        )
                        Text(
                            text = "कुल: ₹$itemTotal",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KiranaGreenDark
                        )
                    }

                    // Stepper
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = KiranaMint,
                        border = BorderStroke(1.dp, KiranaGreenPrimary)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onUpdateQuantity(product.id, -1) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "कम करें",
                                    tint = KiranaGreenPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "$qty",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = KiranaGreenDark,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            IconButton(
                                onClick = { onUpdateQuantity(product.id, 1) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "बढ़ाएं",
                                    tint = KiranaGreenPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delivery Type Selection
        Text(
            text = "डिलीवरी का माध्यम चुनें:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = KiranaGreenDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Home Delivery Option
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { deliveryType = "HOME_DELIVERY" }
                    .testTag("delivery_home_btn"),
                shape = RoundedCornerShape(12.dp),
                color = if (deliveryType == "HOME_DELIVERY") KiranaMint else KiranaSurface,
                border = BorderStroke(
                    if (deliveryType == "HOME_DELIVERY") 2.dp else 1.dp,
                    if (deliveryType == "HOME_DELIVERY") KiranaGreenPrimary else KiranaBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = if (deliveryType == "HOME_DELIVERY") KiranaGreenPrimary else KiranaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "होम डिलीवरी",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = KiranaTextPrimary
                    )
                    Text(
                        text = if (subtotal >= shopSettings.freeDeliveryAbove) "मुफ्त डिलीवरी" else "₹${shopSettings.deliveryFee} शुल्क",
                        fontSize = 10.sp,
                        color = if (subtotal >= shopSettings.freeDeliveryAbove) KiranaGreenPrimary else KiranaTextSecondary
                    )
                }
            }

            // Store Pickup Option
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { deliveryType = "STORE_PICKUP" }
                    .testTag("delivery_pickup_btn"),
                shape = RoundedCornerShape(12.dp),
                color = if (deliveryType == "STORE_PICKUP") KiranaMint else KiranaSurface,
                border = BorderStroke(
                    if (deliveryType == "STORE_PICKUP") 2.dp else 1.dp,
                    if (deliveryType == "STORE_PICKUP") KiranaGreenPrimary else KiranaBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = if (deliveryType == "STORE_PICKUP") KiranaGreenPrimary else KiranaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "दुकान से उठाएं",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = KiranaTextPrimary
                    )
                    Text(
                        text = "तुरंत तैयार (₹0 शुल्क)",
                        fontSize = 10.sp,
                        color = KiranaGreenPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Customer Details Form
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KiranaSurface),
            border = BorderStroke(1.dp, KiranaBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ग्राहक विवरण (बिलिंग के लिए):",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    placeholder = { Text("आपका नाम (उदा: रमेश शर्मा)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = KiranaGreenPrimary) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("customer_name_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    placeholder = { Text("मोबाइल नंबर (उदा: 9876543210)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = { Icon(Icons.Default.Call, contentDescription = null, tint = KiranaGreenPrimary) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("customer_phone_input")
                )

                if (deliveryType == "HOME_DELIVERY") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customerAddress,
                        onValueChange = { customerAddress = it },
                        placeholder = { Text("डिलीवरी का पता (मकान नं, गली, मोहल्ला)") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = KiranaGreenPrimary) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("customer_address_input")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bill Summary Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KiranaSurface),
            border = BorderStroke(1.dp, KiranaBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "बिल का विवरण (Bill Summary)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KiranaGreenDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "सामान का कुल मूल्य:", fontSize = 13.sp, color = KiranaTextSecondary)
                    Text(text = "₹$subtotal", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                if (totalDiscount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "आपकी कुल बचत (Discount):", fontSize = 13.sp, color = KiranaGreenPrimary)
                        Text(text = "-₹$totalDiscount", fontSize = 13.sp, color = KiranaGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "डिलीवरी शुल्क:", fontSize = 13.sp, color = KiranaTextSecondary)
                    Text(
                        text = if (deliveryFee == 0) "मुफ्त (FREE)" else "₹$deliveryFee",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (deliveryFee == 0) KiranaGreenPrimary else KiranaTextPrimary
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = KiranaBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "कुल भुगतान राशि:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaGreenDark
                        )
                        Text(
                            text = "नकद / UPI द्वारा देय",
                            fontSize = 11.sp,
                            color = KiranaTextSecondary
                        )
                    }
                    Text(
                        text = "₹$finalTotal",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = KiranaGreenDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Confirm Order Button
        Button(
            onClick = {
                onPlaceOrder(
                    customerName.ifBlank { "ग्राहक" },
                    customerPhone.ifBlank { "9876543210" },
                    customerAddress.ifBlank { "घर का पता" },
                    deliveryType
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = KiranaGreenPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("confirm_order_btn")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ऑर्डर कन्फर्म करें • ₹$finalTotal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "ऑर्डर कन्फर्म करने के बाद आपको ऑर्डर पर्ची मिलेगी जिसे आप सीधे दुकानदार को WhatsApp पर भेज सकते हैं।",
            fontSize = 11.sp,
            color = KiranaTextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
