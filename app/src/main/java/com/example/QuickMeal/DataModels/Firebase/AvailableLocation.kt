package com.example.QuickMeal.DataModels.Firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AvailableLocation(
    val id
:String,
    val name:String,
    val searchTerm
:String
):Parcelable {
    constructor():this("","","")
}