package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nameHindi: String,
    val nameEnglish: String,
    val category: String,
    val emoji: String,
    val price: Int,
    val oldPrice: Int,
    val unit: String,
    val isOffer: Boolean = false,
    val inStock: Boolean = true,
    val isFavorite: Boolean = false,
    val description: String = ""
) {
    val discountPercent: Int
        get() = if (oldPrice > price && oldPrice > 0) {
            (((oldPrice - price).toFloat() / oldPrice) * 100).toInt()
        } else {
            0
        }
}
