package com.example.QuickMeal.DataModels.Firebase

data class Location(
    val name: String,
    val restaurants: List<Restaurant>,
    val Location_id:String
){
    constructor():this("", emptyList(),"")
}