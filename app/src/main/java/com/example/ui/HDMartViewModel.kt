package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.HDMartDatabase
import com.example.data.HDMartRepository
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.ShopSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class HDMartTab {
    HOME,
    CATEGORIES,
    FAVORITES,
    CART,
    SHOP_SETTINGS
}

class HDMartViewModel(
    application: Application,
    private val repository: HDMartRepository
) : AndroidViewModel(application) {

    private val _currentTab = MutableStateFlow(HDMartTab.HOME)
    val currentTab: StateFlow<HDMartTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("सभी")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _showEditShopDialog = MutableStateFlow(false)
    val showEditShopDialog: StateFlow<Boolean> = _showEditShopDialog.asStateFlow()

    private val _showAddProductDialog = MutableStateFlow(false)
    val showAddProductDialog: StateFlow<Boolean> = _showAddProductDialog.asStateFlow()

    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    val shopSettings: StateFlow<ShopSettings> = repository.shopSettings
        .combine(MutableStateFlow(Unit)) { settings, _ ->
            settings ?: ShopSettings()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ShopSettings()
        )

    val cartItems: StateFlow<List<CartItemWithProduct>> = repository.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val cartCount: StateFlow<Int> = repository.cartCount
        .combine(MutableStateFlow(0)) { count, _ -> count ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val favoriteProducts: StateFlow<List<Product>> = repository.favoriteProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val offerProducts: StateFlow<List<Product>> = repository.offerProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filtered products based on search query and category
    val filteredProducts: StateFlow<List<Product>> = combine(
        repository.allProducts,
        _searchQuery,
        _selectedCategory
    ) { products, query, category ->
        var list = products

        if (category != "सभी") {
            list = list.filter { it.category == category }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase(Locale.ROOT)
            list = list.filter { product ->
                product.nameHindi.lowercase(Locale.ROOT).contains(q) ||
                product.nameEnglish.lowercase(Locale.ROOT).contains(q) ||
                product.category.lowercase(Locale.ROOT).contains(q)
            }
        }

        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectTab(tab: HDMartTab) {
        _currentTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setShowEditShopDialog(show: Boolean) {
        _showEditShopDialog.value = show
    }

    fun setShowAddProductDialog(show: Boolean) {
        _showAddProductDialog.value = show
    }

    fun setLastPlacedOrder(order: Order?) {
        _lastPlacedOrder.value = order
    }

    fun addToCart(productId: Long) {
        viewModelScope.launch {
            val current = cartItems.value.firstOrNull { it.cartItem.productId == productId }
            val newQty = (current?.cartItem?.quantity ?: 0) + 1
            repository.updateCartQuantity(productId, newQty)
        }
    }

    fun updateCartQuantity(productId: Long, delta: Int) {
        viewModelScope.launch {
            val current = cartItems.value.firstOrNull { it.cartItem.productId == productId }
            val currentQty = current?.cartItem?.quantity ?: 0
            val newQty = currentQty + delta
            repository.updateCartQuantity(productId, newQty)
        }
    }

    fun removeFromCart(productId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun toggleFavorite(productId: Long, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(productId, !currentFav)
        }
    }

    fun saveShopSettings(
        shopName: String,
        tagline: String,
        ownerName: String,
        phone: String,
        address: String,
        upiId: String
    ) {
        viewModelScope.launch {
            val current = shopSettings.value
            repository.saveShopSettings(
                current.copy(
                    shopName = shopName.ifBlank { "श्री गणेश किराणा स्टोर" },
                    tagline = tagline.ifBlank { "हाई डिस्काउंट मार्ट · ऑनलाइन किराना" },
                    ownerName = ownerName.ifBlank { "दुकानदार" },
                    phone = phone.ifBlank { "+91 98290 12345" },
                    address = address.ifBlank { "मेन मार्केट" },
                    upiId = upiId.ifBlank { "kirana@upi" }
                )
            )
            _showEditShopDialog.value = false
        }
    }

    fun addNewProduct(
        nameHindi: String,
        nameEnglish: String,
        category: String,
        price: Int,
        oldPrice: Int,
        unit: String,
        emoji: String,
        isOffer: Boolean
    ) {
        viewModelScope.launch {
            val product = Product(
                nameHindi = nameHindi,
                nameEnglish = nameEnglish.ifBlank { nameHindi },
                category = category,
                emoji = emoji.ifBlank { "🛍️" },
                price = price,
                oldPrice = if (oldPrice > 0) oldPrice else price,
                unit = unit.ifBlank { "1 नग" },
                isOffer = isOffer,
                inStock = true
            )
            repository.insertProduct(product)
            _showAddProductDialog.value = false
        }
    }

    fun placeOrder(
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        deliveryType: String
    ) {
        viewModelScope.launch {
            val items = cartItems.value
            if (items.isEmpty()) return@launch

            val itemsSummary = items.joinToString("\n") {
                "• ${it.product.nameHindi} (${it.product.unit}) x ${it.cartItem.quantity} = ₹${it.product.price * it.cartItem.quantity}"
            }

            val subtotal = items.sumOf { it.product.price * it.cartItem.quantity }
            val originalTotal = items.sumOf { it.product.oldPrice * it.cartItem.quantity }
            val discount = if (originalTotal > subtotal) originalTotal - subtotal else 0

            val currentSettings = shopSettings.value
            val isFreeDelivery = deliveryType == "STORE_PICKUP" || subtotal >= currentSettings.freeDeliveryAbove
            val deliveryFee = if (isFreeDelivery) 0 else currentSettings.deliveryFee
            val totalAmount = subtotal + deliveryFee

            val randomSuffix = (1000..9999).random()
            val orderNumber = "HD-$randomSuffix"

            val order = Order(
                orderNumber = orderNumber,
                timestamp = System.currentTimeMillis(),
                customerName = customerName.ifBlank { "ग्राहक" },
                customerPhone = customerPhone.ifBlank { "9876543210" },
                customerAddress = if (deliveryType == "STORE_PICKUP") "दुकान से पिकअप (Store Pickup)" else customerAddress.ifBlank { "घर का पता" },
                itemsSummary = itemsSummary,
                subtotal = subtotal,
                discount = discount,
                deliveryFee = deliveryFee,
                totalAmount = totalAmount,
                deliveryType = deliveryType,
                status = "RECEIVED"
            )

            val orderId = repository.placeOrder(order)
            _lastPlacedOrder.value = order.copy(id = orderId)
        }
    }

    fun shareOrderOnWhatsApp(context: Context, order: Order) {
        val settings = shopSettings.value
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault())
        val dateStr = dateFormat.format(Date(order.timestamp))

        val message = """
🏪 *${settings.shopName}*
_${settings.tagline}_
----------------------------------
🧾 *ऑर्डर बिल (Order Slip)*
📌 ऑर्डर सं.: *#${order.orderNumber}*
📅 दिनांक: $dateStr
👤 ग्राहक: ${order.customerName}
📞 संपर्क: ${order.customerPhone}
🚚 प्रकार: ${if (order.deliveryType == "HOME_DELIVERY") "होम डिलीवरी" else "दुकान से पिकअप"}
📍 पता: ${order.customerAddress}
----------------------------------
🛒 *सामान विवरण:*
${order.itemsSummary}
----------------------------------
💰 कुल सामान मूल्य: ₹${order.subtotal}
🎁 कुल बचत: ₹${order.discount}
🛵 डिलीवरी शुल्क: ${if (order.deliveryFee == 0) "मुफ्त (FREE)" else "₹" + order.deliveryFee}
💵 *कुल भुगतान राशि: ₹${order.totalAmount}*
----------------------------------
💳 UPI भुगतान: ${settings.upiId}
📞 दुकान संपर्क: ${settings.phone}
🙏 धन्यवाद! फिर पधारें!
""".trimIndent()

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(sendIntent, "ऑर्डर शेयर करें (WhatsApp / संदेश)")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = HDMartDatabase.getDatabase(
                        application,
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)
                    )
                    val repository = HDMartRepository(
                        productDao = db.productDao(),
                        cartDao = db.cartDao(),
                        shopSettingsDao = db.shopSettingsDao(),
                        orderDao = db.orderDao()
                    )
                    return HDMartViewModel(application, repository) as T
                }
            }
    }
}
