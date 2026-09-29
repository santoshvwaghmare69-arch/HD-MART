package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaTextSecondary

@Composable
fun FavoritesScreen(
    favoriteProducts: List<Product>,
    cartItems: List<CartItemWithProduct>,
    onAddToCart: (Long) -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (favoriteProducts.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🤍", fontSize = 54.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "कोई पसंदीदा सामान नहीं है",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = KiranaGreenDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "रोजमर्रा के किराना सामान पर दिल (♡) दबाएं ताकि आप उन्हें एक क्लिक में दोबारा ऑर्डर कर सकें।",
                fontSize = 13.sp,
                color = KiranaTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxSize()
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "आपके पसंदीदा सामान (${favoriteProducts.size})",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = KiranaGreenDark
                    )
                    Text(
                        text = "रोज के जरूरी राशन का त्वरित ऑर्डर",
                        fontSize = 12.sp,
                        color = KiranaTextSecondary
                    )
                }
            }

            items(favoriteProducts, key = { it.id }) { product ->
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
