package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.CartDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ProductDao
import com.example.data.dao.ShopSettingsDao
import com.example.data.model.CartItem
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.ShopSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Product::class,
        CartItem::class,
        ShopSettings::class,
        Order::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HDMartDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun shopSettingsDao(): ShopSettingsDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: HDMartDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HDMartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HDMartDatabase::class.java,
                    "hdmart_kirana_database"
                )
                .addCallback(HDMartDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class HDMartDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: HDMartDatabase) {
            val productDao = database.productDao()
            val settingsDao = database.shopSettingsDao()

            if (settingsDao.getSettingsSync() == null) {
                settingsDao.saveSettings(
                    ShopSettings(
                        id = 1,
                        shopName = "श्री गणेश किराणा स्टोर",
                        tagline = "हाई डिस्काउंट मार्ट · शुद्ध व ताजा किराना",
                        ownerName = "राजेश गुप्ता",
                        phone = "+91 98290 12345",
                        address = "दुकान नं. 15, मुख्य बाजार, चौक के पास",
                        upiId = "ganeshkirana@upi",
                        freeDeliveryAbove = 499,
                        deliveryFee = 30
                    )
                )
            }

            if (productDao.getProductCount() == 0) {
                val initialProducts = listOf(
                    Product(
                        nameHindi = "चावल (5 किलो)",
                        nameEnglish = "Basmati Rice (5 kg)",
                        category = "अनाज",
                        emoji = "🍚",
                        price = 299,
                        oldPrice = 349,
                        unit = "5 किलो",
                        isOffer = true,
                        inStock = true,
                        description = "उत्तम दानेदार बासमती चावल, बिरयानी व रोजमर्रा के लिए"
                    ),
                    Product(
                        nameHindi = "सरसों तेल (1 लीटर)",
                        nameEnglish = "Mustard Oil (1 L)",
                        category = "तेल व घी",
                        emoji = "🫙",
                        price = 145,
                        oldPrice = 165,
                        unit = "1 लीटर",
                        isOffer = true,
                        inStock = true,
                        description = "शुद्ध कच्ची घानी सरसों का तेल, पक्का स्वाद"
                    ),
                    Product(
                        nameHindi = "आटा (5 किलो)",
                        nameEnglish = "Chakki Fresh Atta (5 kg)",
                        category = "अनाज",
                        emoji = "🌾",
                        price = 210,
                        oldPrice = 240,
                        unit = "5 किलो",
                        isOffer = true,
                        inStock = true,
                        description = "100% शुद्ध गेहूं से निर्मित चक्की का ताजा आटा"
                    ),
                    Product(
                        nameHindi = "चीनी (1 किलो)",
                        nameEnglish = "Refined Sugar (1 kg)",
                        category = "चाय व चीनी",
                        emoji = "🧂",
                        price = 44,
                        oldPrice = 50,
                        unit = "1 किलो",
                        isOffer = true,
                        inStock = true,
                        description = "सफेद साफ दानेदार चीनी"
                    ),
                    Product(
                        nameHindi = "दूध (1 लीटर)",
                        nameEnglish = "Fresh Milk (1 L)",
                        category = "डेयरी",
                        emoji = "🥛",
                        price = 58,
                        oldPrice = 62,
                        unit = "1 लीटर",
                        isOffer = true,
                        inStock = true,
                        description = "ताजा फुल क्रीम पौष्टिक दूध"
                    ),
                    Product(
                        nameHindi = "तुअर दाल (1 किलो)",
                        nameEnglish = "Toor / Arhar Dal (1 kg)",
                        category = "दालें",
                        emoji = "🫘",
                        price = 119,
                        oldPrice = 139,
                        unit = "1 किलो",
                        isOffer = true,
                        inStock = true,
                        description = "पॉलिश रहित शुद्ध पौष्टिक तुअर दाल"
                    ),
                    Product(
                        nameHindi = "देसी घी (1 लीटर)",
                        nameEnglish = "Pure Desi Ghee (1 L)",
                        category = "तेल व घी",
                        emoji = "🧈",
                        price = 540,
                        oldPrice = 620,
                        unit = "1 लीटर",
                        isOffer = true,
                        inStock = true,
                        description = "दानेदार खुशबूदार शुद्ध गाय का देसी घी"
                    ),
                    Product(
                        nameHindi = "प्रीमियम चाय पत्ती (500 ग्राम)",
                        nameEnglish = "Premium Tea (500 g)",
                        category = "चाय व चीनी",
                        emoji = "☕",
                        price = 175,
                        oldPrice = 210,
                        unit = "500 ग्राम",
                        isOffer = true,
                        inStock = true,
                        description = "कड़क और खुशबूदार असम की पत्ती"
                    ),
                    Product(
                        nameHindi = "हल्दी पाउडर (200 ग्राम)",
                        nameEnglish = "Turmeric Powder (200 g)",
                        category = "मसाले",
                        emoji = "🌿",
                        price = 48,
                        oldPrice = 60,
                        unit = "200 ग्राम",
                        isOffer = false,
                        inStock = true,
                        description = "प्राकृतिक रंग व औषधीय गुणों से भरपूर"
                    ),
                    Product(
                        nameHindi = "लाल मिर्च पाउडर (200 ग्राम)",
                        nameEnglish = "Red Chilli Powder (200 g)",
                        category = "मसाले",
                        emoji = "🌶️",
                        price = 65,
                        oldPrice = 80,
                        unit = "200 ग्राम",
                        isOffer = false,
                        inStock = true,
                        description = "तीखी व चटक लाल मिर्च"
                    ),
                    Product(
                        nameHindi = "मूंग दाल धुली (1 किलो)",
                        nameEnglish = "Yellow Moong Dal (1 kg)",
                        category = "दालें",
                        emoji = "🫘",
                        price = 108,
                        oldPrice = 125,
                        unit = "1 किलो",
                        isOffer = false,
                        inStock = true,
                        description = "हल्की व सुपाच्य पीली मूंग दाल"
                    ),
                    Product(
                        nameHindi = "पारले-जी ग्लूकोज बिस्कुट",
                        nameEnglish = "Parle-G Family Pack",
                        category = "स्नैक्स",
                        emoji = "🍪",
                        price = 68,
                        oldPrice = 80,
                        unit = "800 ग्राम",
                        isOffer = true,
                        inStock = true,
                        description = "भारत का पसंदीदा चाय का साथी"
                    ),
                    Product(
                        nameHindi = "मैगी 2-मिनट नूडल्स (4 पैक)",
                        nameEnglish = "Maggi 2-Min Noodles (Pack of 4)",
                        category = "स्नैक्स",
                        emoji = "🍜",
                        price = 54,
                        oldPrice = 60,
                        unit = "280 ग्राम",
                        isOffer = false,
                        inStock = true,
                        description = "मसालेदार टेस्टमेकर के साथ क्विक स्नैक"
                    ),
                    Product(
                        nameHindi = "रिन डिटर्जेंट बार (4 का पैक)",
                        nameEnglish = "Rin Detergent Bar (4 Pack)",
                        category = "घरेलू सामान",
                        emoji = "🧼",
                        price = 72,
                        oldPrice = 85,
                        unit = "1 किलो",
                        isOffer = false,
                        inStock = true,
                        description = "चमकदार कपड़ों की धुलाई के लिए"
                    ),
                    Product(
                        nameHindi = "पनीर (200 ग्राम)",
                        nameEnglish = "Fresh Malai Paneer (200 g)",
                        category = "डेयरी",
                        emoji = "🧀",
                        price = 85,
                        oldPrice = 95,
                        unit = "200 ग्राम",
                        isOffer = false,
                        inStock = true,
                        description = "ताजा मलाईदार पनीर"
                    ),
                    Product(
                        nameHindi = "टाटा नमक (1 किलो)",
                        nameEnglish = "Tata Salt Vacuum Evaporated (1 kg)",
                        category = "किराना",
                        emoji = "🧂",
                        price = 26,
                        oldPrice = 28,
                        unit = "1 किलो",
                        isOffer = false,
                        inStock = true,
                        description = "देश का नमक, आयोडीन युक्त"
                    )
                )
                productDao.insertProducts(initialProducts)
            }
        }
    }
}
