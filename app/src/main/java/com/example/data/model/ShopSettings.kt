package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_settings")
data class ShopSettings(
    @PrimaryKey
    val id: Int = 1,
    val shopName: String = "श्री गणेश किराणा स्टोर",
    val tagline: String = "हाई डिस्काउंट मार्ट · ऑनलाइन किराना",
    val ownerName: String = "दुकानदार",
    val phone: String = "+91 98765 43210",
    val address: String = "मेन मार्केट, किराना गली, दुकान नं. 12",
    val upiId: String = "hdmart@upi",
    val freeDeliveryAbove: Int = 499,
    val deliveryFee: Int = 30
)
