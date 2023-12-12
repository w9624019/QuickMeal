package com.example.QuickMeal.DataModels

import android.os.Parcelable
import com.example.QuickMeal.DataModels.Firebase.Dish
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(

    val quantity:Int,
    val product: Dish
) : Parcelable {
    constructor():this(1,Dish())
}