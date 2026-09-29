package com.example.data

import com.example.data.dao.CartDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ProductDao
import com.example.data.dao.ShopSettingsDao
import com.example.data.model.CartItem
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.ShopSettings
import kotlinx.coroutines.flow.Flow

class HDMartRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val shopSettingsDao: ShopSettingsDao,
    private val orderDao: OrderDao
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val offerProducts: Flow<List<Product>> = productDao.getOffers()
    val favoriteProducts: Flow<List<Product>> = productDao.getFavorites()
    val cartItems: Flow<List<CartItemWithProduct>> = cartDao.getCartItems()
    val cartCount: Flow<Int?> = cartDao.getTotalCartCount()
    val shopSettings: Flow<ShopSettings?> = shopSettingsDao.getSettings()
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()

    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)

    fun getProductsByCategory(category: String): Flow<List<Product>> =
        productDao.getProductsByCategory(category)

    suspend fun toggleFavorite(productId: Long, isFavorite: Boolean) {
        productDao.updateFavorite(productId, isFavorite)
    }

    suspend fun addToCart(productId: Long, quantity: Int = 1) {
        cartDao.insertOrUpdateCart(CartItem(productId = productId, quantity = quantity))
    }

    suspend fun updateCartQuantity(productId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(productId)
        } else {
            cartDao.insertOrUpdateCart(CartItem(productId = productId, quantity = quantity))
        }
    }

    suspend fun removeFromCart(productId: Long) {
        cartDao.deleteCartItem(productId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun saveShopSettings(settings: ShopSettings) {
        shopSettingsDao.saveSettings(settings)
    }

    suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun placeOrder(order: Order): Long {
        val orderId = orderDao.insertOrder(order)
        cartDao.clearCart()
        return orderId
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }
}
