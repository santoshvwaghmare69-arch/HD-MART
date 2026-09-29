package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Product
import com.example.data.model.ShopSettings
import com.example.ui.components.CategoryChips
import com.example.ui.components.ProductCard
import com.example.ui.theme.KiranaBorder
import com.example.ui.theme.KiranaGold
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary
import com.example.ui.theme.KiranaMint
import com.example.ui.theme.KiranaSurface
import com.example.ui.theme.KiranaTextMuted
import com.example.ui.theme.KiranaTextPrimary
import com.example.ui.theme.KiranaTextSecondary

@Composable
fun HomeScreen(
    products: List<Product>,
    cartItems: List<CartItemWithProduct>,
    shopSettings: ShopSettings,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onAddToCart: (Long) -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onSaveShopName: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var shopNameInput by remember(shopSettings.shopName) { mutableStateOf(shopSettings.shopName) }
    val context = LocalContext.current

    // Lookup drawable for hero banner if exists
    val bannerResId = remember {
        val id = context.resources.getIdentifier("hdmart_banner_1790700976766", "drawable", context.packageName)
        if (id != 0) id else null
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Hero Offer Banner
        item(span = { GridItemSpan(2) }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = KiranaGreenDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner")
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (bannerResId != null) {
                        Image(
                            painter = painterResource(id = bannerResId),
                            contentDescription = "HD Mart Offers",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )
                    }

                    // Overlay with gradient/scrim for text readability
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                if (bannerResId != null)
                                    Color.Black.copy(alpha = 0.55f)
                                else
                                    KiranaGreenDark
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(KiranaGold)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "⚡ महा बचत ऑफर",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = KiranaGreenDark
                                    )
                                }
                                Text(
                                    text = "30% तक छूट",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = "ताजा किराना, सीधे आपकी रसोई में",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "आटा, दाल, चावल व शुद्ध तेल पर भारी डिस्काउंट",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Category Chips
        item(span = { GridItemSpan(2) }) {
            CategoryChips(
                selectedCategory = selectedCategory,
                onCategorySelected = onSelectCategory,
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        }

        // Row Header: "आज के ऑफर" + "बचत करें" pill
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = KiranaGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedCategory == "सभी") "आज के ऑफर व उत्पाद" else "$selectedCategory के उत्पाद",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KiranaTextPrimary
                    )
                }

                Surface(
                    color = KiranaMint,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = KiranaGreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "बचत करें",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaGreenPrimary
                        )
                    }
                }
            }
        }

        // Empty Products State
        if (products.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🔍", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "कोई सामान नहीं मिला। कृपया दूसरा नाम आज़माएँ।",
                        fontSize = 14.sp,
                        color = KiranaTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Product Cards Grid
            items(products, key = { it.id }) { product ->
                val cartItem = cartItems.firstOrNull { it.cartItem.productId == product.id }
                val quantity = cartItem?.cartItem?.quantity ?: 0

                ProductCard(
                    product = product,
                    cartQuantity = quantity,
                    onAddToCart = { onAddToCart(product.id) },
                    onUpdateQuantity = { delta -> onUpdateQuantity(product.id, delta) },
                    onToggleFavorite = { onToggleFavorite(product.id, product.isFavorite) }
                )
            }
        }

        // In-Page Shop Setup Section (matches HTML prototype)
        item(span = { GridItemSpan(2) }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KiranaSurface),
                border = BorderStroke(1.dp, KiranaBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .testTag("inpage_shop_setup_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏪", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "दुकान की जानकारी सेट करें",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "दुकान का नाम (हर दुकानदार अपना नाम रख सकता है)",
                        fontSize = 12.sp,
                        color = KiranaTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = shopNameInput,
                        onValueChange = { shopNameInput = it },
                        placeholder = { Text("अपनी दुकान का नाम लिखें") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inpage_shop_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (shopNameInput.isNotBlank()) {
                                onSaveShopName(shopNameInput.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KiranaGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("inpage_save_shop_btn")
                    ) {
                        Text(
                            text = "दुकान का नाम सेव करें ✅",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Bottom Prototype Note
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "यह HD MART किराना ऐप है। ऐप का नाम HD MART ही रहेगा; दुकान का नाम हर खरीदार/दुकानदार अपनी सेटिंग में बदल सकेगा। ऑर्डर पर्ची WhatsApp पर तुरंत शेयर की जा सकती है।",
                fontSize = 12.sp,
                color = KiranaTextMuted,
                lineHeight = 17.sp,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 12.dp)
            )
        }
    }
}
