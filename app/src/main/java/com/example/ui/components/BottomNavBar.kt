package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HDMartTab
import com.example.ui.theme.KiranaBorder
import com.example.ui.theme.KiranaGold
import com.example.ui.theme.KiranaGreenDark
import com.example.ui.theme.KiranaGreenPrimary
import com.example.ui.theme.KiranaMint
import com.example.ui.theme.KiranaSurface
import com.example.ui.theme.KiranaTextMuted

@Composable
fun BottomNavBar(
    selectedTab: HDMartTab,
    onTabSelected: (HDMartTab) -> Unit,
    cartCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = KiranaSurface,
        shadowElevation = 12.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            HorizontalDivider(color = KiranaBorder, thickness = 1.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavBarItem(
                    label = "होम",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    isSelected = selectedTab == HDMartTab.HOME,
                    onClick = { onTabSelected(HDMartTab.HOME) },
                    testTag = "nav_home"
                )

                NavBarItem(
                    label = "कैटेगरी",
                    selectedIcon = Icons.Filled.Category,
                    unselectedIcon = Icons.Outlined.Category,
                    isSelected = selectedTab == HDMartTab.CATEGORIES,
                    onClick = { onTabSelected(HDMartTab.CATEGORIES) },
                    testTag = "nav_categories"
                )

                NavBarItem(
                    label = "पसंदीदा",
                    selectedIcon = Icons.Filled.Favorite,
                    unselectedIcon = Icons.Outlined.FavoriteBorder,
                    isSelected = selectedTab == HDMartTab.FAVORITES,
                    onClick = { onTabSelected(HDMartTab.FAVORITES) },
                    testTag = "nav_favorites"
                )

                NavBarItem(
                    label = "कार्ट",
                    selectedIcon = Icons.Filled.ShoppingCart,
                    unselectedIcon = Icons.Outlined.ShoppingCart,
                    isSelected = selectedTab == HDMartTab.CART,
                    badgeCount = cartCount,
                    onClick = { onTabSelected(HDMartTab.CART) },
                    testTag = "nav_cart"
                )

                NavBarItem(
                    label = "दुकान",
                    selectedIcon = Icons.Filled.Storefront,
                    unselectedIcon = Icons.Outlined.Storefront,
                    isSelected = selectedTab == HDMartTab.SHOP_SETTINGS,
                    onClick = { onTabSelected(HDMartTab.SHOP_SETTINGS) },
                    testTag = "nav_shop"
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) KiranaMint else Color.Transparent)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(
                            containerColor = KiranaGold,
                            contentColor = KiranaGreenDark
                        ) {
                            Text(
                                text = "$badgeCount",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                    contentDescription = label,
                    tint = if (isSelected) KiranaGreenPrimary else KiranaTextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) KiranaGreenPrimary else KiranaTextMuted
        )
    }
}
