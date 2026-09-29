package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.theme.KiranaBorder
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary
import com.example.ui.theme.KiranaMint
import com.example.ui.theme.KiranaSurface
import com.example.ui.theme.KiranaTextMuted
import com.example.ui.theme.KiranaTextPrimary
import com.example.ui.theme.KiranaTextSecondary

@Composable
fun ProductCard(
    product: Product,
    cartQuantity: Int,
    onAddToCart: () -> Unit,
    onUpdateQuantity: (Int) -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = KiranaSurface),
        border = BorderStroke(1.dp, KiranaBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product Visual Block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F5EF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.emoji,
                    fontSize = 42.sp
                )

                // Discount Pill if on offer
                if (product.discountPercent > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE53935))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${product.discountPercent}% छूट",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Favorite button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.85f))
                        .testTag("fav_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "पसंदीदा",
                        tint = if (product.isFavorite) Color(0xFFE53935) else KiranaTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            // Title (Hindi)
            Text(
                text = product.nameHindi,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = KiranaTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Subtitle / English Name & Unit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.category,
                    fontSize = 11.sp,
                    color = KiranaTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.unit,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = KiranaGreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Price Row
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "₹${product.price}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = KiranaGreenDark
                )
                if (product.oldPrice > product.price) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "₹${product.oldPrice}",
                        fontSize = 12.sp,
                        color = KiranaTextMuted,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Add to Cart / Quantity Stepper
            if (cartQuantity == 0) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .clickable { onAddToCart() }
                        .testTag("add_cart_btn_${product.id}"),
                    shape = RoundedCornerShape(9.dp),
                    color = KiranaSurface,
                    border = BorderStroke(1.5.dp, KiranaGreenPrimary)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "＋ कार्ट में डालें",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = KiranaGreenPrimary
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp),
                    shape = RoundedCornerShape(9.dp),
                    color = KiranaMint,
                    border = BorderStroke(1.dp, KiranaGreenPrimary)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onUpdateQuantity(-1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "घटाएं",
                                tint = KiranaGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "$cartQuantity",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KiranaGreenDark
                        )

                        IconButton(
                            onClick = { onUpdateQuantity(1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "बढ़ाएं",
                                tint = KiranaGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
