package com.example.QuickMeal.DataModels.Firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dish(
    val id: Int,
    val name: String,
    val category: String,
    val price: Long,
    val available: Boolean,
    val img: String ,
    val restaurant_id:Long
):Parcelable{
    constructor():this(0,"","",0,false,"",0)
}

