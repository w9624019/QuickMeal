package com.example.QuickMeal.DataModels.Firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restaurant(val id: Int, val name: String, val dishes: List<Dish>,val img:String):Parcelable {
    constructor():this(0,"", emptyList(),"")
}
