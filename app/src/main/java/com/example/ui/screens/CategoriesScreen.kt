package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.KiranaBorder
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary
import com.example.ui.theme.KiranaMint
import com.example.ui.theme.KiranaSurface
import com.example.ui.theme.KiranaTextPrimary
import com.example.ui.theme.KiranaTextSecondary

data class CategoryMeta(
    val name: String,
    val nameEn: String,
    val emoji: String,
    val colorHex: Long
)

val CATEGORY_META_LIST = listOf(
    CategoryMeta("अनाज", "Grains & Flour", "🌾", 0xFFFFF8E1),
    CategoryMeta("तेल व घी", "Oils & Ghee", "🫙", 0xFFFFF3E0),
    CategoryMeta("दालें", "Pulses & Dal", "🫘", 0xFFF1F8E9),
    CategoryMeta("मसाले", "Spices & Masala", "🌶️", 0xFFFFEBEE),
    CategoryMeta("चाय व चीनी", "Tea & Sugar", "☕", 0xFFEFEBE9),
    CategoryMeta("डेयरी", "Dairy & Milk", "🥛", 0xFFE0F7FA),
    CategoryMeta("स्नैक्स", "Biscuits & Snacks", "🍪", 0xFFFFFDE7),
    CategoryMeta("किराना", "Daily Grocery", "🧂", 0xFFE8F5E9),
    CategoryMeta("घरेलू सामान", "Household & Soaps", "🧼", 0xFFEDE7F6)
)

@Composable
fun CategoriesScreen(
    allProducts: List<Product>,
    cartItems: List<CartItemWithProduct>,
    onAddToCart: (Long) -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val categoryProducts = remember(selectedCategory, allProducts) {
        if (selectedCategory != null) {
            allProducts.filter { it.category == selectedCategory }
        } else {
            emptyList()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (selectedCategory == null) {
            // Category Grid View
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text(
                            text = "कैटेगरी अनुसार सामान",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaTextPrimary
                        )
                        Text(
                            text = "अपनी जरूरत के हिसाब से श्रेणी चुनें",
                            fontSize = 13.sp,
                            color = KiranaTextSecondary
                        )
                    }
                }

                items(CATEGORY_META_LIST) { meta ->
                    val count = allProducts.count { it.category == meta.name }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = KiranaSurface),
                        border = BorderStroke(1.dp, KiranaBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategory = meta.name }
                            .testTag("category_card_${meta.name}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                color = androidx.compose.ui.graphics.Color(meta.colorHex),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = meta.emoji, fontSize = 32.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = meta.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = KiranaGreenDark
                            )

                            Text(
                                text = meta.nameEn,
                                fontSize = 11.sp,
                                color = KiranaTextSecondary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                color = KiranaMint,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "$count उत्पाद",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = KiranaGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Selected Category Detail View
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { selectedCategory = null }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "वापस जाएं",
                        tint = KiranaGreenPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = selectedCategory ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KiranaGreenDark
                    )
                    Text(
                        text = "${categoryProducts.size} उत्पाद उपलब्ध",
                        fontSize = 12.sp,
                        color = KiranaTextSecondary
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categoryProducts, key = { it.id }) { product ->
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
        }
    }
}
