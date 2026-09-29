package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.HDMartTab
import com.example.ui.HDMartViewModel
import com.example.ui.components.AddProductDialog
import com.example.ui.components.BottomNavBar
import com.example.ui.components.EditShopDialog
import com.example.ui.components.HDMartTopBar
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ShopSettingsScreen
import com.example.ui.theme.HDMartTheme
import com.example.ui.theme.KiranaBackground

class MainActivity : ComponentActivity() {

    private val viewModel: HDMartViewModel by viewModels {
        HDMartViewModel.provideFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HDMartTheme {
                HDMartApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun HDMartApp(viewModel: HDMartViewModel) {
    val context = LocalContext.current

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val shopSettings by viewModel.shopSettings.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartCount by viewModel.cartCount.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val favoriteProducts by viewModel.favoriteProducts.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val showEditShopDialog by viewModel.showEditShopDialog.collectAsStateWithLifecycle()
    val showAddProductDialog by viewModel.showAddProductDialog.collectAsStateWithLifecycle()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsStateWithLifecycle()

    // Handle back button on sub-tabs
    BackHandler(enabled = currentTab != HDMartTab.HOME) {
        viewModel.selectTab(HDMartTab.HOME)
    }

    Scaffold(
        topBar = {
            HDMartTopBar(
                shopSettings = shopSettings,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onEditShopClick = { viewModel.setShowEditShopDialog(true) },
                showSearch = currentTab == HDMartTab.HOME || currentTab == HDMartTab.CATEGORIES
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                cartCount = cartCount
            )
        },
        containerColor = KiranaBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(KiranaBackground)
        ) {
            when (currentTab) {
                HDMartTab.HOME -> {
                    HomeScreen(
                        products = filteredProducts,
                        cartItems = cartItems,
                        shopSettings = shopSettings,
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.selectCategory(it) },
                        onAddToCart = { viewModel.addToCart(it) },
                        onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                        onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) },
                        onSaveShopName = { name ->
                            viewModel.saveShopSettings(
                                shopName = name,
                                tagline = shopSettings.tagline,
                                ownerName = shopSettings.ownerName,
                                phone = shopSettings.phone,
                                address = shopSettings.address,
                                upiId = shopSettings.upiId
                            )
                        }
                    )
                }

                HDMartTab.CATEGORIES -> {
                    CategoriesScreen(
                        allProducts = filteredProducts,
                        cartItems = cartItems,
                        onAddToCart = { viewModel.addToCart(it) },
                        onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                        onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) }
                    )
                }

                HDMartTab.FAVORITES -> {
                    FavoritesScreen(
                        favoriteProducts = favoriteProducts,
                        cartItems = cartItems,
                        onAddToCart = { viewModel.addToCart(it) },
                        onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                        onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) }
                    )
                }

                HDMartTab.CART -> {
                    CartScreen(
                        cartItems = cartItems,
                        shopSettings = shopSettings,
                        onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                        onRemoveItem = { viewModel.removeFromCart(it) },
                        onClearCart = { viewModel.clearCart() },
                        onPlaceOrder = { name, phone, address, deliveryType ->
                            viewModel.placeOrder(name, phone, address, deliveryType)
                        },
                        onExploreProducts = { viewModel.selectTab(HDMartTab.HOME) }
                    )
                }

                HDMartTab.SHOP_SETTINGS -> {
                    ShopSettingsScreen(
                        shopSettings = shopSettings,
                        orders = allOrders,
                        onEditShopClick = { viewModel.setShowEditShopDialog(true) },
                        onAddProductClick = { viewModel.setShowAddProductDialog(true) },
                        onShareOrderWhatsApp = { order ->
                            viewModel.shareOrderOnWhatsApp(context, order)
                        }
                    )
                }
            }
        }
    }

    // Edit Shop Info Dialog
    if (showEditShopDialog) {
        EditShopDialog(
            currentSettings = shopSettings,
            onDismiss = { viewModel.setShowEditShopDialog(false) },
            onSave = { name, tagline, owner, phone, addr, upi ->
                viewModel.saveShopSettings(name, tagline, owner, phone, addr, upi)
            }
        )
    }

    // Add New Product Dialog
    if (showAddProductDialog) {
        AddProductDialog(
            onDismiss = { viewModel.setShowAddProductDialog(false) },
            onAdd = { nameHi, nameEn, cat, price, oldPrice, unit, emoji, isOffer ->
                viewModel.addNewProduct(nameHi, nameEn, cat, price, oldPrice, unit, emoji, isOffer)
            }
        )
    }

    // Order Success & WhatsApp Bill Dialog
    lastPlacedOrder?.let { order ->
        OrderSuccessDialog(
            order = order,
            shopSettings = shopSettings,
            onDismiss = { viewModel.setLastPlacedOrder(null) },
            onShareWhatsApp = {
                viewModel.shareOrderOnWhatsApp(context, order)
            }
        )
    }
}
