package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val itemsSummary: String,
    val subtotal: Int,
    val discount: Int,
    val deliveryFee: Int,
    val totalAmount: Int,
    val deliveryType: String, // "HOME_DELIVERY" or "STORE_PICKUP"
    val status: String = "RECEIVED" // "RECEIVED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED"
)
