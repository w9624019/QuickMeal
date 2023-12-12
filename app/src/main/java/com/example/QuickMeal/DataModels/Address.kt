package com.example.QuickMeal.DataModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Address(
    val addressTitle:String,
    val City:String,
    val fullName:String,
    val State:String,
    val street:String,
    val Phone:String
) :Parcelable{
    constructor():this("","","","","","")
}
